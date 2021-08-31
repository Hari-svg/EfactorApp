package com.sravan.efactorapp.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sravan.efactorapp.App.App;

import com.sravan.efactorapp.Interfaces.AdapterClickListener;
import com.sravan.efactorapp.Interfaces.NavigationViewHandlerInterface;
import com.sravan.efactorapp.Interfaces.ToolbarHandlerInterface;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.UI.Activities.DashboardActivity;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by ubuntu on 6/12/17.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AdapterClickListener,
        SwipeRefreshLayout.OnRefreshListener, ApiHitListener {

    private static final String TAG = "BaseFragment";
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private View view;
    private NavigationViewHandlerInterface navHandlerInterface;
    private ToolbarHandlerInterface toolbarHandlerInterface;

    private Dialog alertDialogProgressBar;
    private AlertDialog mErrorDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            /*ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);*/
            return view;
        }
        // try {
        view = inflater.inflate(getLayoutResourceView(), container, false);
        toolbarHandlerInterface = (BaseActivity) getActivity();
        findView();
        init();
           /* } catch (InflateException e) {
                Log.e(TAG, "onCreateView: "+e.getMessage() );
            }*/
        return view;
    }

    //TODO this function commented by -sravan
    /*public App getMyApplication() {
        return ((DashboardActivity) getActivity()).getMyApplication();
    }*/
    protected abstract int getLayoutResourceView();

    /**
     * Simple method i.e. just like a block of all view
     */
    protected abstract void findView();

    /**
     * Simple method block
     */
    protected abstract void init();

    protected View getFragmentView() {
        return view;
    }

    protected NavigationViewHandlerInterface getNaivHandler() {
        return (BaseActivity) getActivity();
    }

    protected ToolbarHandlerInterface getToolBar() {
        return toolbarHandlerInterface;
    }

    protected View findViewByIds(int reSourceId) {
        return view.findViewById(reSourceId);
    }

    protected void StartActivityWithFinish(Class aClass) {
        startActivity(new Intent(getContext(), aClass));
        getActivity().finish();
    }


    protected void setFragments(int resourceView, Fragment fragment, boolean backStackFlag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (backStackFlag)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.replace(resourceView, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        //  transaction.commitNowAllowingStateLoss();
//       transaction.commit();
    }

    protected void setFragmentsWithBundle(int resourceView, Fragment fragment, Bundle bundle, boolean backStackFlag) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (backStackFlag)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.replace(resourceView, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        //  transaction.commitNowAllowingStateLoss();
//       transaction.commit();
    }

    protected void clearCompleteBackStack() {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

   /* public void showLog(String TAG, Object msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "showLog: " + msg);
        }
    }*/

    public void showToast(String msg) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    public void hideKeyPad() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void displayProgressBar(boolean isCancellable) {
        alertDialogProgressBar = new Dialog(getContext(),
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
        if (getContext() != null)
            mErrorDialog = new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setMessage(content)
                    .setIcon(ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher_round))
                    .setCancelable(false)
                    .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        if (mErrorDialog != null)
            mErrorDialog.show();
    }


    @Override
    public void onAdapterClickListener(int position) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onSuccessResponse(int apiId, Response<ResponseBody> response) {

    }

    @Override
    public void onFailResponse(int apiId, String error) {

    }


    @Override
    public void onAdapterClickListener(int position, String action) {

    }
}
