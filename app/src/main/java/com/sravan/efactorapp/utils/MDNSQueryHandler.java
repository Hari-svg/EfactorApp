package com.sravan.efactorapp.utils;

import android.content.Context;
import android.util.Log;

import com.github.druk.rx2dnssd.BonjourService;
import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdBindable;


import org.json.JSONObject;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MDNSQueryHandler {
    private static final String TAG = MDNSQueryHandler.class.getSimpleName();
    private static MDNSQueryHandler instance = null;
    private Disposable dnsSubscription;
    private Rx2Dnssd rx2Dnssd;

    public static void init(Context context) {
        if (instance == null) {
            instance = new MDNSQueryHandler(context);
        }
    }

    public static MDNSQueryHandler getInstance() {
        return instance;
    }

    private MDNSQueryHandler(Context context) {
        this.rx2Dnssd = new Rx2DnssdBindable(context);
        RxJavaPlugins.setErrorHandler($$Lambda$MDNSQueryHandler$HdRJkEPEasKM0VAP2fT5pcj8E8.INSTANCE);
    }

    public static /* synthetic */ void lambda$new$0(Throwable throwable)  {
    }

    public void startDiscovery() {
        if (this.dnsSubscription == null) {
            this.dnsSubscription = this.rx2Dnssd.browse("_efactor._tcp", "local.").compose(this.rx2Dnssd.resolve()).compose(this.rx2Dnssd.queryIPV4Records()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$MDNSQueryHandler$7AG7aSO_m1WpODk1VNWFlHdxAI.INSTANCE, $$Lambda$MDNSQueryHandler$JWL2GAqbrgi2TniV6uRmylNxGk.INSTANCE);
            Log.i(TAG, "Start MDNS Discovery");
        }
    }

    public static /* synthetic */ void lambda$startDiscovery$1(BonjourService bonjourService) throws Exception {
        Log.d(TAG, bonjourService.toString());
        String hostname = bonjourService.getHostname();
        if (hostname != null) {
            Map<String, String> textData = bonjourService.getTxtRecords();
            String ip = bonjourService.getInet4Address().getHostAddress();
            String apn = hostname.replace(".local.", "").trim();
            String str = TAG;
            Log.d(str, "APN: " + apn + "   IP: " + ip + " Text Data: " + textData);
            if (textData.get("state").equals("NEW") && textData.get("type").equals("DEV_RPI3_GATEWAY")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("apn", apn);
                jsonObject.put("ip", ip);
                jsonObject.put("ver", textData.get("ver"));
                jsonObject.put("mac", textData.get("mac"));
                //smartapp.getEventBus().post(new Message(EventType.GATEWAY_FOUND_NEW, jsonObject));
            }
            ReachabilityHandler.getInstance().addLocalWifiReachability(textData.get("mac"), ip);
        }
    }

    public static /* synthetic */ void lambda$startDiscovery$2(Throwable throwable) throws Exception {
    }

    public void stopDiscovery() {
        try {
            if (this.dnsSubscription != null && !this.dnsSubscription.isDisposed()) {
                Log.w(TAG, "Stop MDNS Discovery");
                this.dnsSubscription.dispose();
            }
            this.dnsSubscription = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

