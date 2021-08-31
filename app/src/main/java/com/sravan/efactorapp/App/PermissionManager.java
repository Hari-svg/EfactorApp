package com.sravan.efactorapp.App;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ubuntu on 21/11/17.
 */

public class PermissionManager {

    private static final String TAG = "PermissionManager";
    private static final int PERMISSION_REQUEST_CODE=1223;
    public static boolean requestRunning=false;
    private static final List<String> PERMISSIONS= Arrays.asList(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA);

    private static final int MAX_PERMISSION_LABEL_LENGTH = 20;
    AlertDialog alertDialog;


    public static List<String> getPermissionConstants(Context context){
        return PERMISSIONS;
    }

    private static List<PermissionInfo> getPermissions(Context context,
                                                       List<String> requiredPermission) {
        List<PermissionInfo> permissionInfoList=new ArrayList<>();

        PackageManager packageManager=context.getPackageManager();
        for ( String permission: requiredPermission){
            PermissionInfo pInfo=null;

            try {
                pInfo=packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            permissionInfoList.add(pInfo);
        }
        return permissionInfoList;
    }

    private static String[] getPermissionNames(Context context,
                                               List<String> requiredPermission){
        PackageManager packageManager=context.getPackageManager();
        String[] names=new String[requiredPermission.size()];
        int i=0;
        for (PermissionInfo info: getPermissions(context, requiredPermission)){
            CharSequence label=info.loadLabel(packageManager);
            //            if (label.length() > MAX_PERMISSION_LABEL_LENGTH) {
//                label = label.subSequence(0, MAX_PERMISSION_LABEL_LENGTH);
//            }
            names[i] =label.toString();
            i++;
        }
        return names;
    }

    private static boolean[] getPermissionsState(Context context) {
        boolean[] states = new boolean[getPermissionConstants(context).size()];
        int i = 0;
        for (String permission : getPermissionConstants(context)) {
            states[i] = isPermissionGranted(context, permission);
            i++;
        }
        return states;
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission)==
                PackageManager.PERMISSION_GRANTED;
    }

    public static boolean areExplicitPermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void show(final Context context) {
        show(context, null, null);
    }

    public static void show(final Context context, String title, final List<String> requiredPermission){
        if (requiredPermission==null)
            return;
        if (requestRunning)
            return;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        if (title!=null){
            builder.setTitle(title);
        }

        ArrayAdapter adapter=new ArrayAdapter(context, android.R.layout.simple_list_item_1,
                getPermissionNames(context, requiredPermission));
        builder.setAdapter(adapter, null);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                //    alertDialog.cancel();
                requestRunning=true;
                ActivityCompat.requestPermissions(scanForActivity(context),
                        requiredPermission.toArray(new String[requiredPermission.size()]),
                        PERMISSION_REQUEST_CODE);
            }
        });
        builder.setCancelable(false);
        final AlertDialog alertDialog=builder.create();
        try {
            alertDialog.show();
        }
        catch (Exception e){
            Log.e(TAG, "Exception: "+e.getMessage() );
        }
    }

    private static Activity scanForActivity(Context context){
        if (context==null){
            return null;
        }
        else if (context instanceof Activity){
            return (Activity)context;
        }
        else if (context instanceof ContextWrapper){
            return scanForActivity(((ContextWrapper)context).getBaseContext());
        }
        return null;
    }

    public static List<String> isAllPremissiongranted(Context context) {
        List<String> premissions = getPermissionConstants(context);
        List<String> requiredPremisiion = new ArrayList<String>();
        for (int i = 0; i < premissions.size(); i++) {
            if (!isPermissionGranted(context, premissions.get(i))) {
                requiredPremisiion.add(premissions.get(i));
            }
        }
        return requiredPremisiion;
    }
}

