package com.sravan.efactorapp.utils;

import androidx.core.app.NotificationCompat;


import com.sravan.efactorapp.Exceptions.CommunicationErrorException;
import com.sravan.efactorapp.Exceptions.InvalidResponseException;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocalRPICommunication {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType PLAIN_TEXT = MediaType.parse("text/plain");
    private static final int TIME_OUT = 30;
    private String TAG = LocalRPICommunication.class.getSimpleName();
    private OkHttpClient okHttpClient;

    public LocalRPICommunication() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        this.okHttpClient = builder.build();
    }

    public LocalRPICommunication(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout((long) timeout, TimeUnit.SECONDS);
        builder.readTimeout((long) timeout, TimeUnit.SECONDS);
        builder.writeTimeout((long) timeout, TimeUnit.SECONDS);
        this.okHttpClient = builder.build();
    }

    public Single startOnBoarding(String ip, String data) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$startOnBoarding$0$LocalRPICommunication(data, ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$startOnBoarding$0$LocalRPICommunication(String data, String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create(data, JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/onboard").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                } else if (!emitter.isDisposed()) {
                    emitter.onError(new InvalidResponseException());
                }
                return;
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single getWifiStatus(String ip) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$getWifiStatus$1$LocalRPICommunication(ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$getWifiStatus$1$LocalRPICommunication(String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"request\":\"GET\"}", JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/network").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                } else if (!emitter.isDisposed()) {
                    emitter.onError(new InvalidResponseException());
                }
                return;
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single setWifiStatus(String ip, String ssid, String password) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$setWifiStatus$2$LocalRPICommunication(ssid, password, ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$setWifiStatus$2$LocalRPICommunication(String ssid, String password, String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"request\":\"SET\",\"ssid\":\"" + ssid + "\",\"password\":\"" + password + "\"}", JSON);
            Request.Builder builder = new Request.Builder();
            StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(ip);
            sb.append(":54545/network");
            Response response = this.okHttpClient.newCall(builder.url(sb.toString()).post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                } else if (!emitter.isDisposed()) {
                    emitter.onError(new InvalidResponseException());
                }
                return;
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single resetGateway(String ip) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$resetGateway$3$LocalRPICommunication(ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$resetGateway$3$LocalRPICommunication(String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"request\":\"GET\"}", JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/reset").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                } else if (!emitter.isDisposed()) {
                    emitter.onError(new InvalidResponseException());
                }
                return;
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single checkNewDevice(String ip, String address, String payload) {


        return Single.create(new SingleOnSubscribe() {


            // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$checkNewDevice$4$LocalRPICommunication(address, payload, ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$checkNewDevice$4$LocalRPICommunication(String address, String payload, String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"command_type\":\"CHECK_NEW\",\"address\":\"" + address + "\",\"payload\":\"" + payload + "\"}", JSON);
            Request.Builder builder = new Request.Builder();
            StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(ip);
            sb.append(":54545/device");

            Response response = this.okHttpClient.newCall(builder.url(sb.toString()).post(requestBody).build()).execute();

            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {      //200

                    if (!jsonObject.has(MqttServiceConstants.PAYLOAD)) {
                        Thread.sleep(1000);
                        RequestBody requestBody2 = RequestBody.create("{\"command_type\":\"GET_DEVICE_STATUS\", \"address\":\"" + address + "\",\"payload\":\"" + payload + "\"}", JSON);
                        Request.Builder builder2 = new Request.Builder();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("http://");
                        sb2.append(ip);
                        sb2.append(":54545/device");
                        Response response2 = this.okHttpClient.newCall(builder2.url(sb2.toString()).post(requestBody2).build()).execute();
                        if (response2.isSuccessful()) {
                            jsonObject = new JSONObject(response2.body().string());
                            if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) != 200) {
                                throw new InvalidResponseException();
                            }
                        } else {
                            throw new CommunicationErrorException();
                        }
                    }
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                    return;
                }
                throw new InvalidResponseException();
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single getDeviceStatus(String ip, String address, String payload) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$getDeviceStatus$5$LocalRPICommunication(address, payload, ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$getDeviceStatus$5$LocalRPICommunication(String address, String payload, String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"command_type\":\"GET_DEVICE_STATUS\", \"address\":\"" + address + "\",\"payload\":\"" + payload + "\"}", JSON);
            Request.Builder builder = new Request.Builder();
            StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(ip);
            sb.append(":54545/device");
            Response response = this.okHttpClient.newCall(builder.url(sb.toString()).post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                    return;
                }
                throw new InvalidResponseException();
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single updateDeviceList(String ip) {
        return Single.create(new SingleOnSubscribe() {

            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$updateDeviceList$6$LocalRPICommunication(ip, singleEmitter);
            }
        });
    }

    public /* synthetic */ void lambda$updateDeviceList$6$LocalRPICommunication(String ip, SingleEmitter emitter) {
        try {
            RequestBody requestBody = RequestBody.create("{\"command_type\":\"UPDATE_LIST\"}", JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/device").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) == 200) {
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(jsonObject);
                    }
                    return;
                }
                throw new InvalidResponseException();
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }

    public Single setDeviceStatus(String ip, JSONObject jsonBodyObject) {
        return Single.create(new SingleOnSubscribe() {


            @Override // io.reactivex.SingleOnSubscribe
            public final void subscribe(SingleEmitter singleEmitter) {
                LocalRPICommunication.this.lambda$setDeviceStatus$7$LocalRPICommunication(jsonBodyObject, ip, singleEmitter);
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    public /* synthetic */ void lambda$setDeviceStatus$7$LocalRPICommunication(JSONObject jsonBodyObject, String ip, SingleEmitter emitter) {
        Exception e;
        try {
            RequestBody requestBody = RequestBody.create(jsonBodyObject.toString(), JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/device").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) != 200 && !emitter.isDisposed()) {
                    emitter.onError(new InvalidResponseException());
                }
                if (!jsonObject.has(MqttServiceConstants.PAYLOAD)) {
                    Thread.sleep(1000);
                    StringBuilder sb = new StringBuilder();
                    sb.append("{\"command_type\":\"GET_DEVICE_STATUS\", \"address\":\"");
                    try {
                        // sb.append(jsonBodyObject.getString("address"));
                        sb.append("\"}");
                        RequestBody requestBody2 = RequestBody.create(sb.toString(), JSON);
                        Request.Builder builder2 = new Request.Builder();
                        Response response2 = this.okHttpClient.newCall(builder2.url("http://" + ip + ":54545/device").post(requestBody2).build()).execute();
                        if (response2.isSuccessful()) {
                            jsonObject = new JSONObject(response2.body().string());
                            if (jsonObject.getInt(NotificationCompat.CATEGORY_STATUS) != 200 || jsonObject.get(MqttServiceConstants.PAYLOAD) == null) {
                                throw new InvalidResponseException();
                            }
                        } else {
                            throw new CommunicationErrorException();
                        }
                    } catch (Exception e2) {
                        e = e2;
                        if (emitter.isDisposed()) {
                            emitter.onError(e);
                            return;
                        }
                        return;
                    }
                }
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(jsonObject);
                }
                return;
            }
            throw new CommunicationErrorException();
        } catch (Exception e3) {
            e = e3;
            if (emitter.isDisposed()) {
            }
        }
    }

    public JSONObject getAllDeviceStatus(String ip, String address) {
        try {
            RequestBody requestBody = RequestBody.create("{\"command_type\":\"GET_ALL_DEVICE_STATUS\",\"address\":\"" + address + "\"}", JSON);
            Request.Builder builder = new Request.Builder();
            Response response = this.okHttpClient.newCall(builder.url("http://" + ip + ":54545/device").post(requestBody).build()).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string());
            }
            throw new CommunicationErrorException();
        } catch (Exception e) {
            return null;
        }
    }
}

