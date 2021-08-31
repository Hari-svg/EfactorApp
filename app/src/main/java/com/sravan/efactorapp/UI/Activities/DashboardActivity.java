package com.sravan.efactorapp.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.sravan.efactorapp.R;
import com.sravan.efactorapp.UI.Fragments.HomeFragment;
import com.sravan.efactorapp.utils.DataHandler;
import com.sravan.efactorapp.utils.FragmentRequestType;
import com.sravan.efactorapp.utils.OnFragmentInteractionListener;

public class DashboardActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

    }


}