package com.sravan.efactorapp.UI.Fragments.Gateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.sravan.efactorapp.Adapter.NewDeviceAdapter;
import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.BLE.BLECommHandler;
import com.sravan.efactorapp.BLE.BLEConstants;
import com.sravan.efactorapp.BLE.NotificationCallback;
import com.sravan.efactorapp.BLE.OperationResultCallback;
import com.sravan.efactorapp.Event.EventType;
import com.sravan.efactorapp.Event.Message;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.NewDeviceData;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.ConnectionDetector;
import com.sravan.efactorapp.utils.Crypto.CryptoCRC;
import com.sravan.efactorapp.utils.Crypto.CryptoDH;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;
import com.sravan.efactorapp.utils.DataBase.Model.GatewayDao;
import com.sravan.efactorapp.utils.FragmentRequestType;
import com.sravan.efactorapp.utils.LoadingDialog;
import com.sravan.efactorapp.utils.LocalRPICommunication;
import com.sravan.efactorapp.utils.MDNSQueryHandler;
import com.sravan.efactorapp.utils.OnFragmentInteractionListener;
import com.sravan.efactorapp.utils.StaticStorage;
import com.sravan.efactorapp.utils.Utilities;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.interfaces.DHPublicKey;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import kotlin.UByte;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDeviceFragment extends Fragment implements NewDeviceAdapter.OnNewDeviceClickListener, NotificationCallback, ApiHitListener {

    private static final String ARG_PARAM = "ADD_TYPE";
    private static final String TAG = AddDeviceFragment.class.getSimpleName();
    private final int MAX_STATUS_CHECK_RETRY = 5;
    private final int SCAN_TIMEOUT_SEC = 5;
    private String bleDeviceAESKey = null;
    private HashMap<String, RxBleDevice> bluetoothDevices;
    private Button buttonAddDevice;
    private Button buttonScan;
    private SessionManager sessionManager;
    private CryptoDH cryptoDH;
    private EditText editTextName;
    private TextInputEditText editTextPassword;
    private String gatewayId = null;
    private ImageView imageViewRefresh;
    private boolean isAdding = false;
    private LoadingDialog loadingDialog;
    private LocalRPICommunication localRPICommunication;
    private OnFragmentInteractionListener mListener;
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
    private RestClient restClient;
    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {


        public void onReceive(Context context, Intent intent) {
            List<android.net.wifi.ScanResult> results = AddDeviceFragment.this.wifiManager.getScanResults();
            AddDeviceFragment.this.wifiList.clear();
            AddDeviceFragment.this.wifiList.add("Select WiFi Network");
            for (android.net.wifi.ScanResult result : results) {
                if (result.SSID != null && result.SSID.length() > 0 && result.frequency <= 2500) {
                    AddDeviceFragment.this.wifiList.add(result.SSID);
                }
            }
            if (!AddDeviceFragment.this.isAdding) {
                AddDeviceFragment.this.getActivity().runOnUiThread(new Runnable() {


                    public final void run() {
                        AddDeviceFragment.this.lambda$onReceive$0$AddDeviceFragment$2();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onReceive$0$AddDeviceFragment$2() {
            AddDeviceFragment.this.loadingDialog.hide();
            AddDeviceFragment.this.wifiSpinnerAdapter.notifyDataSetChanged();
        }
    };

    private void lambda$onReceive$0$AddDeviceFragment$2() {
        AddDeviceFragment.this.loadingDialog.hide();
        AddDeviceFragment.this.wifiSpinnerAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> wifiSpinnerAdapter;

    public static AddDeviceFragment newInstance(String param1) {
        AddDeviceFragment fragment = new AddDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.selectedType = getArguments().getString(ARG_PARAM);
            String str = TAG;
            Log.d(str, "ADD_TYPE : " + this.selectedType);
        }
        this.localRPICommunication = new LocalRPICommunication();
        this.newDeviceDataMap = new HashMap<>();
        this.restClient = new RestClient(getContext());
        this.newDeviceAdapter = new NewDeviceAdapter(this);
        this.wifiList = new ArrayList();
        this.wifiList.add("Select WiFi Network");
        this.wifiSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.select_dialog_item_material, this.wifiList);
        this.wifiSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
        this.bluetoothDevices = new HashMap<>();
        this.cryptoDH = new CryptoDH(1024);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        this.rootView = inflater.inflate(R.layout.fragment_add_gateway, container, false);
        this.recyclerViewItemList = (RecyclerView) this.rootView.findViewById(R.id.recyclerViewDevices);
        this.recyclerViewItemList.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        this.recyclerViewItemList.setAdapter(this.newDeviceAdapter);
        this.recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.progressBarScanning = (ProgressBar) this.rootView.findViewById(R.id.progressBarScanning);
        this.spinnerWifi = (Spinner) this.rootView.findViewById(R.id.spinnerWifi);
        this.spinnerWifi.setAdapter((SpinnerAdapter) this.wifiSpinnerAdapter);
        this.spinnerWifi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                AddDeviceFragment.this.editTextPassword.setText((CharSequence) null);
                if (position == 0) {
                    AddDeviceFragment.this.editTextPassword.setEnabled(false);
                } else {
                    AddDeviceFragment.this.editTextPassword.setEnabled(true);
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.editTextPassword = (TextInputEditText) this.rootView.findViewById(R.id.editTextWifiPass);
        this.editTextPassword.setEnabled(false);
        this.imageViewRefresh = (ImageView) this.rootView.findViewById(R.id.imageViewRefresh);
        this.imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddDeviceFragment.this.lambda$onCreateView$0$AddDeviceFragment(view);
            }
        });
        this.editTextName = (EditText) this.rootView.findViewById(R.id.editTextName);
        this.editTextName.setText("");
        this.wifiDetailsLayout = (ConstraintLayout) this.rootView.findViewById(R.id.wifiDetailsView);
        this.buttonAddDevice = (Button) this.rootView.findViewById(R.id.buttonSubmit);
        this.buttonAddDevice.setEnabled(false);
        this.buttonAddDevice.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                AddDeviceFragment.this.lambda$onCreateView$1$AddDeviceFragment(view);
            }
        });
        this.editTextName.setVisibility(View.GONE);
        this.wifiDetailsLayout.setVisibility(View.GONE);
        this.buttonAddDevice.setVisibility(View.GONE);
        this.loadingDialog = new LoadingDialog(getActivity());
        /*if (this.selectedType.equals(GatewayDao.TABLENAME)) {
            ((TextView) this.rootView.findViewById(R.id.textView3)).setText(String.format("Select the %s from below to add", "Gateway"));
            ((TextView) this.rootView.findViewById(R.id.textView4)).setText(String.format("Select WiFi network to connect the %s", "Gateway"));
            this.editTextName.setHint("Enter Gateway Name");
            this.buttonAddDevice.setText("Add Gateway");
        } else {
            ((TextView) this.rootView.findViewById(R.id.textView3)).setText(String.format("Select the %s from below to add", "Device"));
            ((TextView) this.rootView.findViewById(R.id.textView4)).setText(String.format("Select WiFi network to connect the %s", "Device"));
            this.editTextName.setHint("Enter Device Name");
            this.buttonAddDevice.setText("Add Device");
        }*/
        this.buttonScan = (Button) this.rootView.findViewById(R.id.buttonScan);
        this.buttonScan.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                AddDeviceFragment.this.lambda$onCreateView$2$AddDeviceFragment(view);
            }
        });
        return this.rootView;
    }

    public /* synthetic */ void lambda$onCreateView$0$AddDeviceFragment(View v) {
        if (!this.wifiManager.startScan()) {
            Log.e(TAG, "Failed to start scan");
        } else {
            this.loadingDialog.show();
        }
    }

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
        try {
            Gateway gateway = DatabaseHandler.getInstance().getGateway(this.selectedDevice.getId());
            if (gateway != null) {
                DatabaseHandler.getInstance().deleteGateway(gateway);
            }
        } catch (Exception e) {
        }
        performOnBoarding(name, ssid, password);
    }

    public /* synthetic */ void lambda$onCreateView$2$AddDeviceFragment(View v) {
        startScanning();
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        App.getEventBus().register(this);
        getActivity().registerReceiver(this.wifiScanReceiver, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        if (this.newDeviceDataMap.size() > 0) {
            this.progressBarScanning.setVisibility(View.GONE);
        } else {
            this.progressBarScanning.setVisibility(View.VISIBLE);
        }
        MDNSQueryHandler.getInstance().stopDiscovery();
        MDNSQueryHandler.getInstance().startDiscovery();
        getActivity().getWindow().addFlags(128);
        this.isAdding = false;
        startScanning();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        App.getEventBus().unregister(this);
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
        this.loadingDialog.hide();
        super.onPause();

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
                AddDeviceFragment.this.lambda$onNewDeviceItemClick$3$AddDeviceFragment();
            }
        });
        if (this.selectedType.equals(GatewayDao.TABLENAME) && !this.wifiManager.startScan()) {
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
        this.loadingDialog.show();
        if (this.selectedType.equals(GatewayDao.TABLENAME)) {
            this.editTextName.setVisibility(View.VISIBLE);
            this.wifiDetailsLayout.setVisibility(View.VISIBLE);
            this.buttonAddDevice.setVisibility(View.VISIBLE);
        } else {
            this.editTextName.setVisibility(View.VISIBLE);
            this.buttonAddDevice.setVisibility(View.VISIBLE);
        }
        this.buttonAddDevice.setEnabled(true);
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
                AddDeviceFragment.this.lambda$startScanTimer$4$AddDeviceFragment((Long) obj);
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

    private void startScanning() {
        Disposable disposable = this.scanSubscription;
        if (disposable == null || disposable.isDisposed()) {
            startScanTimer();
            //Toast.makeText(getContext(), "" + App.getBleClient(), Toast.LENGTH_SHORT).show();
            this.scanSubscription = App.getBleClient().
                    scanBleDevices(new ScanSettings.Builder().build(), new ScanFilter.Builder().setManufacturerData(17989, new byte[]{65, 67, 84, 79}).build())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate() {

                        @Override // io.reactivex.functions.Predicate
                        public final boolean test(Object obj) {
                            return AddDeviceFragment.this.lambda$startScanning$5$AddDeviceFragment((ScanResult) obj);
                        }
                    }).subscribe(new Consumer() {

                        @Override // io.reactivex.functions.Consumer
                        public final void accept(Object obj) {
                            AddDeviceFragment.this.lambda$startScanning$6$AddDeviceFragment((ScanResult) obj);
                        }
                    }, $$Lambda$AddDeviceFragment$W6UW3Y9n7L5DPH5bpHChfUdNrwE.INSTANCE);
        }
    }

    public /* synthetic */ boolean lambda$startScanning$5$AddDeviceFragment(ScanResult scanResult) {
        RxBleDevice device = scanResult.getBleDevice();
        if (device.getName() == null || this.bluetoothDevices.containsKey(device.getName())) {
            return false;
        }
        this.bluetoothDevices.put(device.getName(), device);
        return true;
    }

    public /* synthetic */ void lambda$startScanning$6$AddDeviceFragment(ScanResult scanResult) {
        String str = TAG;
        Log.i(str, "ScanResult Received: " + scanResult.getBleDevice().getName());
        NewDeviceData newDeviceData = new NewDeviceData(scanResult.getBleDevice().getName(), "v1.0");
        newDeviceData.setType("PROD_GATEWAY");
        newDeviceData.setIp("BLE");
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

    private void startAddTimer() {
        Disposable disposable = this.timerSubscription;
        if (disposable != null && !disposable.isDisposed()) {
            this.timerSubscription.dispose();
        }
        this.isAdding = true;
        this.timerSubscription = Observable.timer(90, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddDeviceFragment.this.lambda$startAddTimer$8$AddDeviceFragment((Long) obj);
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
                AddDeviceFragment.this.lambda$stopAddTimer$9$AddDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$stopAddTimer$9$AddDeviceFragment() {
        this.loadingDialog.hide();
    }

    private void performOnBoarding(String name, String ssid, String password) {
        String str = TAG;
        Log.d(str, this.selectedDevice.getIp() + " | " + ssid + " | " + password);
        this.loadingDialog.show();
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
                jsonObject.put("username", "11");
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
                            AddDeviceFragment.this.lambda$performOnBoarding$10$AddDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddDeviceFragment.this.lambda$performOnBoarding$11$AddDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                stopAddTimer();
                this.loadingDialog.hide();
                MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(6) to add Gateway!\nPlease retry.", null);
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
            this.loadingDialog.hide();
            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(8) to add Gateway!\nPlease retry.", null);
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
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(7) to add Gateway!\nPlease retry.", null);
    }

    private void startCheckStatusTimer() {
        this.retryCount++;
        if (this.retryCount < 15) {
            Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddDeviceFragment.this.lambda$startCheckStatusTimer$12$AddDeviceFragment((Long) obj);
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddDeviceFragment.this.lambda$startCheckStatusTimer$13$AddDeviceFragment((Throwable) obj);
                }
            });
            return;
        }
        stopAddTimer();
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(5) to add Gateway!\nPlease retry.", null);
    }

    public /* synthetic */ void lambda$startCheckStatusTimer$12$AddDeviceFragment(Long aLong) {
        checkStatus();
    }

    public /* synthetic */ void lambda$startCheckStatusTimer$13$AddDeviceFragment(Throwable throwable) {
        stopAddTimer();
        this.loadingDialog.hide();
    }

    private void checkStatus() {
        this.localRPICommunication.getWifiStatus(this.selectedDevice.getIp()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddDeviceFragment.this.lambda$checkStatus$14$AddDeviceFragment(obj);
            }
        }, new Consumer() {

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                AddDeviceFragment.this.lambda$checkStatus$15$AddDeviceFragment(obj);
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
            this.loadingDialog.hide();
        }
    }

    public /* synthetic */ void lambda$checkStatus$15$AddDeviceFragment(Object throwable) {
        stopAddTimer();
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(4) to add Gateway!\nPlease retry.", null);
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
                        AddDeviceFragment.this.lambda$completeOnBoarding$16$AddDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    AddDeviceFragment.this.lambda$completeOnBoarding$17$AddDeviceFragment(obj);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            stopAddTimer();
            this.loadingDialog.hide();
            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(3) to add Gateway!\nPlease retry.", null);
        }
    }

    public /* synthetic */ void lambda$completeOnBoarding$16$AddDeviceFragment(Object o) throws Exception {
        if (((JSONObject) o).getInt(NotificationCompat.CATEGORY_STATUS) != 200) {
            stopAddTimer();
            this.loadingDialog.hide();
            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(1) to add Gateway!\nPlease retry.", null);
            return;
        }
        stopAddTimer();
        this.loadingDialog.hide();
        redirectToHome();
    }

    public /* synthetic */ void lambda$completeOnBoarding$17$AddDeviceFragment(Object throwable) {
        String str = TAG;
        Log.e(str, "Error Onboarding" + throwable);
        stopAddTimer();
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Failed(2) to add Gateway!\nPlease retry.", null);
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
            //TODO PUSH TO Cloud

            /* CloudHandler.getInstance().addGateway(jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

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
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(3) adding Gateway.\nPlease check internet connection and retry.", null);
        }
    }

    public /* synthetic */ void lambda$performGatewayAuthotization$18$AddDeviceFragment(Object object) throws Exception {
        JSONObject responseJson = (JSONObject) object;
        String str = TAG;
        Log.d(str, "Data Received: " + responseJson);
        if (responseJson.getInt("statusCode") == 200) {
            storeDeviceDatabase();
            return;
        }
        stopAddTimer();
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(1) adding Gateway.\nPlease check internet connection and retry.", null);
    }

    public /* synthetic */ void lambda$performGatewayAuthotization$19$AddDeviceFragment(Object throwable) throws Exception {
        stopAddTimer();
        this.loadingDialog.hide();
        String str = TAG;
        Log.e(str, "performGatewayAuthotization Exception: " + throwable);
        FragmentActivity activity = getActivity();
        Toast.makeText(activity, "Exception: " + throwable, Toast.LENGTH_LONG).show();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(2) adding Gateway.\nPlease check internet connection and retry.", null);
    }

    private void redirectToHome() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM, this.selectedType);
        bundle.putString("ID", this.selectedDevice.getId());
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new HomeFragment(), "NewFragmentTag");
        ft.commit();
        this.mListener.onFragmentInteraction(FragmentRequestType.FRAGMENT_ADD_THING_COMPLETE, bundle);
        Log.d(TAG, "Redirected to home");
    }

    @Override
    public void onNotification(int errorcode, byte[] data) {
        if (errorcode == 0) {
            String strData = new String(data);
            String str = TAG;
            Log.e(str, "Notification Data: " + strData);
            if (strData.equals("DONE")) {
                handshakeAdditionProcess();
            } else if (strData.equals("ALLDONE")) {
                Log.d(TAG, "Check for network status");
            } else if (!strData.startsWith("ST:")) {
            } else {
                if (this.selectedDevice.getWifi_ssid() == null || this.selectedDevice.getWifi_ssid().length() == 0) {
                    if (strData.equals("ST:0:1:1:1")) {
                        getActivity().runOnUiThread(new Runnable() {

                            public final void run() {
                                AddDeviceFragment.this.lambda$onNotification$20$AddDeviceFragment();
                            }
                        });
                    }
                } else if (strData.equals("ST:1:1:1:1")) {
                    getActivity().runOnUiThread(new Runnable() {


                        public final void run() {
                            AddDeviceFragment.this.lambda$onNotification$21$AddDeviceFragment();
                        }
                    });
                }
            }
        } else if (errorcode == -1) {
            Log.i(TAG, "Notification Setup Successful");
            startAddition();
        } else {
            Log.e(TAG, "Notification Setup Error");
            stopAddTimer();
        }
    }

    public /* synthetic */ void lambda$onNotification$20$AddDeviceFragment() {
        this.updated_at = Utilities.getEpoch();
        this.selectedDevice.setLocation(StaticStorage.currentLocation);
        performGatewayAuthotization();
    }

    public /* synthetic */ void lambda$onNotification$21$AddDeviceFragment() {
        this.updated_at = Utilities.getEpoch();
        this.selectedDevice.setLocation(StaticStorage.currentLocation);
        performGatewayAuthotization();
    }

    private void startAddition() {

        BLECommHandler.getInstance().sendRawDataOverBLE(this.selectedBleDevice, BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, generateInitData(), $$Lambda$AddDeviceFragment$Rmx9tGnHkDXBgIb0YWspCcqA5Wc.INSTANCE);
    }

    public static /* synthetic */ void lambda$startAddition$22(int errorcode, String data) {
        if (errorcode == 0) {
            Log.i(TAG, "Data Successfully Send Over Ble startAddition");
            return;
        }
        String str = TAG;
        Log.e(str, "Error Sending Data over Ble: " + data);
    }

    private void handshakeAdditionProcess() {
        BLECommHandler.getInstance().readFromBle(this.selectedBleDevice, BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, new OperationResultCallback() {


            @Override
            public final void onOperationResult(int i, String str) {
                AddDeviceFragment.this.lambda$handshakeAdditionProcess$24$AddDeviceFragment(i, str);
            }
        });
    }

    public /* synthetic */ void lambda$handshakeAdditionProcess$24$AddDeviceFragment(int errorcode, String data) {
        if (errorcode == 0) {
            Log.d(TAG, "Data " + BLEConstants.BLE_SERVICE_ATTR_CONN_UUID + "\n" + validateReceviedData(Base64.decode(data, 0)));
            BLECommHandler.getInstance().sendRawDataOverBLE(this.selectedBleDevice, BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, validateReceviedData(Base64.decode(data, 0)), new OperationResultCallback() {


                @Override
                public final void onOperationResult(int i, String str) {
                    AddDeviceFragment.this.lambda$null$23$AddDeviceFragment(i, str);
                }
            });
            return;
        }
        String str = TAG;
        Log.e(str, "Error Sending Data over Ble: " + data);
        stopAddTimer();
    }

    public /* synthetic */ void lambda$null$23$AddDeviceFragment(int errorcode1, String data1) {
        if (errorcode1 == 0) {
            Log.i(TAG, "Data Successfully Send Over Ble handshakeAdditionProcess");
            return;
        }
        String str = TAG;

        stopAddTimer();
    }

    /* JADX INFO: Multiple debug info for r2v5 int: [D('i' int), D('j' int)] */
    /* JADX INFO: Multiple debug info for r1v9 int: [D('i' int), D('j' int)] */
    private byte[] generateInitData() {
        byte[] keyPArray = Utilities.hexIntStringToBytes(this.cryptoDH.getP().toString(16));
        byte[] keyGArray = Utilities.hexIntStringToBytes(this.cryptoDH.getG().toString(16));
        DHPublicKey localDHPublicKey = this.cryptoDH.getPublicKey();
        Log.d(TAG, "generateInitData: " + localDHPublicKey);
        if (localDHPublicKey == null) {
            return null;
        }
        String str = localDHPublicKey.getY().toString(16);
        while (str.length() < 256) {
            str = "0" + str;
        }
        byte[] keyPublicArray = Utilities.hexIntStringToBytes(str);
        int totalLength = keyPArray.length + keyGArray.length + keyPublicArray.length + 6 + 1;
        byte glen1 = (byte) ((keyGArray.length >> 8) & 255);
        byte glen2 = (byte) (keyGArray.length & 255);
        byte publen1 = (byte) ((keyPublicArray.length >> 8) & 255);
        byte publen2 = (byte) (keyPublicArray.length & 255);
        byte[] keySendData = new byte[(totalLength + 4)];
        int i = 0 + 1;
        keySendData[0] = (byte) ((totalLength >> 8) & 255);
        int i2 = i + 1;
        keySendData[i] = (byte) (totalLength & 255);
        int i3 = i2 + 1;
        keySendData[i2] = (byte) ((keyPArray.length >> 8) & 255);
        int i4 = i3 + 1;
        keySendData[i3] = (byte) (keyPArray.length & 255);
        int j = 0;
        while (j < keyPArray.length) {
            keySendData[i4] = keyPArray[j];
            j++;
            i4++;
            localDHPublicKey = localDHPublicKey;
        }
        int j2 = i4 + 1;
        keySendData[i4] = glen1;
        int i5 = j2 + 1;
        keySendData[j2] = glen2;
        int j3 = 0;
        while (j3 < keyGArray.length) {
            keySendData[i5] = keyGArray[j3];
            j3++;
            i5++;
            keyPArray = keyPArray;
        }
        int i6 = i5 + 1;
        keySendData[i5] = publen1;
        int i7 = i6 + 1;
        keySendData[i6] = publen2;
        int j4 = 0;
        while (j4 < keyPublicArray.length) {
            keySendData[i7] = keyPublicArray[j4];
            j4++;
            i7++;
        }
        int j5 = i7 + 1;
        keySendData[i7] = -95;
        int checksum = CryptoCRC.caluCRC(0, keySendData, keySendData.length - 2);
        int i8 = j5 + 1;
        keySendData[j5] = (byte) (checksum & 255);
        int i9 = i8 + 1;
        keySendData[i8] = (byte) ((checksum >> 8) & 255);
        Log.e(str, "keySendData: " + keySendData);
        return keySendData;
    }

    /* JADX INFO: Multiple debug info for r9v2 byte: [D('i' int), D('thingIDLen' int)] */
    private byte[] validateReceviedData(byte[] receivedData) {
        try {
            Log.d(TAG, "Received Data " + receivedData);
            int totalLen = receivedData.length;
            Log.d(TAG, "totalLen " + totalLen);
            // int checksum = CryptoCRC.caluCRC(0, receivedData, totalLen - 2) & 65535;
            int checksum_pkt = ((receivedData[totalLen - 1] & UByte.MAX_VALUE) << 8) | (receivedData[totalLen - 2] & UByte.MAX_VALUE);
           /* if (checksum != checksum_pkt) {
                Log.e(TAG, "Error in checksum calculation: Received: " + checksum_pkt + " Actual: " + checksum);
                //    return null;
            }*/
            byte[] aesIV = new byte[16];
            int i = 0;
            int j = 0;
            while (j < 16) {
                aesIV[j] = receivedData[i];
                j++;
                i++;
            }
            int i2 = i + 1;
            byte b = receivedData[i];
            byte[] encrThingID = new byte[b];
            int j2 = 0;
            while (j2 < b) {
                encrThingID[j2] = receivedData[i2];
                j2++;
                i2++;
            }
            int pubKeyLen = (totalLen - i2) - 2;
            byte[] pubKeyByteArr = new byte[pubKeyLen];
            int j3 = 0;
            while (j3 < pubKeyLen) {
                pubKeyByteArr[j3] = receivedData[i2];
                j3++;
                i2++;
            }
           /* this.cryptoDH.generateSecretKey(new BigInteger(Utilities.bytesToString(pubKeyByteArr), 16));
            if (this.cryptoDH.getSecretKey() == null) {
                Log.e(TAG, "Error generating secrect key");
                //   return null;
            }
            byte[] secretKey = CryptoMD5.getMD5Byte(this.cryptoDH.getSecretKey());*/
            Log.d(TAG, "encrThingID: " + encrThingID);
            String newDeviceData = new String(encrThingID);

            //   String newDeviceData = new String(new CryptoAES(secretKey, aesIV).decrypt(encrThingID));
          /*  Log.d(TAG, "BLE Key: " + Utilities.byteArrayToHexString(secretKey));
            this.bleDeviceAESKey = Base64.encodeToString(secretKey, 0);*/
            Log.d(TAG, "Decrypted Received Data " + newDeviceData);
            String[] _tarr = newDeviceData.split(":");
            if (_tarr.length < 3) {
                Log.e(TAG, "Incorrect Data Received from Device");
                // return null;
            }
            this.gatewayId = _tarr[1];
            this.selectedDevice.setType(_tarr[0]);
            this.selectedDevice.setId(this.gatewayId);
            this.selectedDevice.setFw_ver(_tarr[2]);
            this.selectedDevice.setModel_name(_tarr[3]);
            this.selectedDevice.setModel_version(_tarr[4]);
            this.selectedDevice.setMac(_tarr[5]);
            this.selectedDevice.setModel_desc("Efactor Prod Gateway");
            this.selectedDevice.setManufacturer("EFactor");
            this.selectedDevice.setSecretKey("");
            SecureRandom random = new SecureRandom();
            random.nextBytes(aesIV);
            //CryptoAES cryptoAES = new CryptoAES(secretKey, aesIV);
            random.nextInt(10);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "0d6ef01d-3057-400f-9ee2-92335b0c4f16");
            //jsonObject.put(CognitoUserPoolsSignInProvider.AttributeKeys.USERNAME, StaticStorage.currentUser.getSub_uuid());
            jsonObject.put("authcode", /*this.selectedDevice.getAuthCode()*/"308963d534fea1ad98307e08dba83275c8ac6007");
            if (this.selectedDevice.getWifi_ssid() == null || this.selectedDevice.getWifi_ssid().length() <= 0) {
                jsonObject.put("sta_ssid", (Object) null);
                jsonObject.put("sta_pass", (Object) null);
            } else {
                jsonObject.put("sta_ssid", this.selectedDevice.getWifi_ssid());
                jsonObject.put("sta_pass", this.selectedDevice.getWifi_password());
            }
            Log.d(TAG, "JSON Sent: " + jsonObject.toString());
            //  byte[] encryptResponse = cryptoAES.encrypt(jsonObject.toString().getBytes());
            byte[] encryptResponse = jsonObject.toString().getBytes();
            byte[] encryptedSendData = new byte[(encryptResponse.length + 19)];
            int i3 = 0;
            int j4 = 0;
            while (j4 < 16) {
                encryptedSendData[i3] = aesIV[j4];
                j4++;
                i3++;
            }
            int j5 = 0;
            while (j5 < encryptResponse.length) {
                encryptedSendData[i3] = encryptResponse[j5];
                j5++;
                i3++;
            }
            int i4 = i3 + 1;
            encryptedSendData[i3] = 26;
           /* int checksum2 = CryptoCRC.caluCRC(0, encryptedSendData, encryptedSendData.length - 2);
            int i5 = i4 + 1;
            encryptedSendData[i4] = (byte) (checksum2 & 255);
            int i6 = i5 + 1;
            encryptedSendData[i5] = (byte) ((checksum2 >> 8) & 255);*/
            return encryptedSendData;
        } catch (Exception e) {
            return null;
        }
    }

    private void completeBleAdditionProcess() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(NotificationCompat.CATEGORY_STATUS, 1);
            BLECommHandler.getInstance().sendRawDataOverBLE(this.selectedBleDevice,
                    BLEConstants.BLE_SERVICE_ATTR_CONN_UUID, BLECommHandler.getInstance()
                            .encryptBLEData(this.bleDeviceAESKey, jsonObject, (byte) 28), new OperationResultCallback() {


                        public final void onOperationResult(int i, String str) {
                            AddDeviceFragment.this.lambda$completeBleAdditionProcess$27$AddDeviceFragment(i, str);
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
                    AddDeviceFragment.this.lambda$null$25$AddDeviceFragment();
                }
            });
            return;
        }
        Log.e(TAG, "Error sending auth check request");
        getActivity().runOnUiThread(new Runnable() {


            public final void run() {
                AddDeviceFragment.this.lambda$null$26$AddDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$null$25$AddDeviceFragment() {
        stopAddTimer();
        this.loadingDialog.hide();
        redirectToHome();
    }

    public /* synthetic */ void lambda$null$26$AddDeviceFragment() {
        stopAddTimer();
        this.loadingDialog.hide();
    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        loadingDialog.hide();
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
                        this.loadingDialog.hide();

                        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(1) adding Gateway.\nPlease check internet connection and retry.", null);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    this.loadingDialog.hide();
                    MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(1) adding Gateway.\nPlease check internet connection and retry.", null);

                }
            } else {
                this.loadingDialog.hide();
                MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(1) adding Gateway.\nPlease check internet connection and retry.", null);
            }
        }

    }

    @Override
    public void onFailResponse(int apiId, String error) {
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(1) adding Gateway.\nPlease check internet connection and retry.", null);
    }
}
