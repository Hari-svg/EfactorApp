package com.sravan.efactorapp.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.SendOTPPOJO;
import com.sravan.efactorapp.Model.UserLogin;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.utils.ConnectionDetector;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class signUp_one extends BaseFragment {
private EditText editTextPhone;
private Button buttonContinue;
private RestClient restClient;
    private String TAG=signUp_one.class.getSimpleName();
    private String MobileNumber;

    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_sign_up_one;
    }

    @Override
    protected void findView() {
        editTextPhone=(EditText) findViewByIds(R.id.editTextPhone);
        buttonContinue=(Button) findViewByIds(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isNetAvail(getContext())) {
                    if (editTextPhone.getText().toString().trim().length() == 10 && !editTextPhone.getText().toString().isEmpty()) {
                        SendOTP(editTextPhone.getText().toString().trim());
                        MobileNumber=editTextPhone.getText().toString().trim();
                    } else {
                        showToast("Please Enter Valid Mobile Number");
                    }
                }else {
                    showToast("No Internet Connection");

                }
            }
        });
    }



    @Override
    protected void init() {
        restClient=new RestClient(getContext());
    }
    private void SendOTP(String mobile) {
        displayProgressBar(false);
        restClient.callback(this).Send_OTP(mobile);
    }
    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_SEND_OTP_SUCCESS) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    SendOTPPOJO pojo = gson.fromJson(s, SendOTPPOJO.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "Res USER" + pojo.getMessage());
                            showToast(pojo.getMessage());
                                Bundle bundle=new Bundle();
                                bundle.putString("OTP", String.valueOf(pojo.getOtp()));
                                bundle.putString("mobile",MobileNumber);
                                Log.e(TAG,MobileNumber);
                            setFragmentsWithBundle(R.id.frameLayout, new signUp_two(), bundle,true);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        displayErrorDialog("Error", pojo.getStatus());
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


}
