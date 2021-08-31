package com.sravan.efactorapp.UI.Fragments.Device;


import com.sravan.efactorapp.utils.Utilities;

public class Fan {
    private int power;

    public Fan(int power2) {
        this.power = power2;
    }

    public static String requestStatusFrame() {
        return Utilities.byteArrayToHexString(new byte[]{83, 84});
    }

    public String setStatusFrame() {

        return Utilities.byteArrayToHexString(new byte[]{80, (byte) (this.power + 48)});
    }

    public int setStatusFromData(String data) {
        byte[] frame = Utilities.hexStringToByteArray(data);
        if (frame.length < 2) {
            this.power = 0;
        } else {
            this.power = frame[1] - 48;
        }
        if (this.power > 1) {
            this.power = 0;
        }
        return this.power;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power2) {
        this.power = power2;
    }
}