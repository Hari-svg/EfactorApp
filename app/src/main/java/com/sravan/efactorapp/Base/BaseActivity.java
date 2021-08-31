package com.sravan.efactorapp.Base;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.BuildConfig;
import com.sravan.efactorapp.Interfaces.NavigationViewHandlerInterface;
import com.sravan.efactorapp.Interfaces.ToolbarHandlerInterface;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ubuntu on 6/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationViewHandlerInterface,
        ToolbarHandlerInterface, ApiHitListener {

    private Dialog alertDialogProgressBar;
    private AlertDialog mErrorDialog;
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceView());
        findView();
        init();
    }

    protected abstract int getLayoutResourceView();

    protected abstract void findView();

    protected abstract void init();

    /**
     * Create a view
     *
     * @param id  view id
     * @param pak view package
     * @return view
     */

    protected View findViewById(String id, String pak) {
        return findViewById(getResources().getIdentifier(id, null, pak));
    }

    /**
     * Call Next Activity using Intent without {@link #finish()}
     *
     * @param aClass next activity
     */
    protected void StartActivity(Class aClass) {

        startActivity(new Intent(this, aClass));
    }

    /**
     * Call next Activity to send data
     *
     * @param aClass
     * @param bundle
     */
    protected void StartACtivityWithData(Class aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void StartACtivityWithFinishWithData(Class aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /**
     * Call Next Activity using Intent with {@link #finish()}
     *
     * @param aClass next activity
     */
    protected void StartActivityWithFinish(Class aClass) {
        Intent intent = new Intent(this, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void setFragments(int resourceView, Fragment fragment, boolean backStackFlag) {
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (backStackFlag)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.replace(resourceView, fragment, fragment.getClass().getSimpleName());
        try {
            transaction.commit();
            // transaction.commitNowAllowingStateLoss();
        } catch (Exception e) {
            // Log.e(TAG, "exception: "+e.getMessage() );
        }
    }
    protected void setFragmentsWithBundle(int resourceView, Fragment fragment, Bundle bundle, boolean backStackFlag) {
        fragment.setArguments(bundle);
        androidx.fragment.app.FragmentManager fragmentManager = this.getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (backStackFlag)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.replace(resourceView, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        //  transaction.commitNowAllowingStateLoss();
//       transaction.commit();
    }
    @Override
    public void onBackPressed() {
        int couunt = getSupportFragmentManager().getBackStackEntryCount();
        androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();

        if (couunt > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    protected void clearCompleteBackStack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * #implements of OnClick Listener
     *
     * @param v click on view
     */
    @Override
    public void onClick(View v) {

    }


    @Override
    public void setNavTitle(String title) {

    }

    @Override
    public void setNavToggleButtonVisibilty(boolean visibilty) {

    }

    @Override
    public void setBackButtonVisibilty(boolean visibilty) {

    }

    @Override
    public void setNavigationToolbarVisibilty(boolean visibilty) {

    }

    @Override
    public void setHeaderProfilePic(String uri, Drawable drawable) {

    }

    @Override
    public void setUserName(String Name) {

    }

    @Override
    public void setTitleTextTB(String title) {

    }

    @Override
    public void setTitleButtonVisibiltyTB(boolean visibility) {

    }

    @Override
    public void setToolbarVisibilityTB(boolean visibility) {

    }

    @Override
    public void setbackButtonVisibiltyTB(boolean visibility) {

    }

    public void showLog(String TAG, Object msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, ": " + msg);
        }
    }

    @Override
    public void lockDrawer(boolean visibilty) {

    }


    public void onNaviProfileClickedNav() {

    }

    @Override
    public void setLocationTitle(String Name) {

    }

    @Override
    public void setCurrentLocation(String Name) {

    }

    @Override
    public void setLocationComponentVisibilty(boolean visibilty) {

    }

    @Override
    public void setTitleButtonVisibility(boolean visibilty) {

    }

    public void displayProgressBar(boolean isCancellable) {
        alertDialogProgressBar = new Dialog(BaseActivity.this,
                R.style.YourCustomStyle);
        alertDialogProgressBar.setCancelable(false);
        alertDialogProgressBar
                .requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogProgressBar.setContentView(R.layout.progress_dialog);

        alertDialogProgressBar.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        alertDialogProgressBar.show();
    }

    public void dismissProgressBar() {
        if (alertDialogProgressBar != null) {
            alertDialogProgressBar.dismiss();
        }
    }

    public void displayErrorDialog(String title, String content) {
        mErrorDialog = new AlertDialog.Builder(BaseActivity.this)
                .setTitle(title)
                .setMessage(content)
                .setIcon(ContextCompat.getDrawable(BaseActivity.this, R.mipmap.ic_launcher_round))
                .setCancelable(false)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        mErrorDialog.show();
    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {

    }

    @Override
    public void onFailResponse(int apiId, String error) {

    }

    public App getMyApplication() {
        return (App) getApplication();
    }

    public BaseFragment getLatestFragment() {

        androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null && fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    public void clearBackStack(String name) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            try {
                getSupportFragmentManager().popBackStack
                        (name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
            }
        }
    }
}
