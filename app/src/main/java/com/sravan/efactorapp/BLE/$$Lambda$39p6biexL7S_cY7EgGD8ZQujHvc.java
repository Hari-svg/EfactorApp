package com.sravan.efactorapp.BLE;

import com.polidea.rxandroidble2.RxBleConnection;

import io.reactivex.functions.Function;


public final /* synthetic */ class $$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc implements Function {
    public static final /* synthetic */ $$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc INSTANCE = new $$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc();

    private /* synthetic */ $$Lambda$39p6biexL7S_cY7EgGD8ZQujHvc() {
    }

    @Override // io.reactivex.functions.Function
    public final Object apply(Object obj) {
        return ((RxBleConnection) obj).discoverServices();
    }
}
