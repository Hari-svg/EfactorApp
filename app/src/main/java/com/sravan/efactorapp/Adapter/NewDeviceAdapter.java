package com.sravan.efactorapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sravan.efactorapp.Model.NewDeviceData;
import com.sravan.efactorapp.R;

import java.util.ArrayList;

public class NewDeviceAdapter extends RecyclerView.Adapter<NewDeviceAdapter.NewDeviceHolder> {
    private ArrayList<NewDeviceData> itemList = new ArrayList<>();
    private OnNewDeviceClickListener onNewDeviceClickListener;

    public interface OnNewDeviceClickListener {
        void onNewDeviceItemClick(NewDeviceData newDeviceData);
    }

    public NewDeviceAdapter(OnNewDeviceClickListener onNewDeviceClickListener2) {
        this.onNewDeviceClickListener = onNewDeviceClickListener2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public NewDeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewDeviceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_device_list_item, parent, false), this.onNewDeviceClickListener);
    }

    public void onBindViewHolder(NewDeviceHolder holder, int position) {
        holder.textViewName.setText(this.itemList.get(position).getApn());
        holder.textViewVer.setText(this.itemList.get(position).getVer());
        holder.setSelected(this.itemList.get(position).isSelected());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public void addItem(NewDeviceData deviceData) {
        this.itemList.add(deviceData);
        notifyDataSetChanged();
    }

    public class NewDeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnNewDeviceClickListener onNewDeviceClickListener;
        public TextView textViewName;
        public TextView textViewVer;

        public NewDeviceHolder(View itemView, OnNewDeviceClickListener onNewDeviceClickListener2) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVer = (TextView) itemView.findViewById(R.id.textViewVer);
            this.onNewDeviceClickListener = onNewDeviceClickListener2;
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int n = getAdapterPosition();
            ((NewDeviceData) NewDeviceAdapter.this.itemList.get(n)).setSelected(true);
            for (int i = 0; i < NewDeviceAdapter.this.itemList.size(); i++) {
                if (i != n) {
                    ((NewDeviceData) NewDeviceAdapter.this.itemList.get(i)).setSelected(false);
                }
            }
            OnNewDeviceClickListener onNewDeviceClickListener2 = this.onNewDeviceClickListener;
            if (onNewDeviceClickListener2 != null) {
                onNewDeviceClickListener2.onNewDeviceItemClick((NewDeviceData) NewDeviceAdapter.this.itemList.get(n));
            }
            NewDeviceAdapter.this.notifyDataSetChanged();
        }

        public void setSelected(boolean selected) {
            if (selected) {
                this.itemView.setBackgroundColor(this.itemView.getContext().getColor(R.color.green));
                this.textViewName.setTextColor(this.itemView.getContext().getColor(R.color.white));
                this.textViewVer.setTextColor(this.itemView.getContext().getColor(R.color.white));
                return;
            }
            this.itemView.setBackgroundColor(this.itemView.getContext().getColor(R.color.white));
            this.textViewName.setTextColor(this.itemView.getContext().getColor(R.color.black));
            this.textViewVer.setTextColor(this.itemView.getContext().getColor(R.color.black));
        }
    }
}

