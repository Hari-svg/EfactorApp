package com.sravan.efactorapp.utils;

import android.content.res.Resources;
import android.util.Log;

import java.security.SecureRandom;
import java.util.Calendar;



public class Utilities {
    private static final String HEXES = "0123456789ABCDEF";
    private static final String HEX_HEADER = "0x";
    private static final String TAG = "Utilities";

    public static float convertDpToPixel(float dp) {
        return (float) Math.round((((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f) * dp);
    }

    public static String generateCode(int chars) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < chars) {
            stringBuilder.append(Integer.toHexString(random.nextInt()));
        }
        return stringBuilder.substring(0, chars);
    }

    public static long getEpoch() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    public static String byteArrayToHexString(byte[] value) {
        if (value != null) {
            StringBuilder hex = new StringBuilder(value.length * 2);
            for (byte b : value) {
                hex.append(HEXES.charAt((b & 240) >> 4));
                hex.append(HEXES.charAt(b & 15));
            }
            String str2=TAG;
            Log.d(str2, "Device Status: " + value[0]);
            Log.d(str2, "Device Status: " + value[1]);
            //   return hex.toString();

            if(value[1]==49) {
                return "01005508A1C2000000016C";
            }
            else if(value[1]==48) {
                return "01005508A1C2000000006B";
            }
            else {
                return hex.toString();
            }
        }
        throw new NullPointerException("Value to convert cannot be null.");
    }

    /*public static String bytesToString(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (byte b : paramArrayOfByte) {
            String str = Integer.toHexString(b & UByte.MAX_VALUE);
            if (str.length() < 2) {
                str = "0" + str;
            }
            localStringBuilder.append(str);
        }
        return localStringBuilder.toString();
    }
*/
    public static byte[] hexStringToByteArray(String value) {
        if (value != null) {
            String value2 = value.trim();
            if (value2.startsWith(HEX_HEADER)) {
                value2 = value2.substring(HEX_HEADER.length());
            }
            int len = value2.length();
            if (len % 2 != 0) {
                value2 = "0" + value2;
                len = value2.length();
            }
            byte[] data = new byte[(len / 2)];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(value2.charAt(i), 16) << 4) + Character.digit(value2.charAt(i + 1), 16));
            }
            return data;
        }
        throw new NullPointerException("Value to convert cannot be null.");
    }

    public static byte[] hexIntStringToBytes(String paramString) {
        if (paramString.length() < 2) {
            paramString = "0" + paramString;
        }
        byte[] arrayOfByte = new byte[(paramString.length() / 2)];
        for (int i = 0; i < paramString.length(); i += 2) {
            arrayOfByte[i / 2] = (byte) Integer.parseInt(paramString.substring(i, i + 2), 16);
        }
        return arrayOfByte;
    }


}
