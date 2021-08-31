package com.sravan.efactorapp.UI.Fragments.Device;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sravan.efactorapp.Adapter.DeviceListAdapter;
import com.sravan.efactorapp.Adapter.GatewayAdapter;
import com.sravan.efactorapp.Adapter.RoomAdapter;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.Model.MYDEVICESPOJO;
import com.sravan.efactorapp.Model.ROOMINFOPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.Gateway.AddDeviceFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class MyDevices extends BaseFragment implements ApiHitListener, DeviceListAdapter.OnDeviceClickListener {
    private static final String ROOMID ="ROOMID" ;
    private static final String GATEWAYID ="GATEWAYID" ;
    private static final String TAG = MyDevices.class.getSimpleName();

    private String RoomId,GatewayId;
    private RestClient restClient;
    private SessionManager sessionManager;
    private RecyclerView Device_rv;
    private Button bt_add_room;
    private LinearLayout addDevice_layout;
    private List<MYDEVICESPOJO.DeviceDATA> DeviceDataList;
    private String selectedDeeviceName = null;

    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_my_devices;
    }
    public static AddDeviceFragment newInstance(String roomId,String gatewayId) {
        AddDeviceFragment fragment = new AddDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ROOMID, roomId);
        args.putString(GATEWAYID,gatewayId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void findView() {
        if (getArguments() != null) {
            this.RoomId = getArguments().getString(ROOMID);
            this.GatewayId = getArguments().getString(GATEWAYID);
            String str = TAG;
            Log.d(str, "Room Id : " + this.RoomId);
            Log.d(str, "Gateway Id : " + this.GatewayId);
        }
        addDevice_layout=(LinearLayout) findViewByIds(R.id.addDevice_layout);
        Device_rv=(RecyclerView) findViewByIds(R.id.device_rv);
        bt_add_room=(Button) findViewByIds(R.id.add_device);

    }

    @Override
    protected void init() {
        sessionManager=new SessionManager(getActivity());
        restClient=new RestClient(getActivity());
        DeviceDataList=new ArrayList<>();
        if (sessionManager.isLoggedIn()){
            displayProgressBar(false);
            GetDevices(sessionManager.getUserId());

        }
        bt_add_room.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.add_device:
               // Add_Gateway();
                Bundle bundle=new Bundle();
                bundle.putString("ROOMID",RoomId);
                bundle.putString("GATEWAYID",GatewayId);
                setFragmentsWithBundle(R.id.frameLayout,new AddEndDeviceFragment(),bundle,true);
                break;
            /*case R.id.add_room:
                hideKeyPad();
                Addroom();
                break;

            case R.id.logout:
                // MyConstant.OTP_type = "V";
                sessionManager.logoutUser();
                break;*/
            default:
                break;
        }
    }

    private void GetDevices(String userId) {
        restClient.callback(this).GET_DEVICES(RoomId);
    }
    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_GET_DEVICES) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    MYDEVICESPOJO pojo = gson.fromJson(s, MYDEVICESPOJO.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "MyDevices Data : " + pojo.getDevices());
                            DeviceDataList=pojo.getDevices();
                            if (DeviceDataList.size()==0){
                                addDevice_layout.setVisibility(View.VISIBLE);
                            }else {
                                addDevice_layout.setVisibility(View.GONE);
                                this.Device_rv.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
                                this.Device_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                DeviceListAdapter Adapter=new DeviceListAdapter(getActivity(),DeviceDataList,this);
                                Device_rv.setAdapter(Adapter);
                            }
                            //setFragments(R.id.frameLayout, new HomeFragment(), true);
                            Log.e(TAG, "DeviceList" + DeviceDataList.size());

                        } catch (Exception e) {
                            dismissProgressBar();
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dismissProgressBar();
                        displayErrorDialog("Error", pojo.getStatus());
                    }

                } catch (IOException e) {
                    dismissProgressBar();
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

    @Override
    public void OnDeviceClick(MYDEVICESPOJO.DeviceDATA device, int i) {

    }

    @Override
    public void OnSwitchClick(MYDEVICESPOJO.DeviceDATA device, boolean power) {

        String str = TAG;
        Log.d(str, "Device Power Clicked: " + power + "\n" + device);
        Fan fan = new Fan(0);
        if (power) {
            fan.setPower(1);
            //  fan.setPower(on);
        }
        String str2 = TAG;
        String data = null;
        Log.d(str2, "Device Status: " + fan.setStatusFrame());
       /* if(fan.setStatusFrame()=="5031") {
            fan.setStatusFrame()="01005508A1C2000000016C";
        }*/
      //  this.loadingDialog.show();
        this.selectedDeeviceName = device.getDeviceName();
        Log.d(str2, "Device name: " + device.getDeviceName());

        DataHandler.getInstance().setDeviceStatus(device, fan.setStatusFrame());


    }
}