package com.sravan.efactorapp.utils;

import android.util.Log;


import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.Enum.ThingReachability;
import com.sravan.efactorapp.Event.EventType;
import com.sravan.efactorapp.Event.Message;
import com.sravan.efactorapp.Model.MYDEVICESPOJO;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Device;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DataHandler {
    private static final String TAG = DataHandler.class.getSimpleName();
    private static DataHandler instance = null;
    private List<Gateway> gatewayList;
    private boolean isNew = false;
    private boolean isRegistered = false;
    private LocalRPICommunication localRPICommunication;

    public static void init(boolean isNew2) {
        if (instance == null) {
            instance = new DataHandler(isNew2);
        }
    }

    public static DataHandler getInstance() {
        return instance;
    }

    private DataHandler(boolean isNew2) {
        this.isNew = isNew2;
        this.gatewayList = null;
        this.localRPICommunication = new LocalRPICommunication(5);
    }

    public void register() {
        if (!this.isRegistered) {
            this.isRegistered = true;
            App.getEventBus().register(this);
        }
    }

    public void unregister() {
        this.isRegistered = false;
        App.getEventBus().unregister(this);
       /* if (this.isNew) {
            NewGatewayCommHandler.getInstance().disconnect();
        } else {
            GatewayCommHandler.getInstance().disconnect();
        }*/
    }

    public void updateGatewayList() {
        this.gatewayList = DatabaseHandler.getInstance().getGateways();
        List<Gateway> list = this.gatewayList;
        if (list != null && list.size() > 0) {
           /* if (this.isNew) {
                NewGatewayCommHandler.getInstance().connect();
            } else {
                GatewayCommHandler.getInstance().connect();
            }*/
        }
    }

    public void disconnect() {
        /*if (this.isNew) {
            NewGatewayCommHandler.getInstance().disconnect();
        } else {
            GatewayCommHandler.getInstance().disconnect();
        }*/
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(Message message) {
        switch (message.getEventType()) {
            case CLOUD_CONNECTED:
                Log.d(TAG, "Cloud Connected");
                return;
            case CLOUD_DISCONNECTED:
                Log.d(TAG, "Cloud Disconnected");
                return;
            case CLOUD_GATEWAY_DATA_RECEIVED:
                String str = TAG;
                Log.d(str, "Gateway Data Received: " + message.getJsonData());
                try {
                    JSONObject jsonObject = message.getJsonData();
                    if (jsonObject.getJSONObject("state").getJSONObject("reported").getInt("connected") == 0) {
                        ReachabilityHandler.getInstance().removeThingReachability(jsonObject.getString("gateway_id"), ThingReachability.CLOUD);
                        return;
                    } else {
                        ReachabilityHandler.getInstance().addThingReachability(jsonObject.getString("gateway_id"), ThingReachability.CLOUD);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            case GATEWAY_UPDATE:
                Log.d(TAG, "Gateway Updated");
                /*if (this.isNew) {
                    NewGatewayCommHandler.getInstance().disconnect();
                } else {
                    GatewayCommHandler.getInstance().disconnect();
                }*/
                updateGatewayList();
                return;
            case CLOUD_DEVICE_DATA_RECEIVED:
                try {
                    String str2 = TAG;
                    Log.d(str2, "Cloud Device Data Received: " + message.getJsonData());
                    JSONObject jsonObject2 = message.getJsonData();
                    if (jsonObject2.getString("event_type").equals("DEVICE_STATUS")) {
                        Device device = DatabaseHandler.getInstance().getDevice(jsonObject2.getString("device_id"));
                        device.setStatus_data(jsonObject2.getString("device_status"));
                        device.setStatus_updated_at(Utilities.getEpoch());
                        DatabaseHandler.getInstance().updateDevice(device);
                        App.getEventBus().post(new Message(EventType.DEVICE_DATA_UPDATED));
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    return;
                }
            case LOCAL_DEVICE_DATA_RECEIVED:
                try {
                    String str3 = TAG;
                    Log.d(str3, "Local Device Data Received: " + message.getJsonData());
                    JSONObject jsonObject3 = message.getJsonData();
                    Device device2 = DatabaseHandler.getInstance().getDevice(jsonObject3.getString("address"));
                    device2.setStatus_data(jsonObject3.getString(MqttServiceConstants.PAYLOAD));
                    device2.setStatus_updated_at(Utilities.getEpoch());
                    DatabaseHandler.getInstance().updateDevice(device2);
                    return;
                } catch (Exception e3) {
                    return;
                }
            default:
                return;
        }
    }

    /*public void setDeviceStatus(Device device, String status) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command_type", "SET_DEVICE_STATUS");
            jsonObject.put(MqttServiceConstants.PAYLOAD, status);
            jsonObject.put("address", device.getDevice_id());
            String str = TAG;
            Log.d(str, "Send Data: " + jsonObject.toString());
            Log.d(str, "isThingReachable : " + device.getGateway_id()+"\n"+ ThingReachability.LOCAL_WIFI);
            Log.d(str, "isThingReachable : " + ReachabilityHandler.getInstance().isThingReachable(device.getGateway_id(), ThingReachability.LOCAL_WIFI));

            Log.d(str+"21", String.valueOf(ReachabilityHandler.getInstance().isThingReachable("3684680538cf4d4c9ddb73c4ecefb03e",ThingReachability.LOCAL_WIFI)));

            if (ReachabilityHandler.getInstance().isThingReachable(device.getGateway_id(), ThingReachability.LOCAL_WIFI))
            {


                //this.localRPICommunication.setDeviceStatus(ReachabilityHandler.getInstance().getLocalIP(DatabaseHandler.getInstance().getGateway(device.getGateway_id()).getMac()), jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk.INSTANCE, new Consumer() {
                    Gateway gateway = null;
                    //      this.localRPICommunication.setDeviceStatus(ReachabilityHandler.getInstance().getLocalIP(gateway.getMac()), jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk.INSTANCE, new Consumer() {
                         this.localRPICommunication.setDeviceStatus("192.168.1.5", jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk.INSTANCE, new Consumer() {
                             Gateway gateway = null;



                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {

                        try {
                            DataHandler.this.lambda$setDeviceStatus$1$DataHandler(device, jsonObject, obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } else if (!ReachabilityHandler.getInstance().isThingReachable(device.getGateway_id(), ThingReachability.CLOUD)) {
                smartapp.getEventBus().post(new Message(EventType.DEVICE_COMMAND_SEND_FAILED));
            } else if (this.isNew) {
                NewGatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
            } else {
                GatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
            }
        } catch (Exception e) {
        }
    }*/
    public void setDeviceStatus(MYDEVICESPOJO.DeviceDATA device, String status) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command_type", "SET_DEVICE_STATUS");
            jsonObject.put(MqttServiceConstants.PAYLOAD, status);
            jsonObject.put("address", device.getDeviceId());
            String str = TAG;
            Log.d(str, "Send Data: 2 " + ReachabilityHandler.getInstance().getLocalIP(DatabaseHandler.getInstance().getGateway(device.getGatewayId()).getMac()));

            Log.d(str, "Send Data: " + jsonObject.toString());
            if (ReachabilityHandler.getInstance().isThingReachable(device.getGatewayId(), ThingReachability.LOCAL_WIFI)) {

                this.localRPICommunication.setDeviceStatus(ReachabilityHandler.getInstance().getLocalIP(DatabaseHandler.getInstance().getGateway(device.getGatewayId()).getMac()), jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk.INSTANCE, new Consumer() {

                    //this.localRPICommunication.setDeviceStatus("192.168.1.5", jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk.INSTANCE, new Consumer() {

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {

                        try {
                            DataHandler.this.lambda$setDeviceStatus$1$DataHandler(device, jsonObject, obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } else if (!ReachabilityHandler.getInstance().isThingReachable(device.getGatewayId(), ThingReachability.CLOUD)) {
                App.getEventBus().post(new Message(EventType.DEVICE_COMMAND_SEND_FAILED));
            } /*else if (this.isNew) {
                NewGatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
            } else {
                GatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
            }*/
        } catch (Exception e) {
        }
    }

    public /* synthetic */ void lambda$setDeviceStatus$1$DataHandler(MYDEVICESPOJO.DeviceDATA device, JSONObject jsonObject, Object throwable) throws Exception {
        String str = TAG;
        Log.d(str, "Error: " + throwable);
        ReachabilityHandler.getInstance().removeThingReachability(device.getGatewayId(), ThingReachability.LOCAL_WIFI);
        if (!ReachabilityHandler.getInstance().isThingReachable(device.getGatewayId(), ThingReachability.CLOUD)) {
            App.getEventBus().post(new Message(EventType.DEVICE_COMMAND_SEND_FAILED));
        } /*else if (this.isNew) {
            NewGatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
        } else {
            GatewayCommHandler.getInstance().updateDeviceData(device.getGateway_id(), jsonObject.toString());
        }*/
    }

    public void getDeviceStatusLocally() {
        Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                try {
                    DataHandler.this.lambda$getDeviceStatusLocally$2$DataHandler(singleEmitter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public /* synthetic */ void lambda$getDeviceStatusLocally$2$DataHandler(SingleEmitter emitter) throws Exception {
        JSONArray jsonArray;
        Device tempDevice;
        List<Gateway> gatewayList2 = DatabaseHandler.getInstance().getGateways(false);
        if (gatewayList2 != null) {
            for (Gateway gateway : gatewayList2) {
                if (!ReachabilityHandler.getInstance().isThingReachable(gateway.getGateway_id(), ThingReachability.CLOUD)) {
                    if (ReachabilityHandler.getInstance().isThingReachable(gateway.getGateway_id(), ThingReachability.LOCAL_WIFI)) {
                        String ip = ReachabilityHandler.getInstance().getLocalIP(gateway.getMac());
                        for (Device device : DatabaseHandler.getInstance().getDevices(gateway.getGateway_id())) {
                            JSONObject jsonObject = this.localRPICommunication.getAllDeviceStatus(ip, device.getDevice_id());
                            String str = TAG;
                            Log.d(str, "Local Status: " + jsonObject);
                            if (!(jsonObject == null || !jsonObject.has(MqttServiceConstants.PAYLOAD) || (jsonArray = jsonObject.getJSONArray(MqttServiceConstants.PAYLOAD)) == null)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject _temp = jsonArray.getJSONObject(i);
                                    String key = _temp.keys().next();
                                    if ((_temp.getString(key) != null || _temp.getString(key).length() > 0) && (tempDevice = DatabaseHandler.getInstance().getDevice(key)) != null && _temp.getString(key) != null && _temp.getString(key).length() > 1) {
                                        tempDevice.setStatus_data(_temp.getString(key));
                                        tempDevice.setStatus_updated_at(Utilities.getEpoch());
                                        DatabaseHandler.getInstance().updateDevice(tempDevice);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            App.getEventBus().post(new Message(EventType.DEVICE_DATA_UPDATED));
        }
    }
}

