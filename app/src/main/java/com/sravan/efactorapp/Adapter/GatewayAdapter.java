package com.sravan.efactorapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sravan.efactorapp.Base.AdapterClickListener;
import com.sravan.efactorapp.Base.BaseRecycleAdapter;

import com.sravan.efactorapp.Model.GATEWAYPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.RestClient.ApiHitListener;
import com.sravan.efactorapp.RestClient.RestClient;
import com.sravan.efactorapp.UI.Fragments.MyGateways;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class GatewayAdapter extends BaseRecycleAdapter {
    FragmentActivity activity;
    List<GATEWAYPOJO.Gateway> gatewayList;
    private AdapterClickListener clickListener;


    public GatewayAdapter(AdapterClickListener clickListener, List<GATEWAYPOJO.Gateway> gatewayList) {
        this.activity = activity;
        this.gatewayList = gatewayList;
        this.clickListener=clickListener;

    }

    @Override
    public int getItemCount() {
        return gatewayList.size();
    }

    @Override
    protected int getLayoutResourceView(int viewType) {
        return R.layout.layout_single_gateway;
    }

    @Override
    protected int getItemSize() {
        return gatewayList.size();
    }

    @Override
    protected ViewHolder setViewHolder(int viewType, View view) {
        return new Holder(view);
    }


    public class Holder extends ViewHolder {

        private TextView roomname,gateway_id;
        private CardView card_room;
        private ImageView view, delete,edit_gateway;
        private int pos;

        public Holder(@NonNull View itemView) {
            super(itemView);
            roomname = itemView.findViewById(R.id.gateway_name);
            gateway_id = itemView.findViewById(R.id.gateway_id);
            view = itemView.findViewById(R.id.gateway_image);
            delete = itemView.findViewById(R.id.delete_gateway);
            edit_gateway = itemView.findViewById(R.id.edit_gateway);
            delete.setOnClickListener(this);
            edit_gateway.setOnClickListener(this);

        }


        @Override
        public void setData(int position) {
            pos = position;
            roomname.setText(gatewayList.get(position).getGatewayName());
            gateway_id.setText(gatewayList.get(position).getGatewayId());
            view.setBackgroundResource(R.drawable.logo);
            delete.setOnClickListener(this);
            edit_gateway.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

            switch (v.getId()) {

                case R.id.delete_gateway:
                    //clickListener.onAdapterClickListener(pos);
                    clickListener.onAdapterClickListener(pos, "Delete");
                    break;
                case R.id.edit_gateway:
                    //clickListener.onAdapterClickListener(pos);
                    clickListener.onAdapterClickListener(pos, "Edit");
                    break;
            }
        }
    }
}