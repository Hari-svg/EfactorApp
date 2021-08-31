package com.sravan.efactorapp.UI.Fragments.Device;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.dynamite.ProviderConstants;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.sravan.efactorapp.Interfaces.DialogSubmitListener;
import com.sravan.efactorapp.Model.CheckDevicePOJO;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.Gateway.AddDeviceFragment;
import com.sravan.efactorapp.UI.Fragments.Gateway.DialogOkListener;
import com.sravan.efactorapp.UI.Fragments.Gateway.MessageDialog;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.Constants;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Device;
import com.sravan.efactorapp.utils.DataBase.Model.DeviceDao;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;
import com.sravan.efactorapp.utils.FragmentRequestType;
import com.sravan.efactorapp.utils.LoadingDialog;
import com.sravan.efactorapp.utils.LocalRPICommunication;
import com.sravan.efactorapp.utils.MqttServiceConstants;
import com.sravan.efactorapp.utils.OnFragmentInteractionListener;
import com.sravan.efactorapp.utils.ReachabilityHandler;


import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddEndDeviceFragment extends Fragment implements ApiHitListener {
    private static final String TAG = AddEndDeviceFragment.class.getSimpleName();
    private static final String ROOMID = "ROOMID";
    private static final String GATEWAYID = "GATEWAYID";
    private String RoomId, GatewayId;
    private CodeScanner codeScanner;
    private int gatewayCount = 0;
    private List<Gateway> gatewayList = DatabaseHandler.getInstance().getGateways();
    private LoadingDialog loadingDialog;
    private LocalRPICommunication localRPICommunication = new LocalRPICommunication();
    //private OnFragmentInteractionListener mListener;
    private JSONObject newDeviceJson = null;
    private View rootView;
    int device_count = 0;
    private TextView textViewManualEntry;
    private SessionManager sessionManager;
    private RestClient restClient;
    private OnFragmentInteractionListener mListener;

    public static AddEndDeviceFragment newInstance(String roomId, String gatewayId) {
        AddEndDeviceFragment fragment = new AddEndDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ROOMID, roomId);
        args.putString(GATEWAYID, gatewayId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_add_end_device, container, false);
        if (getArguments() != null) {
            this.RoomId = getArguments().getString(ROOMID);
            this.GatewayId = getArguments().getString(GATEWAYID);
            String str = TAG;
            Log.d(str, "Room Id : " + this.RoomId);
            Log.d(str, "Gateway Id : " + this.GatewayId);
        }

        this.loadingDialog = new LoadingDialog(getActivity());
        this.sessionManager = new SessionManager(getContext());
        this.restClient = new RestClient(getContext());
        ReachabilityHandler.init(getContext());

        CodeScannerView codeScannerView = (CodeScannerView) this.rootView.findViewById(R.id.scanner_view);
        this.codeScanner = new CodeScanner(getActivity(), codeScannerView);
        this.codeScanner.setDecodeCallback(new DecodeCallback() {

            @Override // com.budiyev.android.codescanner.DecodeCallback
            public final void onDecoded(Result result) {
                AddEndDeviceFragment.this.lambda$onCreateView$2$AddEndDeviceFragment(result);
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                AddEndDeviceFragment.this.lambda$onCreateView$3$AddEndDeviceFragment(view);
            }
        });
        this.textViewManualEntry = (TextView) this.rootView.findViewById(R.id.textViewManualEntry);
        this.textViewManualEntry.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                AddEndDeviceFragment.this.lambda$onCreateView$5$AddEndDeviceFragment(view);
            }
        });
        return this.rootView;
    }

    public /* synthetic */ void lambda$onCreateView$2$AddEndDeviceFragment(Result result) {
        String str = TAG;
        Log.d(str, "Decoded Message: " + result);
        String data = result.getText();
        if (data != null) {
            String[] array = data.split(",");
            if (!data.startsWith("<") || !data.endsWith(">") || array.length != 3) {
                getActivity().runOnUiThread(new Runnable() {


                    public final void run() {
                        AddEndDeviceFragment.this.lambda$null$1$AddEndDeviceFragment();
                    }
                });
            } else {
                checkDevice(array);
            }
        }
    }

    public /* synthetic */ void lambda$null$0$AddEndDeviceFragment() {
        this.codeScanner.startPreview();
    }

    public /* synthetic */ void lambda$null$1$AddEndDeviceFragment() {
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Invalid QR Code.\nPlease scan the QR Code printed on the device.", new DialogOkListener() {


            public final void onOkClicked() {
                AddEndDeviceFragment.this.lambda$null$0$AddEndDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$onCreateView$3$AddEndDeviceFragment(View v) {
        this.codeScanner.startPreview();
    }

    public /* synthetic */ void lambda$onCreateView$5$AddEndDeviceFragment(View v) {
        MessageDialog.showDialogWithEditText(getActivity(), "Add Device", "FFFFFFFFFFFFFFFFC4C3C2C1D4D30500", "Device ID", 1, new DialogSubmitListener() {


            public final void onSubmitClicked(String str) {
                AddEndDeviceFragment.this.lambda$null$4$AddEndDeviceFragment(str);
            }
        });
    }

    public /* synthetic */ void lambda$null$4$AddEndDeviceFragment(String enteredText) {
        if (enteredText != null && enteredText.length() > 0) {
            checkDevice(new String[]{enteredText});
        }
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
    public void onResume() {
        super.onResume();
        this.codeScanner.startPreview();
        this.gatewayCount = 0;
        getActivity().getWindow().addFlags(128);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        this.codeScanner.releaseResources();
        this.loadingDialog.hide();
        getActivity().getWindow().clearFlags(128);
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    private void checkDevice(String[] data) {
        getActivity().runOnUiThread(new Runnable() {


            public final void run() {
                AddEndDeviceFragment.this.lambda$checkDevice$6$AddEndDeviceFragment();
            }
        });
        try {
            String deviceid = data[0].replace("<", "");
            Device device = DatabaseHandler.getInstance().getDevice(deviceid);
            if (device == null || !device.getGateway_id().equals(Constants.NONE_GATEWAY_VALUE)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("device_id", deviceid);
                //      this.newDeviceJson.put("device_id", deviceid);

               /* CloudHandler.getInstance().checkEndDevice(jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddEndDeviceFragment.this.lambda$checkDevice$9$AddEndDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer() {


                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        AddEndDeviceFragment.this.lambda$checkDevice$11$AddEndDeviceFragment(obj);
                    }
                });*/

                restClient.callback(this).CheckDevice(deviceid, sessionManager.getUserId());
                return;
            }
            this.newDeviceJson = new JSONObject();
            this.newDeviceJson.put("device_id", device.getDevice_id());
            //  this.newDeviceJson.put("device_id", deviceid);
            this.newDeviceJson.put("name", device.getName());
            this.newDeviceJson.put(ProviderConstants.API_COLNAME_FEATURE_VERSION, device.getVer());
            this.newDeviceJson.put("type", device.getType());
            this.newDeviceJson.put("fw_version", device.getFw_ver());
            this.newDeviceJson.put("manufacturer", device.getManufacturer());
            this.newDeviceJson.put("model_ver", device.getModel_version());
            this.newDeviceJson.put("model_name", device.getModel_name());
            this.newDeviceJson.put("model_desc", device.getModel_desc());
            this.newDeviceJson.put("feature_data", device.getFeature_data());
            this.newDeviceJson.put("status_data", device.getStatus_data());
            this.newDeviceJson.put("updated_at", device.getUpdated_at());
            checkDeviceLocalReachable();
        } catch (Exception e) {
            getActivity().runOnUiThread(new Runnable() {

                public final void run() {
                    AddEndDeviceFragment.this.lambda$checkDevice$13$AddEndDeviceFragment();
                }
            });
        }
    }

    public /* synthetic */ void lambda$checkDevice$6$AddEndDeviceFragment() {
        this.loadingDialog.show();
    }

    public /* synthetic */ void lambda$checkDevice$9$AddEndDeviceFragment(Object o) {
        try {
            JSONObject responseJson = (JSONObject) o;
            String str = TAG;
            // Log.d(str, "Add end Device-Data Length: " + responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).length());
            /*if (responseJson.getInt("statusCode") != 200 || responseJson.getBoolean("is_added") || responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).length() == 0) {
                throw new CommunicationErrorException();
            }*/
            String str2 = TAG;
            //  Log.d(str2, "New Device Data: str2 :  " + responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD).toString());
            //   this.newDeviceJson = responseJson.getJSONObject(DataBufferSafeParcelable.DATA_FIELD);

            Log.e(TAG, "Response Add end device" + responseJson.toString());
            this.newDeviceJson = responseJson;


            if (device_count == 0) {
                checkDeviceLocalReachable();
                device_count = 1;
            }


        } catch (Exception e) {
            getActivity().runOnUiThread(new Runnable() {

                public final void run() {
                    AddEndDeviceFragment.this.lambda$null$8$AddEndDeviceFragment();
                }
            });
        }
    }

    public /* synthetic */ void lambda$null$8$AddEndDeviceFragment() {
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Device not found or is registered to another user.", new DialogOkListener() {


            public final void onOkClicked() {
                AddEndDeviceFragment.this.lambda$null$7$AddEndDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$null$7$AddEndDeviceFragment() {
        this.codeScanner.startPreview();
    }

    public /* synthetic */ void lambda$checkDevice$11$AddEndDeviceFragment(Object throwable) {
        getActivity().runOnUiThread(new Runnable() {


            public final void run() {
                AddEndDeviceFragment.this.lambda$null$10$AddEndDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$null$10$AddEndDeviceFragment() {
        this.loadingDialog.hide();
    }

    public /* synthetic */ void lambda$checkDevice$13$AddEndDeviceFragment() {
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error adding new device.\nPlease retry.", new DialogOkListener() {


            public final void onOkClicked() {
                AddEndDeviceFragment.this.lambda$null$12$AddEndDeviceFragment();
            }
        });
    }

    public /* synthetic */ void lambda$null$12$AddEndDeviceFragment() {
        this.codeScanner.startPreview();
    }

    private void checkDeviceLocalReachable() {
       Log.e(TAG, "Checking" + this.gatewayList.size());
        Log.e(TAG, "DATABASE INSTANCE" + DatabaseHandler.getInstance());
        Log.e(TAG, "GATEWAY MAC" + this.gatewayList.get(0).getMac());
        Log.e(TAG, "REACHABILITY INSTANCE" + ReachabilityHandler.getInstance());
        Log.e(TAG, "REACHABILITY getIp" + ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(0).getMac()));
        List<Gateway> list = this.gatewayList;
        Log.d(TAG, "checkDeviceLocalReachable" + ":" + ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(0).getMac()));


  /*      Log.e(TAG,"Checking"+this.gatewayList.size());
        List<Gateway> list = this.gatewayList;
        Log.d(TAG,"checkDeviceLocalReachable"+ ":" +ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(this.gatewayCount).getMac()));*/



       /* if (list == null || this.gatewayCount >= list.size()) {
            this.gatewayCount = 0;
          //  this.loadingDialog.hide();
            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "To add a device your Gateway needs to reachable locally.\nDevice needs to be in range of the gateway.", null);
        } else*/
        /* if (!ReachabilityHandler.getInstance().isThingReachable(this.gatewayList.get(this.gatewayCount).getGateway_id(), ThingReachability.LOCAL_WIFI)) {
            this.gatewayCount++;
            checkDeviceLocalReachable();
        } else*/

        {
            try {
                // this.localRPICommunication.checkNewDevice(ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(this.gatewayCount).getMac()), this.newDeviceJson.getString("device_id"), getFormat(this.newDeviceJson.getString("type"))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                Log.d(TAG, "checkDeviceLocalReachable" + ":" + this.newDeviceJson);
                this.localRPICommunication.checkNewDevice(ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(this.gatewayCount).getMac()), this.newDeviceJson.getString("device_id"), "01005508AD0100000000B6").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
               // this.localRPICommunication.checkNewDevice(/*sessionManager.getLOCALIP()*/ReachabilityHandler.getInstance().getLocalIP(this.gatewayList.get(this.gatewayCount).getMac()), this.newDeviceJson.getString("device_id"), "01005508AD0100000000B6").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
//this.localRPICommunication.checkNewDevice("192.168.0.154", this.newDeviceJson.getString("device_id"), "01005508AD0100000000B6").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddEndDeviceFragment.this.lambda$checkDeviceLocalReachable$14$AddEndDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        try {
                            AddEndDeviceFragment.this.lambda$checkDeviceLocalReachable$15$AddEndDeviceFragment(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                this.gatewayCount++;
                checkDeviceLocalReachable();
            }
        }
    }

    public /* synthetic */ void lambda$checkDeviceLocalReachable$14$AddEndDeviceFragment(Object o) throws Exception {
        JSONObject responseJson = (JSONObject) o;
        if (responseJson.getString(MqttServiceConstants.PAYLOAD) == null || responseJson.getString(MqttServiceConstants.PAYLOAD).length() == 0) {
            this.gatewayCount++;
            checkDeviceLocalReachable();
            return;
        }

        Bundle bundle = new Bundle();

        bundle.putString("ROOMID", RoomId);
        bundle.putString("GATEWAYID", GatewayId);

        bundle.putString("NEW_DEVICE", this.newDeviceJson.toString());
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment frag=new NewEndDeviceFragment();
        frag.setArguments(bundle);
        ft.replace(R.id.frameLayout, frag, "tag");
        ft.commit();

    }

    public /* synthetic */ void lambda$checkDeviceLocalReachable$15$AddEndDeviceFragment(Object throwable) throws Exception {
        this.gatewayCount++;
        checkDeviceLocalReachable();
    }

    private String getFormat(String type) {
        if (type.equals("FAN")) {
            return Fan.requestStatusFrame();
        }
        return null;
    }


    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        loadingDialog.hide();
        if (apiId == ApiIds.ID_CHECK_DEVICE_INFO) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    CheckDevicePOJO device = gson.fromJson(s, CheckDevicePOJO.class);
                    if (device.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "" + device.getStatus());
                            this.loadingDialog.hide();

                            this.newDeviceJson = new JSONObject();
                            this.newDeviceJson.put("device_id", device.getData().get(0).getDeviceId());
                            //  this.newDeviceJson.put("device_id", deviceid);
                            this.newDeviceJson.put("name", device.getData().get(0).getDeviceName());
                            this.newDeviceJson.put(ProviderConstants.API_COLNAME_FEATURE_VERSION, device.getData().get(0).getDeviceVer());
                            this.newDeviceJson.put("type", device.getData().get(0).getDeviceType());
                            this.newDeviceJson.put("fw_version", device.getData().get(0).getDeviceFwVer());
                            this.newDeviceJson.put("manufacturer", device.getData().get(0).getDeviceManufacturer());
                            this.newDeviceJson.put("model_ver", device.getData().get(0).getDeviceModelVersion());
                            this.newDeviceJson.put("model_name", device.getData().get(0).getDeviceModelName());
                            this.newDeviceJson.put("model_desc", device.getData().get(0).getDeviceModelDesc());
                            this.newDeviceJson.put("feature_data", device.getData().get(0).getDeviceFeatureData());
                            this.newDeviceJson.put("status_data", device.getData().get(0).getDeviceStatusData());
                            this.newDeviceJson.put("updated_at", device.getData().get(0).getUpdatedAt());
                            //  storeDeviceDatabase();
                            // setFragments(R.id.frameLayout, new HomeFragment(), true);
                            AddEndDeviceFragment.this.lambda$checkDevice$9$AddEndDeviceFragment(newDeviceJson);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "" + device.getStatus(), null);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    MessageDialog.showErrorDialogWithOkBtn(getActivity(), "" + e.getMessage(), null);
                }
            } else {
                loadingDialog.hide();
                MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error(10)", null);
            }
        }

    }

    @Override
    public void onFailResponse(int apiId, String error) {
        loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "" + error, null);

    }
}
