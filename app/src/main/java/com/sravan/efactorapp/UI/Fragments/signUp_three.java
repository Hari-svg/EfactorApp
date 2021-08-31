package com.sravan.efactorapp.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.ConditionVariable;
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
import com.sravan.efactorapp.Model.CommonPojo;
import com.sravan.efactorapp.Model.SendOTPPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.utils.ConnectionDetector;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class signUp_three extends BaseFragment {
    private static final String TAG = signUp_three.class.getSimpleName();
    private EditText et_name, et_email, et_password, et_phone;
    private String email, name, phone, password;
    private RestClient restClient;
    private TextView bt_register;
    private String MobileNumber;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mobile";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signUp_three() {
        // Required empty public constructor
    }

    public static signUp_three newInstance(String param1, String param2) {
        signUp_three fragment = new signUp_three();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_sign_up_three;
    }
    @Override
    protected void init() {
        restClient=new RestClient(getContext());


    }
    @Override
    protected void findView() {
        if (!getArguments().equals(null)){
            MobileNumber=getArguments().getString(ARG_PARAM1);
            Log.e(TAG,MobileNumber);
        }
        et_email=(EditText) findViewByIds(R.id.et_email);
        et_name=(EditText) findViewByIds(R.id.et_name);
        et_phone=(EditText) findViewByIds(R.id.et_mobile);
        et_phone.setText(MobileNumber);
        et_password=(EditText) findViewByIds(R.id.et_password);
        bt_register=(TextView) findViewByIds(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isNetAvail(getContext())) {
                    email=et_email.getText().toString().trim();
                    name=et_name.getText().toString().trim();
                    phone=et_phone.getText().toString().trim();
                    password=et_password.getText().toString().trim();
                   if (!email.isEmpty()&&!password.isEmpty()&&!phone.isEmpty()){
                       DoReq(name,email,phone,password);
                   }else {
                       showToast("Please enter all details correctly");
                   }

                } else {
                    showToast("No Internet Connection");

                }
            }
        });
    }

    private void DoReq(String name, String email, String phone, String password) {
        displayProgressBar(false);
        restClient.callback(this).REGISTERUSER(name,email,phone,password);

    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_REGISTER_SUCCESS) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    CommonPojo pojo = gson.fromJson(s, CommonPojo.class);
                    if (pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "Res USER" + pojo.getMessage());
                            showToast(pojo.getMessage());
                           setFragments(R.id.frameLayout,new LoginFragment(),true);

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