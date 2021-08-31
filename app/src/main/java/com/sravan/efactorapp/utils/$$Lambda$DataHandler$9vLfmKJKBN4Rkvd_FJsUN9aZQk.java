package com.sravan.efactorapp.utils;

import com.sravan.efactorapp.App.App;
import com.sravan.efactorapp.Event.EventType;
import com.sravan.efactorapp.Event.Message;


import org.json.JSONObject;

import io.reactivex.functions.Consumer;

public final /* synthetic */ class $$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk implements Consumer {
    public static final /* synthetic */ $$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk INSTANCE = new $$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk();

    private /* synthetic */ $$Lambda$DataHandler$9vLfmKJKBN4Rkvd_FJsUN9aZQk() {
    }

    @Override // io.reactivex.functions.Consumer
    public final void accept(Object obj) {
        App.getEventBus().post(new Message(EventType.LOCAL_DEVICE_DATA_RECEIVED, (JSONObject) obj));
    }
}

