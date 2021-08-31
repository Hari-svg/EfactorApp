package com.sravan.efactorapp.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.sravan.efactorapp.Base.BaseActivity;
import com.sravan.efactorapp.Base.BaseFragment;
import com.sravan.efactorapp.Model.ROOMINFOPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.UI.Fragments.Device.MyDevices;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.Holder> {



    public interface OnItemClickListener {
        void onItemClick(ROOMINFOPOJO.Room item);
    }
    FragmentActivity activity;
    List<ROOMINFOPOJO.Room> roomList;
    OnItemClickListener listener;
    public RoomAdapter(FragmentActivity activity, List<ROOMINFOPOJO.Room> roomList) {
        this.activity=activity;
        this.roomList=roomList;
    }


    @NonNull
    @Override
    public RoomAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_room, parent, false);
    return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.Holder holder, int position) {
        holder.roomname.setText(roomList.get(position).getRoomname());
        holder.view.setBackgroundResource(R.drawable.bedroom1);
       holder.card_room.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FragmentManager fragmentManager = activity.getSupportFragmentManager();
               FragmentTransaction transaction = fragmentManager.beginTransaction();
               Bundle bundle=new Bundle();
               bundle.putString("ROOMID",roomList.get(position).getId());
               bundle.putString("GATEWAYID",roomList.get(position).getGatewayId());
               transaction.replace(R.id.frameLayout, MyDevices.class,bundle);
               transaction.commit();
           }
       });

        holder.edit_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("ROOMID",roomList.get(position).getId());
                bundle.putString("GATEWAYID",roomList.get(position).getGatewayId());
                transaction.replace(R.id.frameLayout, MyDevices.class,bundle);
                transaction.commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView roomname;
        private CardView card_room;
        private ImageView view,edit_room;
        public Holder(@NonNull View itemView) {
            super(itemView);
            roomname=itemView.findViewById(R.id.room_name);
            card_room=itemView.findViewById(R.id.room_card);
            view=itemView.findViewById(R.id.imageView);
            edit_room=itemView.findViewById(R.id.edit_room);
        }
    }
}
