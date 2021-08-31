package com.sravan.efactorapp.UI.Fragments.Room;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.Model.ROOMINFOPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class AddRoom extends BaseFragment implements ApiHitListener {

    private SessionManager sessionManager;
    private RestClient restClient;
    private EditText roomname_et;
    private Button add_room_bt;
    private String TAG=AddRoom.class.getSimpleName();

    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_add_room;
    }

    @Override
    protected void findView() {

        roomname_et = (EditText) findViewByIds(R.id.room_name);
        add_room_bt = (Button) findViewByIds(R.id.Add_room_bt);
    }

    @Override
    protected void init() {
        sessionManager = new SessionManager(getContext());
        restClient = new RestClient(getContext());
        add_room_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.Add_room_bt:
               String GatewayID= DatabaseHandler.getInstance().getGateways().get(0).getGateway_id();
                Log.e(TAG,GatewayID);
                displayProgressBar(false);
                //  MyConstant.OTP_type = "F";
               restClient.callback(this).ADD_ROOM(sessionManager.getUserId(),GatewayID,roomname_et.getText().toString().trim());
                //  setFragmentsWithBundle(R.id.frameLayout, new ConfirmEmailFragment(), bundle, true);
                break;

        }
    }
    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_ADD_ROOM) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    CommonPojo pojo = gson.fromJson(s, CommonPojo.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "" + pojo.getStatus());
                          showToast(pojo.getMessage());
                            setFragments(R.id.frameLayout, new HomeFragment(), true);

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