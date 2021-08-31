package com.sravan.efactorapp.utils;

import android.content.Context;


import com.sravan.efactorapp.Enum.ThingReachability;
import com.sravan.efactorapp.spf.SessionManager;
import com.sravan.efactorapp.utils.DataBase.DatabaseHandler;
import com.sravan.efactorapp.utils.DataBase.Model.Gateway;

import java.util.HashMap;
import java.util.Map;

public class ReachabilityHandler {
    private static ReachabilityHandler instance;

    private HashMap<String, String> thingLocalIPMap = DatabaseHandler.getInstance().restoreLocalIPDetails();
    private HashMap<String, Integer> thingReachabilityMap = new HashMap<>();

    public static void init(Context context) {
        if (instance == null) {
            instance = new ReachabilityHandler(context);
        }
    }

    public static ReachabilityHandler getInstance() {
        return instance;
    }

    private ReachabilityHandler(Context context) {

        HashMap<String, String> hashMap = this.thingLocalIPMap;
        if (hashMap == null) {
            this.thingLocalIPMap = new HashMap<>();
            return;
        }
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            addLocalWifiReachability(entry.getKey(), entry.getValue());
        }
    }

    public synchronized void addThingReachability(String thingid, ThingReachability reachability) {
        if (this.thingReachabilityMap.containsKey(thingid)) {
            this.thingReachabilityMap.put(thingid, Integer.valueOf(reachability.getValue() | this.thingReachabilityMap.get(thingid).intValue()));
        } else {
            this.thingReachabilityMap.put(thingid, Integer.valueOf(reachability.getValue()));
        }
    }

    public synchronized void addLocalWifiReachability(String mac, String ip) {
        Gateway thing = DatabaseHandler.getInstance().getGatewayFromMAC(mac);
        if (thing != null) {
            this.thingLocalIPMap.put(mac, ip);
            DatabaseHandler.getInstance().saveLocalIPDetails(this.thingLocalIPMap);
            addThingReachability(thing.getGateway_id(), ThingReachability.LOCAL_WIFI);
        }
    }

    public synchronized void removeThingReachability(String thingid, ThingReachability reachability) {
        if (this.thingReachabilityMap.containsKey(thingid)) {
            if (reachability != null) {
                int value = this.thingReachabilityMap.get(thingid).intValue();
                this.thingReachabilityMap.put(thingid, Integer.valueOf(value ^ (reachability.getValue() & value)));
            } else {
                this.thingReachabilityMap.remove(thingid);
            }
            if (reachability == ThingReachability.LOCAL_WIFI) {
                removeLocalIP(DatabaseHandler.getInstance().getGateway(thingid).getMac());
            }
        }
    }

    public synchronized boolean isThingReachable(String thingid, ThingReachability reachability) {
        if (!this.thingReachabilityMap.containsKey(thingid)) {
            return false;

        }
        boolean z = true;
        if (reachability == null) {

            if (this.thingReachabilityMap.get(thingid).intValue() == 0) {
                z = false;
            }

            return z;
        }
        if ((this.thingReachabilityMap.get(thingid).intValue() & reachability.getValue()) == 0) {
            z = false;
        }
        return z;
    }

    public synchronized String getLocalIP(String mac) {
        return this.thingLocalIPMap.get(mac)/*"MAC9C9C1FEE8BDC"*/;
    }

    public synchronized void removeLocalIP(String mac) {
        if (this.thingLocalIPMap.containsKey(mac)) {
            this.thingLocalIPMap.remove(mac);
            DatabaseHandler.getInstance().saveLocalIPDetails(this.thingLocalIPMap);
        }
    }

    public synchronized String getGatewayLocalIP(String id) {
        return this.thingLocalIPMap.get(DatabaseHandler.getInstance().getGateway(id).getMac());
    }
}
