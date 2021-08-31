package com.sravan.efactorapp.utils.Crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.UByte;

public class CryptoMD5 {
    public static byte[] getMD5Byte(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("md5");
            localMessageDigest.update(paramArrayOfByte);
            return localMessageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5String(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("md5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte1 = localMessageDigest.digest();
            StringBuilder localStringBuilder = new StringBuilder();
            for (byte b : arrayOfByte1) {
                String str = Integer.toHexString(b & UByte.MAX_VALUE);
                if (str.length() == 1) {
                    localStringBuilder.append("0");
                }
                localStringBuilder.append(str);
            }
            return localStringBuilder.toString();
        } catch (NullPointerException | NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
            return "";
        }
    }
}
