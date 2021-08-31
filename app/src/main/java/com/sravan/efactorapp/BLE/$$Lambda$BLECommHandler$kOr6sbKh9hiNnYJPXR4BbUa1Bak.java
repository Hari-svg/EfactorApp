package com.sravan.efactorapp.BLE;

import com.polidea.rxandroidble2.RxBleDeviceServices;

import io.reactivex.functions.Function;


public final /* synthetic */ class $$Lambda$BLECommHandler$kOr6sbKh9hiNnYJPXR4BbUa1Bak implements Function {
    public static final /* synthetic */ $$Lambda$BLECommHandler$kOr6sbKh9hiNnYJPXR4BbUa1Bak INSTANCE = new $$Lambda$BLECommHandler$kOr6sbKh9hiNnYJPXR4BbUa1Bak();

    private /* synthetic */ $$Lambda$BLECommHandler$kOr6sbKh9hiNnYJPXR4BbUa1Bak() {
    }

    @Override // io.reactivex.functions.Function
    public final Object apply(Object obj) {
        return ((RxBleDeviceServices) obj).getCharacteristic(BLEConstants.BLE_SERVICE_ATTR_CONN_UUID);
    }
}

