package com.sravan.efactorapp.UI.Fragments.Gateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.sravan.efactorapp.Adapter.NewDeviceAdapter;
import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.BLE.BLECommHandler;
import com.sravan.efactorapp.BLE.BLEConstants;
import com.sravan.efactorapp.BLE.NotificationCallback;
import com.sravan.efactorapp.BLE.OperationResultCallback;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Event.EventType;
import com.sravan.efactorapp.Event.Message;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.NewDeviceData;
import com.sravan.efactorapp.Model.UserLogin;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.ConnectionDetector;
import com.sravan.efactorapp.utils.Crypto.CryptoDH;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;
import com.sravan.efactorapp.utils.LocalRPICommunication;
import com.sravan.efactorapp.utils.MDNSQueryHandler;
import com.sravan.efactorapp.utils.StaticStorage;
import com.sravan.efactorapp.utils.Utilities;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class AddGateway extends BaseFragment implements NewDeviceAdapter.OnNewDeviceClickListener, NotificationCallback, ApiHitListener {

    private static final String TAG = AddGateway.class.getSimpleName();
    private final int MAX_STATUS_CHECK_RETRY = 5;
    private final int SCAN_TIMEOUT_SEC = 5;
    private String bleDeviceAESKey = null;
    private HashMap<String, RxBleDevice> bluetoothDevices;
    private Button buttonAddDevice;
    private Button buttonScan;
    private CryptoDH cryptoDH;
    private EditText editTextName;
    private TextInputEditText editTextPassword;
    private String gatewayId = null;
    private ImageView imageViewRefresh;
    private boolean isAdding = false;

    private String UserId;
    private SessionManager sessionManager;
    private RestClient restClient;
    //private LocalRPICommunication localRPICommunication;
    private NewDeviceAdapter newDeviceAdapter;
    private HashMap<String, String> newDeviceDataMap;
    private ProgressBar progressBarScanning;
    private RecyclerView recyclerViewItemList;
    private int retryCount = 0;
    private View rootView;
    private Disposable scanSubscription = null;
    private RxBleDevice selectedBleDevice;
    private NewDeviceData selectedDevice = null;
    private String selectedType;
    private Spinner spinnerWifi;
    private Disposable timerSubscription = null;
    private long updated_at = 0;
    private ConstraintLayout wifiDetailsLayout;
    private List<String> wifiList;
    private WifiManager wifiManager;
    private LottieAnimationView animation;
    private ScrollView scrollView;
    private ArrayAdapter<String> wifiSpinnerAdapter;
    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {


        public void onReceive(Context context, Intent intent) {
            List<android.net.wifi.ScanResult> results = AddGateway.this.wifiManager.getScanResults();
            AddGateway.this.wifiList.clear();
            AddGateway.this.wifiList.add("Select WiFi Network");
            for (android.net.wifi.ScanResult result : results) {
                if (result.SSID != null && result.SSID.length() > 0 && result.frequency <= 2500) {
                    AddGateway.this.wifiList.add(result.SSID);
                }
            }
            if (!AddGateway.this.isAdding) {
                AddGateway.this.getActivity().runOnUiThread(new Runnable() {


                    public final void run() {
                        AddGateway.this.lambda$onReceive$0$AddDeviceFragment$2();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onReceive$0$AddDeviceFragment$2() {
            dismissProgressBar();
            AddGateway.this.wifiSpinnerAdapter.notifyDataSetChanged();
        }
    };
    private LocalRPICommunication localRPICommunication;

    private void lambda$onReceive$0$AddDeviceFragment$2() {
        dismissProgressBar();
        AddGateway.this.wifiSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_add_gateway;
    }

    @Override
    protected void findView() {
      //  animation = (LottieAnimationView) findViewByIds(R.id.anim_layout);
        scrollView = (ScrollView) findViewByIds(R.id.scrollView);
//        animation.setVisibility(View.VISIBLE);
        recyclerViewItemList = (RecyclerView) findViewByIds(R.id.recyclerViewDevices);
        recyclerViewItemList.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewItemList.setAdapter(this.newDeviceAdapter);
        spinnerWifi = (Spinner) findViewByIds(R.id.spinnerWifi);
        spinnerWifi.setAdapter((SpinnerAdapter) this.wifiSpinnerAdapter);
        spinnerWifi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                AddGateway.this.editTextPassword.setText((CharSequence) null);
                if (position == 0) {
                    AddGateway.this.editTextPassword.setEnabled(false);
                } else {
                    AddGateway.this.editTextPassword.setEnabled(true);
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.editTextPassword = (TextInputEditText) findViewByIds(R.id.editTextWifiPass);
        this.editTextPassword.setEnabled(false);
        this.imageViewRefresh = (ImageView) findViewByIds(R.id.imageViewRefresh);
        this.imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                // AddGateway.this.lambda$onCreateView$0$AddDeviceFragment(view);
            }
        });
        this.editTextName = (EditText) findViewByIds(R.id.editTextName);
        this.editTextName.setText("");
        this.wifiDetailsLayout = (ConstraintLayout) findViewByIds(R.id.wifiDetailsView);
        this.buttonAddDevice = (Button) findViewByIds(R.id.buttonSubmit);
        this.buttonAddDevice.setEnabled(false);
        this.buttonAddDevice.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {

                //TODO AdD GATEWAY FUNCTION
                AddGateway.this.lambda$onCreateView$1$AddDeviceFragment(view);
            }
        });
        this.editTextName.setVisibility(View.GONE);
        this.wifiDetailsLayout.setVisibility(View.GONE);
        this.buttonAddDevice.setVisibility(View.GONE);
        // this.loadingDialog = new LoadingDialog(getActivity());

        ((TextView) findViewByIds(R.id.textView3)).setText(String.format("Select the %s from below to add", "Gateway"));
        ((TextView) findViewByIds(R.id.textView4)).setText(String.format("Select WiFi network to connect the %s", "Gateway"));
        this.editTextName.setHint("Enter Gateway Name");
        this.buttonAddDevice.setText("Add Gateway");
       /* } else {
            ((TextView) this.rootView.findViewById(R.id.textView3)).setText(String.format("Select the %s from below to add", "Device"));
            ((TextView) this.rootView.findViewById(R.id.textView4)).setText(String.format("Select WiFi network to connect the %s", "Device"));
            this.editTextName.setHint("Enter Device Name");
            this.buttonAddDevice.setText("Add Device");
        }*/
        startScanning();
        this.buttonScan = (Button) findViewByIds(R.id.buttonScan);
        this.buttonScan.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startScanning();

            }
        });
    }


    @Override
    protected void init() {
        MDNSQueryHandler.init(getContext());
        sessionManager = new SessionManager(getContext());
        restClient = new RestClient(getContext());
        this.newDeviceDataMap = new HashMap<>();
        this.newDeviceAdapter = new NewDeviceAdapter(this);
        this.wifiList = new ArrayList();
        this.wifiList.add("Select WiFi Network");
        this.wifiSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.select_dialog_item_material, this.wifiList);
        this.wifiSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
        this.bluetoothDevices = new HashMap<>();
        this.cryptoDH = new CryptoDH(1024);
    }

    private void startScanning() {
        Disposable disposable = this.scanSubscription;
        if (disposable == null || disposable.isDisposed()) {
            startScanTimer();
            Log.e(TAG, "StartScanning : " + App.getBleClient());
            this.scanSubscription = App.getBleClient().
                    scanBleDevices(new ScanSettings.Builder().build(), new ScanFilter.Builder()./*setManufacturerData(17989, new byte[]{65, 67, 84, 79}).*/build())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate() {

                        @Override // io.reactivex.functions.Predicate
                        public final boolean test(Object obj) {
                            Log.e(TAG, "OBJ!" + obj);
                            return AddGateway.this.lambda$startScanning$5$AddDeviceFragment((com.polidea.rxandroidble2.scan.ScanResult) obj);
                        }
                    }).subscribe(new Consumer() {

                        @Override // io.reactivex.functions.Consumer
                        public final void accept(Object obj) {
                            Log.e(TAG, "OBJ2" + obj);
                            AddGateway.this.lambda$startScanning$6$AddDeviceFragment((com.polidea.rxandroidble2.scan.ScanResult) obj);
                        }
                    }, $$Lambda$AddDeviceFragment$W6UW3Y9n7L5DPH5bpHChfUdNrwE.INSTANCE);
        }
    }

    public /* synthetic */ boolean lambda$startScanning$5$AddDeviceFragment(com.polidea.rxandroidble2.scan.ScanResult scanResult) {
        RxBleDevice device = scanResult.getBleDevice();
        Log.e(TAG,"Result : "+scanResult.getBleDevice());
        Log.e(TAG, "Device Result : " + device.getName());
        if (device.getName() == null || this.bluetoothDevices.containsKey(device.getName())) {
            return false;

        }
        this.bluetoothDevices.put(device.getName(), device);
        return true;

    }

    public /* synthetic */ void lambda$startScanning$6$AddDeviceFragment(com.polidea.rxandroidble2.scan.ScanResult scanResult) {
        String str = TAG;
        Toast.makeText(getContext(), "ScanResult Received: " + scanResult.getBleDevice().getName(), Toast.LENGTH_SHORT).show();
        Log.i(str, "ScanResult Received: " + scanResult.getBleDevice().getName());
        NewDeviceData newDeviceData = new NewDeviceData(scanResult.getBleDevice().getName(), "v1.0");
        newDeviceData.setType("PROD_GATEWAY");
        newDeviceData.setIp("BLE");
        //animation.setVisibility(View.GONE);
       // scrollView.setVisibility(View.VISIBLE);
          this.progressBarScanning.setVisibility(View.GONE);
        if (!this.newDeviceDataMap.containsKey(newDeviceData.getApn())) {
            this.newDeviceDataMap.put(newDeviceData.getApn(), newDeviceData.getIp());
            this.newDeviceAdapter.addItem(newDeviceData);
        }
    }

    static /* synthetic */ void lambda$startScanning$7(Throwable throwable) {
        String str = TAG;
        Log.d(str, "Error Scanning: " + throwable);
    }


    private void startScanTimer() {
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
            this.timerSubscription = null;
        }
        this.timerSubscription = Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddGateway.this.lambda$startScanTimer$4$AddDeviceFragment((Long) obj);
            }
        });
    }

    public /* synthetic */ void lambda$startScanTimer$4$AddDeviceFragment(Long aLong) {
        Log.i(TAG, "Timer Completed -> Stop Scan");
        Disposable disposable = this.scanSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.scanSubscription.dispose();
        }
    }
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$efactor$factor_app$Event$EventType = new int[EventType.values().length];

        static {
            try {
                $SwitchMap$com$efactor$factor_app$Event$EventType[EventType.GATEWAY_FOUND_NEW.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
        if (AnonymousClass3.$SwitchMap$com$efactor$factor_app$Event$EventType[message.getEventType().ordinal()] == 1) {
            try {
                String str = TAG;
                Log.d(str, "New Gateway: " + message.getJsonData());
                NewDeviceData newDeviceData = new NewDeviceData(message.getJsonData().getString("apn"), message.getJsonData().getString("ver"));
                newDeviceData.setIp(message.getJsonData().getString("ip"));
                this.progressBarScanning.setVisibility(View.GONE);
                if (!this.newDeviceDataMap.containsKey(newDeviceData.getApn())) {
                    this.newDeviceDataMap.put(newDeviceData.getApn(), newDeviceData.getIp());
                    this.newDeviceAdapter.addItem(newDeviceData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onNewDeviceItemClick(NewDeviceData deviceData) {
        String str = TAG;
        Log.d(str, "Device Name: " + deviceData.getApn());
        this.selectedDevice = deviceData;
        getActivity().runOnUiThread(new Runnable() {

            public final void run() {
                AddGateway.this.lambda$onNewDeviceItemClick$3$AddDeviceFragment();
            }
        });
        if (/*this.selectedType.equals(GatewayDao.TABLENAME) &&*/ !this.wifiManager.startScan()) {
            Log.e(TAG, "Failed to start scan");
        }
    }

    public /* synthetic */ void lambda$onNewDeviceItemClick$3$AddDeviceFragment() {
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
            this.timerSubscription = null;
        }
        Disposable disposable2 = this.scanSubscription;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.scanSubscription.dispose();
            this.scanSubscription = null;
        }
        displayProgressBar(false);
        // this.loadingDialog.show();
        // if (this.selectedType.equals(GatewayDao.TABLENAME)) {
        this.editTextName.setVisibility(View.VISIBLE);
        this.wifiDetailsLayout.setVisibility(View.VISIBLE);
        this.buttonAddDevice.setVisibility(View.VISIBLE);
        /*} else {
            this.editTextName.setVisibility(View.VISIBLE);
            this.buttonAddDevice.setVisibility(View.VISIBLE);
        }*/
        this.buttonAddDevice.setEnabled(true);
    }

    // ADDING GATEWAY
    public /* synthetic */ void lambda$onCreateView$1$AddDeviceFragment(View v) {
        int pos = this.spinnerWifi.getSelectedItemPosition();
        String ssid = null;
        String password = null;
        this.editTextName.setError(null);
        String name = this.editTextName.getText().toString();
        if (name == null || name.length() < 2) {
            this.editTextName.setError("Enter Name");
            return;
        }
        if (pos > 0) {
            ssid = this.wifiList.get(pos);
            password = this.editTextPassword.getText().toString();
        }

        /* try {
            Gateway gateway = DatabaseHandler.getInstance().getGateway(this.selectedDevice.getId());
            if (gateway != null) {
                DatabaseHandler.getInstance().deleteGateway(gateway);
            }
        } catch (Exception e) {
        }*/

        //TODO PERFORM
        performOnBoarding(name, ssid, password);
    }

    private void performOnBoarding(String name, String ssid, String password) {
        String str = TAG;
        Log.d(str, this.selectedDevice.getIp() + " | " + ssid + " | " + password);
        displayProgressBar(false);
        this.selectedDevice.setAuthCode();
        this.selectedDevice.setName(name);
        if (ssid != null) {
            this.selectedDevice.setWifi_ssid(ssid);
            this.selectedDevice.setWifi_password(password);
        }
        startAddTimer();
        if (!this.selectedDevice.getIp().equals("BLE") || !this.selectedDevice.getType().equals("PROD_GATEWAY")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request", "ADD");
                jsonObject.put("username", sessionManager.getUserId());
                String str2 = "";
                jsonObject.put("wifi_ssid", ssid == null ? str2 : ssid);
                if (password != null) {
                    str2 = password;
                }
                jsonObject.put("wifi_password", str2);
                jsonObject.put("security_key", this.selectedDevice.getSecretKey());
                jsonObject.put("authcode", this.selectedDevice.getAuthCode());
                this.localRPICommunication.startOnBoarding(this.selectedDevice.getIp(), jsonObject.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddGateway.this.lambda$performOnBoarding$10$AddDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddGateway.this.lambda$performOnBoarding$11$AddDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                stopAddTimer();
                dismissProgressBar();
                displayErrorDialog("Error", "Failed(6) to add Gateway!\nPlease retry.");
                //MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(6) to add Gateway!\nPlease retry.", null);
            }
        } else {
            this.selectedBleDevice = this.bluetoothDevices.get(this.selectedDevice.getApn());
            String str3 = TAG;
            Log.d(str3, "=================== Connect to device : " + this.selectedBleDevice.getName());
            BLECommHandler.getInstance().registerNotification(this.selectedBleDevice, BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, this);
        }
    }

    public /* synthetic */ void lambda$performOnBoarding$10$AddDeviceFragment(Object object) throws Exception {
        JSONObject responseJson = (JSONObject) object;
        if (responseJson.getInt(NotificationCompat.CATEGORY_STATUS) != 200) {
            stopAddTimer();
            dismissProgressBar();
            displayErrorDialog("Error", "Failed(8) to add Gateway!\nPlease retry.");
            //this.loadingDialog.hide();
            //MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(8) to add Gateway!\nPlease retry.", null);
            return;
        }
        this.selectedDevice.setId(responseJson.getString("gateway_id"));
        this.selectedDevice.setType(responseJson.getString("type"));
        this.selectedDevice.setFw_ver(responseJson.getString("fw_ver"));
        this.selectedDevice.setManufacturer(responseJson.getString("manufacturer"));
        this.selectedDevice.setModel_version(responseJson.getString("model_version"));
        this.selectedDevice.setModel_name(responseJson.getString("model_name"));
        this.selectedDevice.setModel_desc(responseJson.getString("model_desc"));
        this.retryCount = 0;
        startCheckStatusTimer();
    }

    public /* synthetic */ void lambda$performOnBoarding$11$AddDeviceFragment(Object throwable) throws Exception {
        String str = TAG;
        Log.e(str, "Error Onboarding" + throwable);
        stopAddTimer();
        dismissProgressBar();
        displayErrorDialog("Error", "Failed(8) to add Gateway!\nPlease retry.");
       /* this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(7) to add Gateway!\nPlease retry.", null);*/
    }

    private void startCheckStatusTimer() {
        this.retryCount++;
        if (this.retryCount < 15) {
            Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddGateway.this.lambda$startCheckStatusTimer$12$AddDeviceFragment((Long) obj);
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddGateway.this.lambda$startCheckStatusTimer$13$AddDeviceFragment((Throwable) obj);
                }
            });
            return;
        }
        stopAddTimer();
        dismissProgressBar();
        displayErrorDialog("Error", "Failed(5) to add Gateway!\nPlease retry.");
        // MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(5) to add Gateway!\nPlease retry.", null);
    }

    public /* synthetic */ void lambda$startCheckStatusTimer$12$AddDeviceFragment(Long aLong) {
        checkStatus();
    }

    public /* synthetic */ void lambda$startCheckStatusTimer$13$AddDeviceFragment(Throwable throwable) {
        stopAddTimer();
        dismissProgressBar();
    }

    private void checkStatus() {
        this.localRPICommunication.getWifiStatus(this.selectedDevice.getIp()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddGateway.this.lambda$checkStatus$14$AddDeviceFragment(obj);
            }
        }, new Consumer() {

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddGateway.this.lambda$checkStatus$15$AddDeviceFragment(obj);
            }
        });
    }

    public /* synthetic */ void lambda$checkStatus$14$AddDeviceFragment(Object object) {
        try {
            JSONObject responseJson = (JSONObject) object;
            String str = TAG;
            Log.d(str, "Status Received: " + responseJson.toString());
            if (this.selectedDevice.getWifi_ssid() == null || this.selectedDevice.getWifi_ssid().length() == 0) {
                if (responseJson.getInt(NotificationCompat.CATEGORY_STATUS) != 200 || !responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).getBoolean("authcode_status")) {
                    startCheckStatusTimer();
                    return;
                }
            } else if (responseJson.getInt(NotificationCompat.CATEGORY_STATUS) != 200 || responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).getString("wifi") == null || !responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).getBoolean("authcode_status")) {
                startCheckStatusTimer();
                return;
            }
            this.updated_at = Utilities.getEpoch();
            this.selectedDevice.setMac(responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).getString("mac"));
            this.selectedDevice.setLocation(StaticStorage.currentLocation);
            performGatewayAuthotization();
        } catch (Exception e) {
            e.printStackTrace();
            stopAddTimer();
            dismissProgressBar();
            //this.loadingDialog.hide();
        }
    }


    public /* synthetic */ void lambda$checkStatus$15$AddDeviceFragment(Object throwable) {
        stopAddTimer();
        dismissProgressBar();
        displayErrorDialog("Error", "Failed(4) to add Gateway!\nPlease retry.");
        /* this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(4) to add Gateway!\nPlease retry.", null);*/
    }

    private void startAddTimer() {
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
        }
        this.isAdding = true;
        this.timerSubscription = Observable.timer(90, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddGateway.this.lambda$startAddTimer$8$AddDeviceFragment((Long) obj);
            }
        });
    }

    public /* synthetic */ void lambda$startAddTimer$8$AddDeviceFragment(Long aLong) {
        this.isAdding = false;
    }

    private void stopAddTimer() {
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
        }
        this.isAdding = false;
        getActivity().runOnUiThread(new Runnable() {


            public final void run() {
                AddGateway.this.lambda$stopAddTimer$9$AddDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$stopAddTimer$9$AddDeviceFragment() {
        dismissProgressBar();
        //this.loadingDialog.hide();
    }

    private void performGatewayAuthotization() {
        String Lat = null;
        String Long = null;
        String WIFISSID = null;

        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("identity_id", AWSMobileClient.getInstance().getIdentityId());
            jsonObject.put("productId", "12");
            jsonObject.put("gateway_id", this.selectedDevice.getId());
            jsonObject.put("gateway_authcode", this.selectedDevice.getAuthCode());
            if (this.selectedDevice.getLocation() != null) {
                Lat = String.valueOf(this.selectedDevice.getLocation().getLatitude());
                Long = String.valueOf(this.selectedDevice.getLocation().getLongitude());
                jsonObject.put("gateway_lati", this.selectedDevice.getLocation().getLatitude());
                jsonObject.put("gateway_longi", this.selectedDevice.getLocation().getLongitude());
            } else {
                Lat = String.valueOf(9999);
                Long = String.valueOf(9999);
                jsonObject.put("gateway_lati", 9999);
                jsonObject.put("gateway_longi", 9999);
            }
            jsonObject.put("gateway_name", this.selectedDevice.getName());
            jsonObject.put("gateway_mac", this.selectedDevice.getMac());
            jsonObject.put("gateway_apn", this.selectedDevice.getApn());
            jsonObject.put("gateway_ver", this.selectedDevice.getVer());
            if (this.selectedDevice.getWifi_ssid() == null || this.selectedDevice.getWifi_ssid().length() == 0) {
                jsonObject.put("gateway_wifi_ssid", " ");
                WIFISSID = "";
            } else {
                WIFISSID = this.selectedDevice.getWifi_ssid();
                jsonObject.put("gateway_wifi_ssid", this.selectedDevice.getWifi_ssid());
            }
            jsonObject.put("gateway_secret_key", this.selectedDevice.getSecretKey());
            jsonObject.put("gateway_type", this.selectedDevice.getType());
            jsonObject.put("gateway_fw_ver", this.selectedDevice.getFw_ver());
            jsonObject.put("gateway_manufacturer", this.selectedDevice.getManufacturer());
            jsonObject.put("gateway_model_version", this.selectedDevice.getModel_version());
            jsonObject.put("gateway_model_name", this.selectedDevice.getModel_name());
            jsonObject.put("gateway_model_desc", this.selectedDevice.getModel_desc());
            jsonObject.put("gateway_timezone", this.selectedDevice.getTimezone());
            jsonObject.put("gateway_feature_data", this.selectedDevice.getFeatureData());
            jsonObject.put("gateway_status_data", this.selectedDevice.getStatusData());
            jsonObject.put("updated_at", this.updated_at);
            // jsonObject.put("status_code", "200");
            /*CloudHandler.getInstance().addGateway(jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        AddDeviceFragment.this.lambda$performGatewayAuthotization$18$AddDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        AddDeviceFragment.this.lambda$performGatewayAuthotization$19$AddDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/

            if (ConnectionDetector.isNetAvail(getContext())) {
                restClient.callback(this).ADD_GATEWAY(sessionManager.getUserId(), this.selectedDevice.getId(), this.selectedDevice.getAuthCode(), Lat, Long, this.selectedDevice.getName(), this.selectedDevice.getMac(),
                        this.selectedDevice.getApn(), WIFISSID, this.selectedDevice.getSecretKey(), this.selectedDevice.getType(), this.selectedDevice.getFw_ver(), this.selectedDevice.getManufacturer(),
                        this.selectedDevice.getModel_version(), this.selectedDevice.getModel_name(), this.selectedDevice.getIp(), this.selectedDevice.getModel_desc(), this.selectedDevice.getTimezone());
            } else {
                showToast("No Internet Connection");

            }


        } catch (Exception e) {
            e.printStackTrace();
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            displayErrorDialog("Error", "Error(3) adding Gateway.\nPlease check internet connection and retry.");
            //MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(3) adding Gateway.\nPlease check internet connection and retry.", null);
        }
    }

    @Override
    public void onNotification(int i, byte[] bArr) {

    }


    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_ADD_GATEWAY) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    CommonPojo login_pojo = gson.fromJson(s, CommonPojo.class);
                    if (login_pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "" + login_pojo.getStatus());
                            storeDeviceDatabase();
                            // setFragments(R.id.frameLayout, new HomeFragment(), true);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        displayErrorDialog("Error", login_pojo.getStatus());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    displayErrorDialog("Error", e.getMessage());

                }
            } else {
                dismissProgressBar();
                displayErrorDialog("Error", response.message());
            }
        }

    }

    @Override
    public void onFailResponse(int apiId, String error) {
        dismissProgressBar();
        displayErrorDialog("Error", error);
    }

    private void storeDeviceDatabase() {
        Gateway gateway = new Gateway();
        gateway.setGateway_id(this.selectedDevice.getId());
        gateway.setName(this.selectedDevice.getName());
        gateway.setApn(this.selectedDevice.getApn());
        gateway.setMac(this.selectedDevice.getMac());
        gateway.setUpdated_at(this.updated_at);
        gateway.setType(this.selectedDevice.getType());
        if (this.selectedDevice.getLocation() != null) {
            gateway.setLatitude(this.selectedDevice.getLocation().getLatitude());
            gateway.setLongitude(this.selectedDevice.getLocation().getLongitude());
        } else {
            gateway.setLatitude(9999.0d);
            gateway.setLongitude(9999.0d);
        }
        if (this.selectedDevice.getWifi_ssid() == null || this.selectedDevice.getWifi_ssid().length() == 0) {
            gateway.setWifi_ssid(" ");
        } else {
            gateway.setWifi_ssid(this.selectedDevice.getWifi_ssid());
        }
        gateway.setTimezone(this.selectedDevice.getTimezone());
        gateway.setModel_version(this.selectedDevice.getModel_version());
        gateway.setFw_ver(this.selectedDevice.getFw_ver());
        gateway.setModel_desc(this.selectedDevice.getModel_desc());
        gateway.setModel_name(this.selectedDevice.getModel_name());
        gateway.setManufacturer(this.selectedDevice.getManufacturer());
        gateway.setFeature_data(this.selectedDevice.getFeatureData());
        gateway.setStatus_data(this.selectedDevice.getStatusData());
        gateway.setIs_deleted(false);
        gateway.setSecret_key(this.selectedDevice.getSecretKey());
        DatabaseHandler.getInstance().addGateway(gateway);
        if (this.selectedDevice.getIp().equals("BLE")) {
            completeBleAdditionProcess();
        } else {
            completeOnBoarding();
        }
    }

    private void completeOnBoarding() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("request", "DONE");
            this.localRPICommunication.startOnBoarding(this.selectedDevice.getIp(), jsonObject.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        AddGateway.this.lambda$completeOnBoarding$16$AddDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddGateway.this.lambda$completeOnBoarding$17$AddDeviceFragment(obj);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            stopAddTimer();
            dismissProgressBar();
            displayErrorDialog("Error", "Failed(3) to add Gateway!\nPlease retry.");
            //this.loadingDialog.hide();
            //MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(3) to add Gateway!\nPlease retry.", null);
        }
    }

    public /* synthetic */ void lambda$completeOnBoarding$16$AddDeviceFragment(Object o) throws Exception {
        if (((JSONObject) o).getInt(NotificationCompat.CATEGORY_STATUS) != 200) {
            stopAddTimer();
            dismissProgressBar();
            displayErrorDialog("Error", "Failed(1) to add Gateway!\nPlease retry.");
            // this.loadingDialog.hide();
            //MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(1) to add Gateway!\nPlease retry.", null);
            return;
        }
        stopAddTimer();
        dismissProgressBar();
        setFragments(R.id.frameLayout, new HomeFragment(), true);
        //this.loadingDialog.hide();
        //redirectToHome();
    }

    public /* synthetic */ void lambda$completeOnBoarding$17$AddDeviceFragment(Object throwable) {
        String str = TAG;
        Log.e(str, "Error Onboarding" + throwable);
        stopAddTimer();
       /* this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(2) to add Gateway!\nPlease retry.", null);*/
        dismissProgressBar();
        displayErrorDialog("Error", "Failed(2) to add Gateway!\nPlease retry.");
    }

    private void completeBleAdditionProcess() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(NotificationCompat.CATEGORY_STATUS, 1);
            BLECommHandler.getInstance().sendRawDataOverBLE(this.selectedBleDevice,
                    BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, BLECommHandler.getInstance()
                            .encryptBLEData(this.bleDeviceAESKey, jsonObject, (byte) 28), new OperationResultCallback() {


                        public final void onOperationResult(int i, String str) {
                            AddGateway.this.lambda$completeBleAdditionProcess$27$AddDeviceFragment(i, str);
                        }
                    });
        } catch (Exception e) {
        }
    }

    public /* synthetic */ void lambda$completeBleAdditionProcess$27$AddDeviceFragment(int errorcode1, String data1) {
        if (errorcode1 == 0) {
            Log.i(TAG, "Data Successfully Send Over Ble");
            getActivity().runOnUiThread(new Runnable() {


                public final void run() {
                    AddGateway.this.lambda$null$25$AddDeviceFragment();
                }
            });
            return;
        }
        Log.e(TAG, "Error sending auth check request");
        getActivity().runOnUiThread(new Runnable() {


            public final void run() {
                AddGateway.this.lambda$null$26$AddDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$null$25$AddDeviceFragment() {
        stopAddTimer();

        /*this.loadingDialog.hide();
        redirectToHome();*/
        dismissProgressBar();
        setFragments(R.id.frameLayout, new HomeFragment(), true);
    }

    public /* synthetic */ void lambda$null$26$AddDeviceFragment() {
        stopAddTimer();
        setFragments(R.id.frameLayout, new HomeFragment(), true);

        //this.loadingDialog.hide();
    }
    public void onResume() {
        super.onResume();
      //  smartapp.getEventBus().register(this);
        getActivity().registerReceiver(this.wifiScanReceiver, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        if (this.newDeviceDataMap.size() > 0) {
            //this.progressBarScanning.setVisibility(View.GONE);
        } else {
            //this.progressBarScanning.setVisibility(View.VISIBLE);
        }
        MDNSQueryHandler.getInstance().stopDiscovery();
        MDNSQueryHandler.getInstance().startDiscovery();
        getActivity().getWindow().addFlags(128);
        this.isAdding = false;
        startScanning();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
       //smartapp.getEventBus().unregister(this);
        getActivity().unregisterReceiver(this.wifiScanReceiver);
        getActivity().getWindow().clearFlags(128);
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
        }
        Disposable disposable2 = this.scanSubscription;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.scanSubscription.dispose();
        }
        BLECommHandler.getInstance().disconnectBle();
       dismissProgressBar();
        // this.loadingDialog.hide();
        super.onPause();
    }

}