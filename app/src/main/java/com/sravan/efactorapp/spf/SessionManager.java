package com.sravan.efactorapp.spf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import com.google.gson.Gson;
import com.sravan.efactorapp.MainActivity;
import com.sravan.efactorapp.Model.UserLogin;
import com.sravan.efactorapp.UI.Activities.DashboardActivity;

/**
 * Created by ubuntu on 9/11/17.
 */

public class SessionManager {

    private static final String COMPANY_NAME = "companyname";
    private static String session_name = "Efactor";
    private static String RESPONSE = "response";
    private static String BALANCE = "BALANCE";
    private static String LOCALIP = "local_ip";

    private static String CREDIT_CARD = "credit_card";

    private static String FUllNAME = "fname";
   // private static String LNAME = "lname";
    private static String IMAGE = "image";
    private static String Country = "country";
    private static String PHONE = "phone";
    //private static String DriverStatus = "DriverStatus";

    private static String EMAIL = "email";
    private static String PASSWORD = "password";
    private static String USER_ID = "user_id";
    private static String AUTHCODE = "authcode";

   // private static String PACKAGE_CATEGERYID = "PackageCategories";

    private static String ISLOGIN = "is_login";

    private static String ADDRESS = "address";
    private static String MOBILE = "mobile";
    private static String DIALCODE = "dialcode";
    private static final String TAG = "SessionManager";


    private Context context;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        this.context = context;
        if (context == null) return;

        preferences = context.getSharedPreferences(session_name, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }



  /*  public static void setPackageCategeryid(String packageCategeryid) {
        editor.putString(PACKAGE_CATEGERYID, packageCategeryid);
        editor.commit();

    }*/

    public void saveUserDetails(String result) {
        editor.putString(RESPONSE, result);
        editor.commit();
    }

    public void saveBalance(String balance) {
        editor.putString(BALANCE, balance);
        editor.commit();
    }

    public String getBalance() {
        return preferences.getString(BALANCE, "0");

    }


    public String getUserDetails() {
        return preferences.getString(RESPONSE, "");

    }
    public void  setLOCALIP(String MAC){
        editor.putString(LOCALIP,MAC);
        editor.commit();

    }
    public String getLOCALIP() {
        return preferences.getString(LOCALIP, "");

    }

    public UserLogin getUserModel(Context context) {
        String userDetails = getUserDetails();
        if (userDetails != null && !userDetails.isEmpty()) {
            UserLogin userLogin = new Gson().fromJson(userDetails, UserLogin.class);
            if (userLogin != null) {
                UserLogin data = userLogin;
                return data;
            }
        }
        return null;
    }

    public void saveCountry(String country) {
        editor.putString(Country, country);
        editor.commit();
    }

    public String getCountry() {
        return preferences.getString(Country, "");
    }



    public UserLogin getResponse(){

       String result= preferences.getString(RESPONSE, "def_Value");
        Log.e(TAG, "result: "+result );
        Gson gson=new Gson();
        UserLogin userLogin=gson.fromJson(result, UserLogin.class);
        return userLogin;
    }

    public void updateUserInfo(String fname, String lname, String country,String companyname) {

        editor.putString(FUllNAME, fname);

        editor.putString(Country, country);
        editor.putString(COMPANY_NAME, companyname);
        editor.commit();
    }

    public void updateUserInfo(String fullname, String mobile, String country, String image,String companyname) {

        editor.putString(FUllNAME, fullname);
        editor.putString(MOBILE, mobile);
        editor.putString(Country, country);
        editor.putString(IMAGE, image);
        editor.putString(COMPANY_NAME, companyname);

        editor.commit();
    }

    public void createLoginSession(String userid, String fullname, String email, String password,
                                   String mobile,String authcode) {
        editor.putBoolean(ISLOGIN, true);
        editor.putString(FUllNAME, fullname);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(AUTHCODE, authcode);
        editor.putString(MOBILE, mobile);
        editor.putString(USER_ID, userid);

        Log.e("Session Created",getUserId());
        editor.commit();
    }
//TODO - ADD COMPANY NAME
   /* public void createLoginSession(String fullname, String email, String password, String image,
                                   String mobile,String userid) {
        editor.putBoolean(ISLOGIN, true);
        editor.putString(FUllNAME, fullname);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(IMAGE, image);
        editor.putString(MOBILE, mobile);
        editor.commit();

    }*/




    public String getUserFullName() {
        return preferences.getString(FUllNAME, "");
    }
    public String getAuthCode() {
        return preferences.getString(AUTHCODE, "");
    }
    public String getAddress() {
        return preferences.getString(ADDRESS, "");
    }

    public String getMOBILE() {
        return preferences.getString(MOBILE, "");
    }
    public String getCOMPANYNAME() {
        return preferences.getString(COMPANY_NAME, "");
    }

    // TODO: to get user info use getResponse method
    /*public HashMap<String, String> getUserDetails(){
        HashMap<String, String> map=new HashMap<>();
        map.put(FNAME, preferences.getString(FNAME, "def_fname"));
        map.put(LNAME, preferences.getString(LNAME, "def_lname"));
        map.put(EMAIL, preferences.getString(EMAIL, "def_email"));
        map.put(PASSWORD, preferences.getString(PASSWORD, "def_password"));
        map.put(IMAGE, preferences.getString(IMAGE, "def_image"));
        map.put(ADDRESS, preferences.getString(ADDRESS, "def_image"));
        map.put(MOBILE, preferences.getString(MOBILE, "def_image"));
        return map;
    }*/

    public void checkLogin(Bundle bundle) {
        if (!this.isLoggedIn()) {
            //TODO user is not login
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivity(intent);

        } else {
            // user is already login
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(ISLOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        // after logout, redirect user to login activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }
    public String getUserId() {
        return preferences.getString(USER_ID, "");

    }

    public String getPassword() {
        return preferences.getString(PASSWORD, "def_password");
    }

    public String getProfileImage() {

        Log.e(TAG, "getProfileImage: " + preferences.getString(IMAGE, ""));
        return preferences.getString(IMAGE, "");
    }

    public void updatePassword(String newPassword) {
        editor.putString(PASSWORD, newPassword);
        editor.commit();
    }

    public void updateAddress(String newAddress) {
        editor.putString(ADDRESS, newAddress);
        editor.commit();
    }


    public void saveCreditCardInfo(String result) {
        editor.putString(CREDIT_CARD, result);
        editor.commit();
    }

    public String getCreditCardInfo() {
        return preferences.getString(CREDIT_CARD, "def_credit_card");

    }

    public static String getDIALCODE() {
        return preferences.getString(DIALCODE, "def_credit_card");

    }

    public static void setDIALCODE(String dialcode) {
        editor.putString(DIALCODE, dialcode);
        editor.commit();
    }

    public void savePhone(String phone) {
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public void saveEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }


    public void saveUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }


   }
