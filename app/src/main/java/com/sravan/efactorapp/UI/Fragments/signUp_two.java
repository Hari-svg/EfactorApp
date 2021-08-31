package com.sravan.efactorapp.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class signUp_two extends BaseFragment {
    private static final String ARG_PARAM = "OTP";
    private static final String MOBILE = "mobile";
    private String  MOBILENUMBER;
    private String OTP;
    private static String TAG = signUp_two.class.getSimpleName();
    private EditText editTextCode;
    private RestClient restClient;

    @Override
    protected int getLayoutResourceView() {
        return R.layout.activity_verify_phone;
    }

    public static signUp_two newInstance(int param1,String param2) {
        signUp_two fragment = new signUp_two();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param1);
        args.putString(MOBILE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void findView() {
        if (getArguments() != null) {
            OTP = getArguments().getString(ARG_PARAM);
            MOBILENUMBER=getArguments().getString(MOBILE);
            String str = TAG;
            Log.d(str, "OTP" + OTP);
            Log.d(str, "MOBILE" + MOBILENUMBER);
        }
        editTextCode = (EditText) findViewByIds(R.id.editTextCode);
        editTextCode.setText(OTP);


    }

    @Override
    protected void init() {
        restClient = new RestClient(getContext());
        if (!editTextCode.getText().toString().isEmpty()) {
            displayProgressBar(false);
            restClient.callback(this).VERIFY_OTP(OTP,MOBILENUMBER);
        }

    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_VERIFY_OTP) {
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
                            Bundle bundle=new Bundle();
                            bundle.putString("mobile",MOBILENUMBER);
                             setFragmentsWithBundle(R.id.frameLayout, new signUp_three(), bundle,true);
                            Log.e(TAG, "SignUp-2" + pojo.getMessage());


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
