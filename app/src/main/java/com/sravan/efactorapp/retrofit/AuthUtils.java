package com.sravan.efactorapp.retrofit;


import android.util.Base64;

public class AuthUtils {
    public static String basic(String email, String password) {

        String basicAuth = "Basic " +
                Base64.encodeToString(String.format("%s:%s", email, password)
                                .getBytes(),
                        Base64.NO_WRAP);

        return basicAuth;
    }

}
