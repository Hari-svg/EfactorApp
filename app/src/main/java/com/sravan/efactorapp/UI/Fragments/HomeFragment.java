package com.sravan.efactorapp.UI.Fragments;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.sravan.efactorapp.Adapter.GatewayAdapter;
import com.sravan.efactorapp.Adapter.RoomAdapter;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.Model.ROOMINFOPOJO;
import com.sravan.efactorapp.Model.UserLogin;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.Device.AddEndDeviceFragment;
import com.sravan.efactorapp.UI.Fragments.Gateway.AddDeviceFragment;
import com.sravan.efactorapp.UI.Fragments.Gateway.AddGateway;
import com.sravan.efactorapp.UI.Fragments.Room.AddRoom;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;
import com.sravan.efactorapp.utils.DataBase.Model.GatewayDao;

import java.io.IOException;
 import java.io.Serializable;
 import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.SmartLocation;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements ApiHitListener,RoomAdapter.OnItemClickListener{

    private Button add_gateway, add_device,add_room,logout;
    private String TAG = HomeFragment.class.getSimpleName();
    private LinearLayout gateway_layout,add_gateway_layout,roomLayout,addRoom_layout;
    private SessionManager sessionManager;
    private RecyclerView RoomsRV,GatewayRV;
    private RestClient restClient;
    private List<Gateway> GatewayList;
    private List<ROOMINFOPOJO.Room> RoomList;

    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void findView() {
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Welcome");
        add_gateway = (Button) findViewByIds(R.id.add_gateway);
        add_room = (Button) findViewByIds(R.id.add_room);
        logout = (Button) findViewByIds(R.id.logout);
        gateway_layout=(LinearLayout) findViewByIds(R.id.gateway_layout);
        add_gateway_layout=(LinearLayout) findViewByIds(R.id.addGateway_layout);
        roomLayout=(LinearLayout) findViewByIds(R.id.roomLayout);
        addRoom_layout=(LinearLayout) findViewByIds(R.id.addRoom_layout);
        RoomsRV=(RecyclerView) findViewByIds(R.id.room_rv);
        GatewayRV=(RecyclerView) findViewByIds(R.id.gateway_rv);
    }

    @Override
    protected void init() {
        sessionManager=new SessionManager(getActivity());
        restClient=new RestClient(getActivity());
        Log.e(TAG,"HOme Frag --> UserID :"+sessionManager.getUserId());
        GatewayList=new ArrayList<>();

        if (sessionManager.isLoggedIn()){
            displayProgressBar(false);
            GetGateways(sessionManager.getUserId());
            GetRooms(sessionManager.getUserId());
        }
        add_gateway.setOnClickListener(this);
        add_room.setOnClickListener(this);
        logout.setOnClickListener(this);
        gateway_layout.setOnClickListener(this);

    }

    private void GetRooms(String userId) {

        restClient.callback(this).GET_ROOMS(userId);
    }

    private void GetGateways(String userId) {
        restClient.callback(this).GET_GATEWAYS(userId);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.add_gateway:
          //      Add_Gateway();
                break;
            case R.id.add_room:
                hideKeyPad();
                Addroom();
                break;

            case R.id.logout:
                // MyConstant.OTP_type = "V";
                sessionManager.logoutUser();
                break;
            case R.id.gateway_layout:
                Gateway_list();
            default:
                break;
        }
    }

    private void Gateway_list() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("GATEWA_LIST", (Serializable) GatewayList);
        setFragmentsWithBundle(R.id.frameLayout, new MyGateways(), bundle, true);
    }

    private void Addroom() {

        setFragments(R.id.frameLayout, new AddRoom(), true);
    }

    private void Add_Gateway() {
        Bundle bundle = new Bundle();
        bundle.putString("ADD_TYPE", GatewayDao.TABLENAME);
        setFragmentsWithBundle(R.id.frameLayout, new AddDeviceFragment(), bundle, true);

    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_GET_GATEWAY_INFO) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    GATEWAYPOJO pojo = gson.fromJson(s, GATEWAYPOJO.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "" + pojo.getGateways());
                            GatewayList=pojo.getGateways();
                            sessionManager.setLOCALIP(GatewayList.get(0).getGatewayIp());

                            if (GatewayList.size()==0){
                                add_gateway_layout.setVisibility(View.VISIBLE);
                            }else {
                                add_gateway_layout.setVisibility(View.GONE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                                GatewayRV.setLayoutManager(gridLayoutManager);
                              //  GatewayAdapter gatewayAdapter=new GatewayAdapter(getActivity(),GatewayList);
                                //GatewayRV.setAdapter(gatewayAdapter);
                            }
                            //setFragments(R.id.frameLayout, new HomeFragment(), true);
                            Log.e(TAG, "GatewayList" + GatewayList.size());

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
        }else if (apiId == ApiIds.ID_GET_ROOM_INFO) {
            dismissProgressBar();
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    ROOMINFOPOJO pojo = gson.fromJson(s, ROOMINFOPOJO.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "" + pojo.getRooms());
                            RoomList=pojo.getRooms();
                            if (RoomList.size()==0){
                                addRoom_layout.setVisibility(View.VISIBLE);
                            }
                            else {
                                addRoom_layout.setVisibility(View.GONE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                                RoomsRV.setLayoutManager(gridLayoutManager);
                                RoomAdapter roomAdapter=new RoomAdapter(getActivity(),RoomList);
                                RoomsRV.setAdapter(roomAdapter);
                            }
                            //setFragments(R.id.frameLayout, new HomeFragment(), true);
                            Log.e(TAG, "RoomList" + RoomList);

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
    public void onItemClick(ROOMINFOPOJO.Room item) {
        showToast(String.valueOf(item));
        /*Bundle bundle=new Bundle();
        bundle.putString("RoomId");
        setFragmentsWithBundle();*/

    }


}
