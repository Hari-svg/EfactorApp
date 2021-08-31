package com.sravan.efactorapp.Base;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sravan.efactorapp.Interfaces.AdapterClickListener;


/**
 * Created by sravan on 6/12/2019.
 */

public abstract class BaseRecycleAdapter extends RecyclerView.Adapter<BaseRecycleAdapter.ViewHolder>
    implements AdapterClickListener
{

    private View view;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        view=layoutInflater.inflate(getLayoutResourceView(viewType), parent, false);
        return setViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return getItemSize();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    protected abstract int getLayoutResourceView(int viewType);

    protected abstract int getItemSize();

    protected int getViewType(int position){
        return 0;
    }

    protected abstract ViewHolder setViewHolder(int viewType, View view);


    protected View findViewByIds(int resId){
        return view.findViewById(resId);
    }


    public abstract class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener

    {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }

        public abstract void setData(int position);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onAdapterClickListener(int position) {

    }

    @Override
    public void onAdapterClickListener(int position, String action) {

    }
}
