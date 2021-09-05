package com.sravan.efactorapp.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sravan.efactorapp.Model.MYDEVICESPOJO;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.UI.Fragments.Device.DeviceUtil;
import com.sravan.efactorapp.utils.DataHandler;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceItemHolder> {
    private static final String TAG = "Device list Adapter";
    private List<MYDEVICESPOJO.DeviceDATA> deviceDataList;
    private OnDeviceClickListener onDeviceClickListener;

    public DeviceListAdapter(FragmentActivity activity, List<MYDEVICESPOJO.DeviceDATA> deviceDataList,OnDeviceClickListener onDeviceClickListener2) {

        this.deviceDataList=deviceDataList;
        this.onDeviceClickListener=onDeviceClickListener;
    }

    public interface OnDeviceClickListener {
        void OnDeviceClick(MYDEVICESPOJO.DeviceDATA device, int i);

        void OnSwitchClick(MYDEVICESPOJO.DeviceDATA device, boolean z);
    }

   /* public DeviceListAdapter(OnDeviceClickListener onDeviceClickListener2) {
        this.onDeviceClickListener = onDeviceClickListener2;
        if (this.deviceDataList == null) {
            this.deviceDataList = Collections.emptyList();
        }

    }*/

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DeviceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device_list_item, parent, false), this.onDeviceClickListener);
    }

    public void onBindViewHolder(DeviceItemHolder holder, int position) {
        holder.textViewName.setText(this.deviceDataList.get(position).getDeviceName());
        //ChangeConnectionMode(false, deviceDataList.get(position));
        String type = this.deviceDataList.get(position).getDeviceType();
        Log.e("Device ListAdapter : ", "Type : " + type + "\n" + "GetPower :" + deviceDataList.get(0).getDeviceStatusData());
        holder.textViewType.setText(DeviceUtil.getTypeStr("Device")/*"FAN"*/);
        int st = DeviceUtil.getPowerStatus("Device", "0");/*0;*/
        Switch r2 = holder.switchPower;
        boolean z = true;
        if (st != 1) {
            z = false;
        }
        r2.setChecked(z);

        //Speed control
        // holder.speed_control.setProgress();

        holder.speed_control.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

                //Toast.makeText(r2.getContext(), "Indicator 1 : " + seekParams.tickText, Toast.LENGTH_SHORT).show();
                //    String device=itemList.get(position);
                int n = position;
                SpeedControlFunction(Integer.parseInt(seekParams.tickText), deviceDataList.get(n));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                // Toast.makeText(r2.getContext(), "Indicator 2 : "+seekBar.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                //Toast.makeText(r2.getContext(), "Indicator 3 : "+seekBar.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Mode change

        holder.switchmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                ChangeConnectionMode(status, deviceDataList.get(position));
            }
        });
        holder.switchPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                POERSWITCH(status, deviceDataList.get(position));
            }
        });

    }

    private void POERSWITCH(boolean status, MYDEVICESPOJO.DeviceDATA device) {
        String PayloadCommand = "";
        if (status == true) {
            PayloadCommand = "01005508A1C2000000016C";
            DataHandler.getInstance().setDeviceStatus(device, PayloadCommand);

        } else {
            //TODO Change command here
            PayloadCommand = "01005508A1C2000000006B";
            DataHandler.getInstance().setDeviceStatus(device, PayloadCommand);
        }
    }

    private void ChangeConnectionMode(boolean status, MYDEVICESPOJO.DeviceDATA device) {
        String PayloadCommand = "";
        if (status == true) {
            PayloadCommand = "01005508FDD000000000D5";
            DataHandler.getInstance().setDeviceStatus(device, PayloadCommand);

        } else {
            //TODO Change command here
            PayloadCommand = "01005508AD0100000000B6";
            DataHandler.getInstance().setDeviceStatus(device, PayloadCommand);
        }
    }


    private void SpeedControlFunction(int speed, MYDEVICESPOJO.DeviceDATA device) {

        String PayloadCommand = null;
        if (speed == 0) {
            PayloadCommand = "01005508A1C2000000006B";
        } else if (speed == 1) {
            PayloadCommand = "01005508A1C0000000016A";
        } else if (speed == 2) {
            PayloadCommand = "01005508A1C0000000026B";
        } else if (speed == 3) {
            PayloadCommand = "01005508A1C0000000036C";
        } else if (speed == 4) {
            PayloadCommand = "01005508A1C0000000046D";
        } else if (speed == 5) {
            PayloadCommand = "01005508A1C0000000056E";
        }
        DataHandler.getInstance().setDeviceStatus(device, PayloadCommand);

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.deviceDataList.size();
    }

    public void update() {
        this.deviceDataList = deviceDataList;
        if (this.deviceDataList == null) {
            this.deviceDataList = Collections.emptyList();
        }

        notifyDataSetChanged();
    }

    public class DeviceItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout reverseLayout, LED_off, container;
        private OnDeviceClickListener onDeviceClickListener;
        private IndicatorSeekBar speed_control;
        private Switch switchPower, switchmode;
        private TextView textViewName;
        private TextView textViewType;
        private CircleImageView led_white, led_pink, led_orange, led_cyan, led_green, led_blue, led_red;

        @SuppressLint("WrongViewCast")
        public DeviceItemHolder(View itemView, OnDeviceClickListener onDeviceClickListener2) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.device_name);
            this.textViewType = (TextView) itemView.findViewById(R.id.textViewType);
            this.switchPower = (Switch) itemView.findViewById(R.id.switchPower);
            this.switchmode = (Switch) itemView.findViewById(R.id.switchMode);
            this.speed_control = (IndicatorSeekBar) itemView.findViewById(R.id.speed_control);
            this.reverseLayout = (LinearLayout) itemView.findViewById(R.id.reverseLayout);
            this.LED_off = (LinearLayout) itemView.findViewById(R.id.LED_off);
            this.container = (LinearLayout) itemView.findViewById(R.id.container);
            this.led_white = (CircleImageView) itemView.findViewById(R.id.led_white);
            this.led_pink = (CircleImageView) itemView.findViewById(R.id.led_pink);
            this.led_orange = (CircleImageView) itemView.findViewById(R.id.led_orange);
            this.led_cyan = (CircleImageView) itemView.findViewById(R.id.led_cyan);
            this.led_green = (CircleImageView) itemView.findViewById(R.id.led_green);
            this.led_blue = (CircleImageView) itemView.findViewById(R.id.led_blue);
            this.led_red = (CircleImageView) itemView.findViewById(R.id.led_red);

            this.onDeviceClickListener = onDeviceClickListener2;

           // this.switchPower.setOnClickListener(this);
            this.switchmode.setOnClickListener(this);
            this.led_white.setOnClickListener(this);
            this.led_pink.setOnClickListener(this);
            this.led_orange.setOnClickListener(this);
            this.led_cyan.setOnClickListener(this);
            this.led_green.setOnClickListener(this);
            this.led_blue.setOnClickListener(this);
            this.led_red.setOnClickListener(this);
            this.LED_off.setOnClickListener(this);
            this.reverseLayout.setOnClickListener(this);
            this.container.setOnClickListener(this);
        }

        public void onClick(View v) {
            int n = getAdapterPosition();
            if (v.getId() == R.id.switchPower) {

                OnDeviceClickListener onDeviceClickListener2 = this.onDeviceClickListener;
               /* if (onDeviceClickListener2 != null) {
                    onDeviceClickListener2.OnSwitchClick((MYDEVICESPOJO.DeviceDATA) DeviceListAdapter.this.deviceDataList.get(n), this.switchPower.isChecked());
                    return;
                }*/
                return;
            } else if (v.getId() == R.id.container) {
              /*  OnDeviceClickListener onDeviceClickListener3 = this.onDeviceClickListener;
                if (onDeviceClickListener3 != null) {
                    onDeviceClickListener3.OnDeviceClick((MYDEVICESPOJO.DeviceDATA) DeviceListAdapter.this.deviceDataList.get(n), n);
                }*/
                return;
            } else if (v.getId() == R.id.led_white) {
                String PayloadCommand = "01005508A1C3000000006C";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_pink) {
                String PayloadCommand = "01005508A1C3000000016D";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_orange) {
                String PayloadCommand = "01005508A1C3000000026E";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_cyan) {
                String PayloadCommand = "01005508A1C30000000470";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_blue) {
                String PayloadCommand = "01005508A1C30000000571";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_red) {
                String PayloadCommand = "01005508A1C3000000036F";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.led_green) {
                String PayloadCommand = "01005508A1C30000000672";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.LED_off) {
                String PayloadCommand = "01005508A1C30000000773";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);

            } else if (v.getId() == R.id.reverseLayout) {
                String PayloadCommand = "01005508A1C0000000066F";
                DataHandler.getInstance().setDeviceStatus(deviceDataList.get(n), PayloadCommand);
            }
        }
    }


}

