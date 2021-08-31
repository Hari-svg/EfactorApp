package com.sravan.efactorapp.utils;

import com.github.druk.rx2dnssd.BonjourService;

import io.reactivex.functions.Consumer;


public final /* synthetic */ class $$Lambda$MDNSQueryHandler$7AG7aSO_m1WpODk1VNWFlHdxAI implements Consumer {
    public static final /* synthetic */ $$Lambda$MDNSQueryHandler$7AG7aSO_m1WpODk1VNWFlHdxAI INSTANCE = new $$Lambda$MDNSQueryHandler$7AG7aSO_m1WpODk1VNWFlHdxAI();

    private /* synthetic */ $$Lambda$MDNSQueryHandler$7AG7aSO_m1WpODk1VNWFlHdxAI() {
    }

    @Override // io.reactivex.functions.Consumer
    public final void accept(Object obj) throws Exception {
        MDNSQueryHandler.lambda$startDiscovery$1((BonjourService) obj);
    }
}
