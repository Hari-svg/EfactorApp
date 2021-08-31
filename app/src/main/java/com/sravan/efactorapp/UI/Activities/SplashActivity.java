package com.sravan.efactorapp.UI.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.App.PermissionManager;
import com.sravan.efactorapp.Base.BaseActivity;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.spf.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    AlertDialog.Builder alertDialog;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            SessionManager manager = new SessionManager(getApplicationContext());
            manager.checkLogin(getIntent().getExtras());
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findView() {
            getSupportActionBar().hide();
    }

    @Override
    protected void init() {
        changeStatusBarColor();
        checkPermission();
    }


    private void checkPermission() {
        if (PermissionManager.areExplicitPermissionsRequired()) {
            List<String> required = PermissionManager.isAllPremissiongranted(SplashActivity.this);
            if (required != null && required.size() > 0) {
                PermissionManager.show(SplashActivity.this,
                        getResources().getString(R.string.app_name), required);
            } else {
                goToNextActivity();
            }

        } else {
            goToNextActivity();

        }

    }

    private void goToNextActivity() {
        if (checkGpsStatus()) {
            handler.postDelayed(runnable, 2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionManager.requestRunning = false;
        List<String> requiredPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                requiredPermission.add(permissions[i]);
            }
        }
        if (requiredPermission != null && requiredPermission.size() > 0) {
            PermissionManager.show(SplashActivity.this, "Permission Required", requiredPermission);
        }
    }


    private boolean checkGpsStatus() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(SplashActivity.this);
        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onStop() {
       // ((App) getApplication()).stopLocationService();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*((App) getApplication()).startLocationService(null);
        NotificationInstanceService.getLatestToken(this);
        AppNotificationMessagingService.createNotifiationChhanel(this);*/
        if (!checkGpsStatus()) {
            showSettingsAlert();
        } else {
            checkPermission();
        }
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
