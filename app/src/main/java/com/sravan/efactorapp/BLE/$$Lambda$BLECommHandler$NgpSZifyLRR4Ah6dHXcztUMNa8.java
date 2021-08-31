package com.sravan.efactorapp.BLE;

import com.polidea.rxandroidble2.RxBleDeviceServices;

import io.reactivex.functions.Function;


public final /* synthetic */ class $$Lambda$BLECommHandler$NgpSZifyLRR4Ah6dHXcztUMNa8 implements Function {
    public static final /* synthetic */ $$Lambda$BLECommHandler$NgpSZifyLRR4Ah6dHXcztUMNa8 INSTANCE = new $$Lambda$BLECommHandler$NgpSZifyLRR4Ah6dHXcztUMNa8();

    private /* synthetic */ $$Lambda$BLECommHandler$NgpSZifyLRR4Ah6dHXcztUMNa8() {
    }

    @Override // io.reactivex.functions.Function
    public final Object apply(Object obj) {
        return ((RxBleDeviceServices) obj).getCharacteristic(BLEConstants.BLE_SERVICE_ATTR_CONN_UUID);
    }
}