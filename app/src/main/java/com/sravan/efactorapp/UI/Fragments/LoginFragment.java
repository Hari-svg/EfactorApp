package com.sravan.efactorapp.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.UserLogin;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.ApiIds;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Activities.DashboardActivity;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.ConnectionDetector;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginFragment extends BaseFragment implements ApiHitListener {
    private TextView sign_up, forgot_password;
    private EditText et_username, et_password;
    private Button bt_signIn;
    private String TAG = LoginFragment.class.getSimpleName();
    private SessionManager session_management;

    @Override
    protected int getLayoutResourceView() {
        return R.layout.loginfragment;
    }

    @Override
    protected void findView() {
        et_username = (EditText) findViewByIds(R.id.et_email);
        et_password = (EditText) findViewByIds(R.id.et_password);
        forgot_password = (TextView) findViewByIds(R.id.tv_forgotpass);
        sign_up = (TextView) findViewByIds(R.id.tv_register);
        bt_signIn = (Button) findViewByIds(R.id.bt_login);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*getToolBar().setTitleTextTB(getString(R.string.login));
        getToolBar().setbackButtonVisibiltyTB(false);*/
        getToolBar().setToolbarVisibilityTB(false);

    }

    @Override
    protected void init() {
        
        session_management = new SessionManager(getContext());
        sign_up.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        bt_signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.tv_forgotpass:
                //  MyConstant.OTP_type = "F";
                Bundle bundle = new Bundle();
                bundle.putString("type", "F");
                //  setFragmentsWithBundle(R.id.frameLayout, new ConfirmEmailFragment(), bundle, true);
                break;
            case R.id.bt_login:
                hideKeyPad();
                onLogin();
                break;

            case R.id.tv_register:
                // MyConstant.OTP_type = "V";
                onSignUp();
                break;

            default:
                break;
        }
    }

    private void onSignUp() {

            setFragments(R.id.frameLayout,new signUp_one(),true);
    }

    private void onLogin() {
        String email = et_username.getText().toString().trim();
        String pass = et_password.getText().toString().trim();
        if (email.matches("")) {
            Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.matches("")) {
            Toast.makeText(getContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6 || pass.length() > 15) {
            Toast.makeText(getContext(), "Password should contain 6 to 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        //doRequest(email, MyConstant.Loginfrom, pass, getDeviceID(), getDeviceToken(), MyConstant.Device_TYPE, "");
        if (ConnectionDetector.isNetAvail(getContext())) {
            doRequest(email, pass);
        } else {
            showToast("No Internet Connection");

        }
    }

    private void doRequest(String email, String pass) {
        // Toast.makeText(getContext(), ""+loginfrom, Toast.LENGTH_SHORT).show();
        displayProgressBar(false);
        RestClient restClient = new RestClient(getContext());
        restClient.callback(this).userLogin(email, pass);
    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {
        dismissProgressBar();
        if (apiId == ApiIds.ID_USER_LOGIN) {
            if (response.isSuccessful()) {
                try {
                    String s = response.body().string();
                    Gson gson = new Gson();
                    //TODO
                    UserLogin login_pojo = gson.fromJson(s, UserLogin.class);
                    if (login_pojo.getStatus().equals("success")) {
                        Log.e(TAG, "success: ");
                        try {
                            Log.e(TAG, "Res USER" + login_pojo.getUserid());
                            session_management.createLoginSession(login_pojo.getUserid(),login_pojo.getFirstName(),login_pojo.getEmail(),et_password.getText().toString(), login_pojo.getPhone());

                            setFragments(R.id.frameLayout, new HomeFragment(), true);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        displayErrorDialog("Error", login_pojo.getStatus());
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
