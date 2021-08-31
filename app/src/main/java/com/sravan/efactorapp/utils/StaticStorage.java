package com.sravan.efactorapp.utils;

import android.location.Location;

import com.sravan.efactorapp.utils.DataBase.Model.User;


public class StaticStorage {
    public static Location currentLocation = null;
    public static User currentUser = null;
    public static boolean deviceRefreshRequired = true;
}
