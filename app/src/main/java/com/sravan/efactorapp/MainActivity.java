package com.sravan.efactorapp;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sravan.efactorapp.Base.BaseActivity;
import com.sravan.efactorapp.Base.ToolbarHandler;
import com.sravan.efactorapp.UI.Activities.DashboardActivity;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.UI.Fragments.LoginFragment;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataHandler;
import com.sravan.efactorapp.utils.FragmentRequestType;
import com.sravan.efactorapp.utils.OnFragmentInteractionListener;

public class MainActivity extends BaseActivity implements OnFragmentInteractionListener {
    private static final String TAG = DashboardActivity.class.getSimpleName();
    private MainActivity.FragmentName currentFragment;
    private boolean firstTime = true;
    private SessionManager sessionManager;
    public enum FragmentName {
        FRAGMENT_HOME,
        FRAGMENT_GATEWAYS,
        FRAGMENT_DEVICE_SETTINGS,
        FRAGMENT_GATEWAY_SETTINGS,
        FRAGMENT_USER_SETTINGS,
        FRAGMENT_OTHERS
    }
    private ToolbarHandler toolbarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceView() {
        return R.layout.activity_main;
    }

    @Override
    protected void findView() {
        toolbarHandler=new ToolbarHandler(this);
        toolbarHandler.findViews();
        DataHandler.init(false);
        DataHandler.getInstance().register();
        sessionManager=new SessionManager(this);
        getSupportActionBar().hide();
//        DataHandler.getInstance().updateGatewayList();
        //gotoHomeScreen();
        this.firstTime = false;

    }

    @Override
    protected void init() {
        if (sessionManager.isLoggedIn()){
            gotoHomeScreen();
        }else {
        setFragments(R.id.frameLayout, new LoginFragment(), false);
    }
    }

    private void gotoHomeScreen() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        replaceFragment(new HomeFragment(), true);
        this.currentFragment = FragmentName.FRAGMENT_HOME;
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //  fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.toString());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(FragmentRequestType fragmentRequestType, Bundle bundle) {

    }
}