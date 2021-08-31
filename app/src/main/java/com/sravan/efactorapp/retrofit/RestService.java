package com.sravan.efactorapp.retrofit;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sravan.efactorapp.BuildConfig;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * Created by Vishvendra.Singh@Brsoftech on 20/6/16.
 */

public class RestService {

    private static long CONNECTION_TIMEOUT = 200;
    static boolean isdebug = BuildConfig.DEBUG;

    public static String API_BASE_URL = "https://efactor.myoffice-365.com/api/";
    //   public static String API_BASE_URL =  "http://205.147.102.6/p/sites/laundrytiger/vendor/v1/";
    //    public static String API_BASE_URL = "http://live.brsoftech.net/laundryyy/vendor/v1/";
    public static String BASEURL_GEOCODER = "https://maps.googleapis.com/maps/api/";
    public static final String URL_FOR_ROUTE = "https://maps.googleapis.com/maps/api/directions/";

    public static Rest getService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = getOkHttpClient();

        Rest rest = new Retrofit.Builder().baseUrl(API_BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                .create(Rest.class);
        return rest;
    }

    public static Rest getServiceGeoCoder() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = getOkHttpClient();

        Rest rest = new Retrofit.Builder().baseUrl(BASEURL_GEOCODER).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                .create(Rest.class);
        return rest;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        //okClientBuilder.addInterceptor(headerAuthorizationInterceptor);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("BrLaundaryVendor", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClientBuilder.addInterceptor(httpLoggingInterceptor);
        //  okClientBuilder.addNetworkInterceptor(new StethoInterceptor());
//        final @Nullable File baseDir = context.getCacheDir();
//        if (baseDir != null) {
//            final File cacheDir = new File(baseDir, “HttpResponseCache”);
//            okClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
//        }
        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        return okClientBuilder.build();
    }

    public static Rest getGoogleDirection() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = getOkHttpClient();

        Rest rest = new Retrofit.Builder().baseUrl(URL_FOR_ROUTE).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                .create(Rest.class);
        return rest;
    }
}

