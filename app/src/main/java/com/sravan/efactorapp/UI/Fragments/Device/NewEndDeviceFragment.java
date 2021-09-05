package com.sravan.efactorapp.UI.Fragments.Device;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.dynamite.ProviderConstants;
import com.google.gson.Gson;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.NewDeviceData;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.Gateway.MessageDialog;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Device;
import com.sravan.efactorapp.utils.DataBase.Model.DeviceDao;
import com.sravan.efactorapp.utils.FragmentRequestType;
import com.sravan.efactorapp.utils.LoadingDialog;
import com.sravan.efactorapp.utils.LocalRPICommunication;
import com.sravan.efactorapp.utils.OnFragmentInteractionListener;
import com.sravan.efactorapp.utils.ReachabilityHandler;
import com.sravan.efactorapp.utils.Utilities;


import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class NewEndDeviceFragment extends Fragment implements ApiHitListener {
    private static final String TAG = NewEndDeviceFragment.class.getSimpleName();
    private Button buttonAdd;
    private EditText editTextName;
    private LoadingDialog loadingDialog;
    private LocalRPICommunication localRPICommunication = new LocalRPICommunication();
    private OnFragmentInteractionListener mListener;
    private NewDeviceData newDeviceData;
    private View rootView;
    private TextView textViewModel;
    private TextView textViewType;
    private long updatedAt = 0;
    private SessionManager sessionManager;
    private RestClient restClient;
    private String RoomId,GatewayId;
    @Override // androidx.fragment.app.Fragment

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.newDeviceData = new NewDeviceData();
        try {
            if (getArguments() != null) {
                RoomId=getArguments().getString("ROOMID");
                GatewayId=getArguments().getString("GATEWAYID");
                JSONObject jsonObject = new JSONObject(getArguments().getString("NEW_DEVICE"));
                String str = TAG;
                Log.d(str, "New Device: " + jsonObject);
                this.newDeviceData.setId(jsonObject.getString("device_id"));
                //TODO GateWay Id while adding Device is adding here
                this.newDeviceData.setGateway_id(GatewayId);
                this.newDeviceData.setVer(jsonObject.getString(ProviderConstants.API_COLNAME_FEATURE_VERSION));
                this.newDeviceData.setManufacturer(jsonObject.getString("manufacturer"));
                this.newDeviceData.setFw_ver(jsonObject.getString("fw_version"));
                this.newDeviceData.setModel_name(jsonObject.getString("model_name"));
                this.newDeviceData.setModel_desc(jsonObject.getString("model_desc"));
                this.newDeviceData.setModel_version(jsonObject.getString("model_ver"));
              //  this.newDeviceData.setFeatureData(jsonObject.getString("feature_data"));
                this.newDeviceData.setType(jsonObject.getString("type"));
                this.newDeviceData.setStatusData(jsonObject.getString("status_data"));
                //this.newDeviceData.setFeatureData(jsonObject.getString("feature_data"));
                this.updatedAt = Utilities.getEpoch();

            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_new_end_device, container, false);
        this.loadingDialog = new LoadingDialog(getActivity());
       this.sessionManager = new SessionManager(getActivity());
        this.restClient = new RestClient(getActivity());
        ReachabilityHandler.init(getContext());
        this.textViewType = (TextView) this.rootView.findViewById(R.id.textViewType);
        this.textViewModel = (TextView) this.rootView.findViewById(R.id.textViewModel);
        this.editTextName = (EditText) this.rootView.findViewById(R.id.editTextName);
        this.buttonAdd = (Button) this.rootView.findViewById(R.id.buttonAdd);
        this.buttonAdd.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                NewEndDeviceFragment.this.lambda$onCreateView$0$NewEndDeviceFragment(view);
            }
        });
        updateDeviceDetails();
        return this.rootView;
    }

    public /* synthetic */ void lambda$onCreateView$0$NewEndDeviceFragment(View v) {
        if (validateName()) {
            addNewDevice();
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
        getActivity().getWindow().addFlags(128);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        this.loadingDialog.hide();
        getActivity().getWindow().clearFlags(128);
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    private void updateDeviceDetails() {
        TextView textView = this.textViewType;
        textView.setText("Type: " + this.newDeviceData.getType() + "  Ver: " + this.newDeviceData.getVer());
        TextView textView2 = this.textViewModel;
        textView2.setText("Model: " + this.newDeviceData.getModel_name() + "  Ver: " + this.newDeviceData.getModel_version());
    }

    private boolean validateName() {
        this.editTextName.setError(null);
        String name = this.editTextName.getText().toString();
        if (name == null && name.length() < 3) {
            return false;
        }
        this.newDeviceData.setName(name);
        return true;
    }

    private void addNewDevice() {
        this.loadingDialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", this.newDeviceData.getId());
            jsonObject.put("gateway_id", this.newDeviceData.getGateway_id());
            jsonObject.put("device_name", this.newDeviceData.getName());
            jsonObject.put("device_type", this.newDeviceData.getType());
            jsonObject.put("device_ver", this.newDeviceData.getVer());
            jsonObject.put("device_fw_ver", this.newDeviceData.getFw_ver());
            jsonObject.put("device_manufacturer", this.newDeviceData.getManufacturer());
            jsonObject.put("device_model_version", this.newDeviceData.getModel_version());
            jsonObject.put("device_model_name", this.newDeviceData.getModel_name());
            jsonObject.put("device_model_desc", this.newDeviceData.getModel_desc());
            jsonObject.put("device_feature_data", this.newDeviceData.getFeatureData());
            jsonObject.put("device_status_data", this.newDeviceData.getStatusData());
            jsonObject.put("updated_at", this.updatedAt);

            // jsonObject.put("device_id", "FFFFFFFFFFFFFFFFC4C3C2C1D4D30300");
            /*jsonObject.put("gateway_id", "3684680538cf4d4c9ddb73c4ecefb03e");
            jsonObject.put("device_name", "light");
            jsonObject.put("device_type", "Device");
            jsonObject.put("device_ver", "V1.0");
            jsonObject.put("device_fw_ver", "V2.10");
            jsonObject.put("device_manufacturer", "Efactor");
            jsonObject.put("device_model_version", "V:1");
            jsonObject.put("device_model_name", "first");
            jsonObject.put("device_model_desc", "bldc_fan");
            jsonObject.put("device_feature_data", "");
            jsonObject.put("device_status_data", "status");
            jsonObject.put("updated_at", "");*/


            Log.e(TAG, "JsonOBJ : " + jsonObject);
           Log.e(TAG, "ROOMID : " + RoomId);
            Log.e(TAG, "GATEWAYID : " + GatewayId);
           restClient.callback(this).Add_Device(sessionManager.getUserId(), this.newDeviceData.getGateway_id(),
                    RoomId, this.newDeviceData.getId(), this.newDeviceData.getName(), this.newDeviceData.getType(), this.newDeviceData.getVer(), this.newDeviceData.getFw_ver(),
                    this.newDeviceData.getManufacturer(), this.newDeviceData.getModel_version(), this.newDeviceData.getModel_name(), this.newDeviceData.getModel_desc(),
                    this.newDeviceData.getStatusData());
           Log.e(TAG,"userId"+sessionManager.getUserId()+"\n"+"GatewayId"+this.newDeviceData.getGateway_id()+"\n"+"getId"+this.newDeviceData.getId()
                   +"\n"+"Name" +this.newDeviceData.getName()+"\n"+"Type :"+this.newDeviceData.getType());

           /* CloudHandler.getInstance().addEndDevice(jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        NewEndDeviceFragment.this.lambda$addNewDevice$1$NewEndDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        NewEndDeviceFragment.this.lambda$addNewDevice$2$NewEndDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        } catch (Exception e) {
            this.loadingDialog.hide();

            MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error adding new device.\nPlease check your internet connection.", null);
        }
    }

    public /* synthetic */ void lambda$addNewDevice$1$NewEndDeviceFragment(Object o) throws Exception {
        if (o.equals("success")) {
            addDeviceToLocalDB();
            return;
        }

        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error adding new device.\nPlease check your internet connection.", null);
    }

    public /* synthetic */ void lambda$addNewDevice$2$NewEndDeviceFragment(Object throwable) throws Exception {
        this.loadingDialog.hide();
        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "Error adding new device.\nPlease check your internet connection.", null);
    }

    private void addDeviceToLocalDB() {
        try {
            Device device = new Device();

           /* device.setDevice_id("FFFFFFFFFFFFFFFFC4C3C2C1D4D30300");
            device.setGateway_id("3684680538cf4d4c9ddb73c4ecefb03e");
            device.setName("light");
            device.setType("Device");
            device.setVer("V1.0");
            device.setFw_ver("V2.10");
            device.setManufacturer("Efactor");
            device.setModel_name("first");
            device.setModel_desc("bldc_fan");
            device.setModel_version("");
            device.setStatus_data("status");*/
            //  device.setStatus_updated_at("");
            device.setDevice_id(this.newDeviceData.getId());
            device.setGateway_id(this.newDeviceData.getGateway_id());
            device.setName(this.newDeviceData.getName());
            device.setType(this.newDeviceData.getType());
            device.setVer(this.newDeviceData.getVer());
            device.setFw_ver(this.newDeviceData.getFw_ver());
            device.setManufacturer(this.newDeviceData.getManufacturer());
            device.setModel_name(this.newDeviceData.getModel_name());
            device.setModel_desc(this.newDeviceData.getModel_desc());
            device.setModel_version(this.newDeviceData.getModel_version());
            device.setFeature_data(this.newDeviceData.getFeatureData());
            device.setStatus_data(this.newDeviceData.getStatusData());
            device.setStatus_updated_at(this.updatedAt);
            device.setUpdated_at(this.updatedAt);
             Device _tempD = DatabaseHandler.getInstance().getDevice(this.newDeviceData.getId());
           // Device _tempD = DatabaseHandler.getInstance().getDevice("FFFFFFFFFFFFFFFFC4C3C2C1D4D30400");
            if (_tempD != null) {
                DatabaseHandler.getInstance().deleteDevice(_tempD);
            }
            DatabaseHandler.getInstance().addDevice(device);
            //TODO- change ip to dynamic
            Log.e(TAG, ReachabilityHandler.getInstance().getGatewayLocalIP(this.newDeviceData.getGateway_id()));
            this.localRPICommunication.updateDeviceList(ReachabilityHandler.getInstance().getGatewayLocalIP(this.newDeviceData.getGateway_id())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                //this.localRPICommunication.updateDeviceList("192.168.0.100").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        NewEndDeviceFragment.this.lambda$addDeviceToLocalDB$3$NewEndDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    try {
                        NewEndDeviceFragment.this.lambda$addDeviceToLocalDB$4$NewEndDeviceFragment(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public /* synthetic */ void lambda$addDeviceToLocalDB$3$NewEndDeviceFragment(Object o) {
        this.loadingDialog.hide();
        Bundle bundle = new Bundle();
        bundle.putString("ADD_TYPE", DeviceDao.TABLENAME);
        Toast.makeText(getContext(), "NEW END device - Add_Type" + DeviceDao.TABLENAME, Toast.LENGTH_SHORT).show();
        Fragment frag=new HomeFragment();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, frag, "add DeviceTag");
        ft.commit();
        this.mListener.onFragmentInteraction(FragmentRequestType.FRAGMENT_ADD_THING_COMPLETE, bundle);
    }

    public /* synthetic */ void lambda$addDeviceToLocalDB$4$NewEndDeviceFragment(Object throwable) {
        this.loadingDialog.hide();
        Bundle bundle = new Bundle();
        bundle.putString("ADD_TYPE", DeviceDao.TABLENAME);

        Toast.makeText(getContext(), "NEW END device - Add_Type" + DeviceDao.TABLENAME, Toast.LENGTH_SHORT).show();
        this.mListener.onFragmentInteraction(FragmentRequestType.FRAGMENT_ADD_THING_COMPLETE, bundle);
    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        loadingDialog.hide();
        if (apiId == ApiIds.ID_ADD_DEVICE) {
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
                           // NewEndDeviceFragment.this.lambda$addNewDevice$1$NewEndDeviceFragment(login_pojo.getStatus());
                            this.loadingDialog.hide();
                            Bundle bundle = new Bundle();
                            bundle.putString("ADD_TYPE", DeviceDao.TABLENAME);
                            Toast.makeText(getContext(), "NEW END device - Add_Type" + DeviceDao.TABLENAME, Toast.LENGTH_SHORT).show();


                            //this.mListener.onFragmentInteraction(FragmentRequestType.FRAGMENT_ADD_THING_COMPLETE, bundle);
                            addDeviceToLocalDB();
                            // setFragments(R.id.frameLayout, new HomeFragment(), true);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        MessageDialog.showErrorDialogWithOkBtn(getActivity(), "" + login_pojo.getMessage(), null);
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
