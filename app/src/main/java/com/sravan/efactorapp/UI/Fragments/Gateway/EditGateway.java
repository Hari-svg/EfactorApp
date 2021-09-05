package com.sravan.efactorapp.UI.Fragments.Gateway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.UI.Fragments.MyGateways;
import com.sravan.efactorapp.UI.Fragments.Room.EditRoom;

import java.util.ArrayList;
import java.util.List;


public class EditGateway extends BaseFragment {
    private static final String ARG_PARAM = "gatewaylist";
    private static final String ARG_PARAM1 = "id";
    private static final String TAG = EditGateway.class.getSimpleName();
    private List<GATEWAYPOJO.Gateway> GatewayList;
    private EditText et_gatewayName;
    private  String GatewayId;
    private int Position;


    @Override
    protected int getLayoutResourceView() {
        return R.layout.fragment_edit_gateway;
    }

    @Override
    protected void findView() {
        GatewayList = new ArrayList<>();
         if (getArguments() != null) {
            GatewayList = (List<GATEWAYPOJO.Gateway>) getArguments().getSerializable(ARG_PARAM);
            Position=getArguments().getInt(ARG_PARAM1);
            String str = TAG;
            Log.d(str, "Gateway List : " + this.GatewayList.toString());
            Log.e("ID",GatewayList.get(Position).getId());
        }
        et_gatewayName = (EditText) findViewByIds(R.id.et_gatewayName);
        et_gatewayName.setText(GatewayList.get(Position).getGatewayName());

    }

    @Override
    protected void init() {

    }
}