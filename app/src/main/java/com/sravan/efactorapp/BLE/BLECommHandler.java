package com.sravan.efactorapp.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.jakewharton.rx.ReplayingShare;
import com.polidea.rxandroidble2.NotificationSetupMode;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import io.reactivex.android.schedulers.AndroidSchedulers;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class BLECommHandler {
    private static BLECommHandler instance;
    private final String TAG = BLECommHandler.class.getSimpleName();
    private Observable<RxBleConnection> bleConnectionObservable = null;
    private BluetoothDevice connectedDevice;
    private Context context;
    private PublishSubject<Void> disconnectTriggerSubject;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothManager mBluetoothManager;
    private String Tag;

    public static void init(Context context2) {
        if (instance == null) {
            instance = new BLECommHandler(context2);
        }
    }

    public static BLECommHandler getInstance() {
        return instance;
    }

    private BLECommHandler(Context context2) {
        this.context = context2;
        this.mBluetoothManager = (BluetoothManager) context2.getSystemService(context.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.disconnectTriggerSubject = PublishSubject.create();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return this.mBluetoothAdapter;
    }

    public void disconnect() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public void connect(final BluetoothDevice device, boolean autoconnect) {
        this.mBluetoothGatt = device.connectGatt(this.context, autoconnect, new BluetoothGattCallback() {


            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == 2) {
                    Log.i(BLECommHandler.this.TAG, "Connected to BLE Device");
                    BLECommHandler.this.mBluetoothGatt.discoverServices();
                    return;
                }
                Log.e(BLECommHandler.this.TAG, "Not connected to BLE Device -> Device not Found");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("err_code", 1);
                    jsonObject.put("err_msg", device.getAddress());
                    // smartapp.getEventBus().post(new Message(EventType.BLE_DISCONNECTED, jsonObject));
                    BLECommHandler.this.connectedDevice = null;
                } catch (Exception e) {
                }
            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == 0) {
                    Log.d(BLECommHandler.this.TAG, "Bluetooth Conencted and Services Discovered");
                    BLECommHandler.this.connectedDevice = device;
                    //  smartapp.getEventBus().post(new Message(EventType.BLE_CONNECTED));
                    return;
                }
                // smartapp.getEventBus().post(new Message(EventType.BLE_DISCONNECTED));
                BLECommHandler.this.mBluetoothGatt.disconnect();
            }

            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ble_uuid", characteristic.getUuid());
                        jsonObject.put("ble_data", characteristic.getValue());
                        // smartapp.getEventBus().post(new Message(EventType.BLE_READ_DATA, jsonObject));
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("err_code", status);
                        jsonObject2.put("err_msg", "Bluetooth Read Failed");
                        jsonObject2.put("ble_uuid", characteristic.getUuid());
                        jsonObject2.put("ble_data", characteristic.getValue());
                        //  smartapp.getEventBus().post(new Message(EventType.BLE_READ_DATA, jsonObject2));
                    } catch (Exception e2) {
                    }
                }
            }

            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ble_uuid", characteristic.getUuid());
                        //  smartapp.getEventBus().post(new Message(EventType.BLE_WRITTEN, jsonObject));
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("err_code", status);
                        jsonObject2.put("err_msg", "Bluetooth Write Failed");
                        //  smartapp.getEventBus().post(new Message(EventType.BLE_WRITTEN, jsonObject2));
                    } catch (Exception e2) {
                    }
                }
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ble_uuid", characteristic.getUuid());
                    jsonObject.put("ble_data", characteristic.getValue());
                    // smartapp.getEventBus().post(new Message(EventType.BLE_NOTIFICATION, jsonObject));
                } catch (Exception e) {
                }
            }
        });
    }

    public void writeData(UUID uuid, byte[] data) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mBluetoothGatt.getService(BLEConstants.BLE_SERVICE_UUID).getCharacteristic(uuid);
        bluetoothGattCharacteristic.setValue(data);
        this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    public void readData(UUID uuid) {
        this.mBluetoothGatt.readCharacteristic(this.mBluetoothGatt.getService(BLEConstants.BLE_SERVICE_UUID).getCharacteristic(uuid));
    }

    public void enableNotification(UUID uuid, boolean enable) {
        this.mBluetoothGatt.setCharacteristicNotification(this.mBluetoothGatt.getService(BLEConstants.BLE_SERVICE_UUID).getCharacteristic(uuid), enable);
    }

    public boolean isConnected() {
        BluetoothDevice bluetoothDevice = this.connectedDevice;
        if (bluetoothDevice == null || this.mBluetoothGatt == null) {
            return false;
        }
        if (this.mBluetoothManager.getConnectionState(bluetoothDevice, 7) == 2) {
            Log.i(this.TAG, "Device is connected");
            return true;
        }
        Log.e(this.TAG, "Device is disconnected");
        return false;
    }

    private Observable<RxBleConnection> prepareConnectionObservable(RxBleDevice bleDevice) {
        Log.d(Tag, "prepareConnectionObservable");
        return bleDevice.establishConnection(false).takeUntil(this.disconnectTriggerSubject).compose(ReplayingShare.instance());
    }

    public void registerNotification(RxBleDevice device, UUID uuid, NotificationCallback notificationCallback) {
        if (device.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            this.bleConnectionObservable.flatMap(new Function() {


                @Override // io.reactivex.functions.Function
                public final Object apply(Object obj) {
                    return ((RxBleConnection) obj).setupNotification(uuid, NotificationSetupMode.COMPAT);
                }
            }).doOnNext(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.this.lambda$registerNotification$1$BLECommHandler(notificationCallback, (Observable) obj);
                }
            }).flatMap($$Lambda$BLECommHandler$SXMtuDSX6Z3ZByDyDFcUiTRSTQ.INSTANCE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.lambda$registerNotification$3(notificationCallback, (byte[]) obj);
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.lambda$registerNotification$4(notificationCallback, (Throwable) obj);
                }
            });
            return;
        }
        this.bleConnectionObservable = prepareConnectionObservable(device);
        this.bleConnectionObservable.flatMapSingle($$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc.INSTANCE).flatMapSingle($$Lambda$BLECommHandler$NgpSZifyLRR4Ah6dHXcztUMNa8.INSTANCE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$registerNotification$11$BLECommHandler(uuid, notificationCallback, (BluetoothGattCharacteristic) obj);
            }
        }, new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$registerNotification$12$BLECommHandler(notificationCallback, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$registerNotification$1$BLECommHandler(NotificationCallback notificationCallback, Observable observable) {
        Log.i(this.TAG, "Notification has been setup");
        if (notificationCallback != null) {
            notificationCallback.onNotification(-1, null);
        }
    }

    static /* synthetic */ ObservableSource lambda$registerNotification$2(Observable observable) {
        return observable;
    }

    static /* synthetic */ void lambda$registerNotification$3(NotificationCallback notificationCallback, byte[] bytes) {
        if (notificationCallback != null) {
            notificationCallback.onNotification(0, bytes);
        }
    }

    static /* synthetic */ void lambda$registerNotification$4(NotificationCallback notificationCallback, Throwable throwable) {
        if (notificationCallback != null) {
            notificationCallback.onNotification(1, null);
        }
    }

    public /* synthetic */ void lambda$registerNotification$11$BLECommHandler(UUID uuid, NotificationCallback notificationCallback, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.bleConnectionObservable.flatMap(new Function() {


            @Override // io.reactivex.functions.Function
            public final Object apply(Object obj) {
                return ((RxBleConnection) obj).setupNotification(uuid, NotificationSetupMode.COMPAT);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$null$7$BLECommHandler(notificationCallback, (Observable) obj);
            }
        }).flatMap($$Lambda$BLECommHandler$M29pQkXVYMIcxVHTASVl9REjKTU.INSTANCE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.lambda$null$9(notificationCallback, (byte[]) obj);
            }
        }, new Consumer() {

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.lambda$null$10(notificationCallback, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$null$7$BLECommHandler(NotificationCallback notificationCallback, Observable observable) {
        Log.i(this.TAG, "Notification has been setup");
        if (notificationCallback != null) {
            notificationCallback.onNotification(-1, null);
        }
    }

    static /* synthetic */ ObservableSource lambda$null$8(Observable observable) {
        return observable;
    }

    static /* synthetic */ void lambda$null$9(NotificationCallback notificationCallback, byte[] bytes) {
        if (notificationCallback != null) {
            notificationCallback.onNotification(0, bytes);
        }
    }

    static /* synthetic */ void lambda$null$10(NotificationCallback notificationCallback, Throwable throwable) {
        if (notificationCallback != null) {
            notificationCallback.onNotification(1, null);
        }
    }

    public /* synthetic */ void lambda$registerNotification$12$BLECommHandler(NotificationCallback notificationCallback, Throwable throwable) {
        Log.e(this.TAG, "BLE Communication Failed(1): ", throwable);
        if (notificationCallback != null) {
            notificationCallback.onNotification(1, null);
        }
    }

    public void readFromBle(RxBleDevice device, UUID uuid, OperationResultCallback callback) {
        if (device.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            this.bleConnectionObservable.flatMapSingle(new Function() {


                @Override // io.reactivex.functions.Function
                public final Object apply(Object obj) {
                    return ((RxBleConnection) obj).readCharacteristic(uuid);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {

                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.lambda$readFromBle$14(callback, (byte[]) obj);
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.lambda$readFromBle$15(callback, (Throwable) obj);
                }
            });
            return;
        }
        this.bleConnectionObservable = prepareConnectionObservable(device);
        this.bleConnectionObservable.flatMapSingle($$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc.INSTANCE).flatMapSingle($$Lambda$BLECommHandler$kOr6sbKh9hiNnYJPXR4BbUa1Bak.INSTANCE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$readFromBle$20$BLECommHandler(uuid, callback, (BluetoothGattCharacteristic) obj);
            }
        }, new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$readFromBle$21$BLECommHandler(callback, (Throwable) obj);
            }
        });
    }

    static /* synthetic */ void lambda$readFromBle$14(OperationResultCallback callback, byte[] bytes) {
        if (callback != null) {

            callback.onOperationResult(0, Base64.encodeToString(bytes, 0));
        }
    }

    static /* synthetic */ void lambda$readFromBle$15(OperationResultCallback callback, Throwable throwable) {
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    public /* synthetic */ void lambda$readFromBle$20$BLECommHandler(UUID uuid, OperationResultCallback callback, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.bleConnectionObservable.flatMapSingle(new Function() {


            @Override // io.reactivex.functions.Function
            public final Object apply(Object obj) {
                return ((RxBleConnection) obj).readCharacteristic(uuid);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.lambda$null$18(callback, (byte[]) obj);
            }
        }, new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.lambda$null$19(callback, (Throwable) obj);
            }
        });
    }

    static /* synthetic */ void lambda$null$18(OperationResultCallback callback, byte[] bytes) {
        if (callback != null) {
            callback.onOperationResult(0, Base64.encodeToString(bytes, 0));
        }
    }

    static /* synthetic */ void lambda$null$19(OperationResultCallback callback, Throwable throwable) {
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    public /* synthetic */ void lambda$readFromBle$21$BLECommHandler(OperationResultCallback callback, Throwable throwable) {
        Log.e(this.TAG, "BLE Communication Failed(2): ", throwable);
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    public void sendDataOverBLE(RxBleDevice device, String key, JSONObject jsonObject, UUID uuid, OperationResultCallback callback) {
        String str = this.TAG;
        Log.d(str, "Ble data Sending: " + jsonObject.toString());
        byte[] byteData = encryptBLEData(key, jsonObject, (byte) 0);
        String str2 = this.TAG;
        Log.i(str2, "Ble data Sending Length: " + byteData.length);
        sendRawDataOverBLE(device, uuid, byteData, callback);
    }


    public void sendRawDataOverBLE(RxBleDevice device, UUID uuid, byte[] byteData, OperationResultCallback callback) {
        Log.d(Tag, "sendRawDataOverBLE: "+byteData);
        //   Log.d(Tag, "sendRawDataOverBLE:" +byteData);

        if (device.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            Log.d(Tag, "RxBleConnection.RxBleConnectionState.CONNECTED");
            this.bleConnectionObservable.flatMapSingle(new Function() {


                @Override // io.reactivex.functions.Function
                public final Object apply(Object obj) {
                    return ((RxBleConnection) obj).writeCharacteristic(uuid, byteData);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.this.lambda$sendRawDataOverBLE$23$BLECommHandler(callback, (byte[]) obj);
                }
            }, new Consumer() {


                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    BLECommHandler.this.lambda$sendRawDataOverBLE$24$BLECommHandler(callback, (Throwable) obj);
                }

            });
            return;
        }
        this.bleConnectionObservable = prepareConnectionObservable(device);
        this.bleConnectionObservable.flatMapSingle($$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc.INSTANCE).flatMapSingle(new Function() {

            @Override // io.reactivex.functions.Function
            public final Object apply(Object obj) {

                return ((RxBleDeviceServices) obj).getCharacteristic(uuid);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$sendRawDataOverBLE$29$BLECommHandler(uuid, byteData, callback, (BluetoothGattCharacteristic) obj);
            }
        }, new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$sendRawDataOverBLE$30$BLECommHandler(callback, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$sendRawDataOverBLE$23$BLECommHandler(OperationResultCallback callback, byte[] bytes) {
        String str = this.TAG;
        Log.i(str, "BLE Write Success: " + bytes);
        Log.i(str, "BLE Write Success: " + bytes.length);
        if (callback != null) {
            callback.onOperationResult(0, "DONE");
        }
    }

    public /* synthetic */ void lambda$sendRawDataOverBLE$24$BLECommHandler(OperationResultCallback callback, Throwable throwable) {
        Log.e(this.TAG, "BLE Communication Failed(3): ", throwable);
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    public /* synthetic */ void lambda$sendRawDataOverBLE$29$BLECommHandler(UUID uuid, byte[] byteData, OperationResultCallback callback, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Log.i("TAG", "BLE connection has been established!");
        this.bleConnectionObservable.flatMapSingle(new Function() {


            @Override // io.reactivex.functions.Function
            public final Object apply(Object obj) {
                return ((RxBleConnection) obj).writeCharacteristic(uuid, byteData);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$null$27$BLECommHandler(callback, (byte[]) obj);
            }
        }, new Consumer() {


            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                BLECommHandler.this.lambda$null$28$BLECommHandler(callback, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$null$27$BLECommHandler(OperationResultCallback callback, byte[] bytes) {
        String str = this.TAG;
        Log.i(str, "BLE Write Success: " + bytes.length);
        if (callback != null) {
            callback.onOperationResult(0, "DONE");
        }
    }

    public /* synthetic */ void lambda$null$28$BLECommHandler(OperationResultCallback callback, Throwable throwable) {
        Log.e(this.TAG, "BLE Communication Failed(4): ", throwable);
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    public /* synthetic */ void lambda$sendRawDataOverBLE$30$BLECommHandler(OperationResultCallback callback, Throwable throwable) {
        Log.e(this.TAG, "BLE Communication Failed(5): ", throwable);
        if (callback != null) {
            callback.onOperationResult(1, throwable.getMessage());
        }
    }

    /* JADX INFO: Multiple debug info for r2v3 int: [D('i' int), D('j' int)] */
    public byte[] encryptBLEData(String key, JSONObject jsonObject, byte tag) {
        byte[] byteData = jsonObject.toString().getBytes();
        byte[] aesIV = new byte[16];
        new SecureRandom().nextBytes(aesIV);
        byte[] encrypted = new byte[0];
        //  byte[] encrypted = new CryptoAES(Base64.decode(key, 0), aesIV).encrypt(byteData);
        byte[] encryptedSendData = new byte[(encrypted.length + 18 + (tag != 0 ? 1 : 0))];
        int i = 0;
        int j = 0;
        while (j < 16) {
            encryptedSendData[i] = aesIV[j];
            j++;
            i++;
        }
        int j2 = 0;
        while (j2 < encrypted.length) {
            encryptedSendData[i] = encrypted[j2];
            j2++;
            i++;
        }
        int j3 = i + 1;
        encryptedSendData[i] = tag;
        int checksum = 0;
        //   int checksum = CryptoCRC.caluCRC(0, encryptedSendData, encryptedSendData.length - 2);
        int i2 = j3 + 1;
        encryptedSendData[j3] = (byte) (checksum & 255);
        int i3 = i2 + 1;
        encryptedSendData[i2] = (byte) ((checksum >> 8) & 255);
        return encryptedSendData;
    }

    public void disconnectBle() {
        try {
            this.disconnectTriggerSubject.onNext(null);
        } catch (Exception e) {
        }
    }
}
