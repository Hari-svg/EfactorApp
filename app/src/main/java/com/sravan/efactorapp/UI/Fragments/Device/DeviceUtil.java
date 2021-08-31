package com.sravan.efactorapp.UI.Fragments.Device;

public class DeviceUtil {
    public static String getTypeStr(String type) {
        if (type.equals("FAN")) {
            return "Fan";
        }
        return "Smart Device";
    }

    public static int getPowerStatus(String type, String statusData) {
        if (type.equals("FAN")) {
            //  return new Fan(0).setStatusFromData(statusData);
        }
        return 0;
    }
}
