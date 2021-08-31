package com.sravan.efactorapp.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.sravan.efactorapp.Adapter.DeviceListAdapter;
import com.sravan.efactorapp.Adapter.GatewayAdapter;
import com.sravan.efactorapp.Base.AdapterClickListener;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.Model.MYDEVICESPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.Gateway.AddDeviceFragment;
import com.sravan.efactorapp.UI.Fragments.Gateway.EditGateway;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.Model.GatewayDao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MyGateways extends BaseFragment implements AdapterClickListener {
    private static final String ARG_PARAM = "GATEWA_LIST";
    private static final String TAG = MyGateways.class.getSimpleName();
    private RecyclerView GatewayRV;
    private List<GATEWAYPOJO.Gateway> GatewayList;
    private String GATEWAYID;
    private RestClient restClient;
    private SessionManager sessionManager;
    private FloatingActionButton add_gateway;


    public static MyGateways newInstance(String param1) {
        MyGateways fragment = new MyGateways();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResourceView() {
        return R.layout.my_gatways;
    }

    @Override
    protected void findView() {
        GatewayList = new ArrayList<>();
       /* if (getArguments() != null) {
            GatewayList = (ArrayList<GATEWAYPOJO.Gateway>) getArguments().getSerializable(ARG_PARAM);
            String str = TAG;
            Log.d(str, "Gateway List : " + this.GatewayList);
        }*/
        GatewayRV = (RecyclerView) findViewByIds(R.id.gateway_rv);
        GatewayRV.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        GatewayRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        add_gateway = (FloatingActionButton) findViewByIds(R.id.add_gateway);

    }

    @Override
    protected void init() {
        add_gateway.setOnClickListener(this);
        sessionManager = new SessionManager(getContext());
        restClient = new RestClient(getContext());
        RefreshGateways();
     /*   GatewayAdapter gatewayAdapter = new GatewayAdapter(this, GatewayList);
        GatewayRV.setAdapter(gatewayAdapter);*/

    }

    @Override
    public void onAdapterClickListener(int position, String action) {
        if (action.equals("Delete")) {
            //showToast(GatewayList.get(position).getGatewayId());
            GATEWAYID = GatewayList.get(position).getId();
            displayProgressBar(false);
            restClient.callback(this).DELETE_GATEWAY(GATEWAYID);

        }
        else if (action.equals("Edit")){
            GATEWAYID=GatewayList.get(position).getId();
            Bundle bundle=new Bundle();
            bundle.putSerializable("gatewaylist", (Serializable) GatewayList.get(position));
            setFragmentsWithBundle(R.id.frameLayout,new EditGateway(),bundle,true);
        }
    }

    private void RefreshGateways() {
        displayProgressBar(false);
        restClient.callback(this).GET_GATEWAYS(sessionManager.getUserId());
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.add_gateway:
                Bundle bundle = new Bundle();
                bundle.putString("ADD_TYPE", GatewayDao.TABLENAME);
                setFragmentsWithBundle(R.id.frameLayout, new AddDeviceFragment(), bundle, true);
                break;
        }
    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_DELETE_GATEWAY) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    CommonPojo pojo = gson.fromJson(s, CommonPojo.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            showToast(pojo.getMessage());
                            // setFragments(R.id.frameLayout, new MyGateways(), true);
                            //Log.e(TAG, "DeviceList" + DeviceDataList.size());
                            RefreshGateways();

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
        } else if (apiId == ApiIds.ID_GET_GATEWAY_INFO) {
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
                            GatewayList = pojo.getGateways();
                            // sessionManager.setLOCALIP(GatewayList.get(0).getGatewayIp());;
                            GatewayAdapter gatewayAdapter = new GatewayAdapter(this, GatewayList);
                            GatewayRV.setAdapter(gatewayAdapter);
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
        }
    }


    @Override
    public void onFailResponse(int apiId, String error) {
        dismissProgressBar();
        displayErrorDialog("Error", error);
    }

}
