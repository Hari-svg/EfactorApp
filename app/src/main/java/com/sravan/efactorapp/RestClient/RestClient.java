package com.sravan.efactorapp.RestClient;

import android.content.Context;
import android.util.Log;


import com.sravan.efactorapp.retrofit.Rest;
import com.sravan.efactorapp.retrofit.RestService;
import com.sravan.efactorapp.retrofit.RetrofitUtils;
import com.sravan.efactorapp.spf.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ububtu on 13/7/16.
 */
public class RestClient extends BaseRestClient {
    private static final String TAG = "RestClient";
    ApiHitListener apiHitListener;
    private Rest api;
    private Object object;
    SessionManager sessionManager;

    public RestClient(Context _context) {
        super(_context);
        sessionManager = new SessionManager(_context);
    }

    public RestClient callback(ApiHitListener apiHitListener) {
        this.apiHitListener = apiHitListener;
        return this;
    }

    private Rest getApi() {
        if (api == null) {
            api = RestService.getService();
        }

        return api;
    }

    private Rest getApiGeoCoding() {
        if (api == null) {
            api = RestService.getServiceGeoCoder();
        }

        return api;
    }


    public void newUserStep1(String email) {
        Call<ResponseBody> call = getApi().newUser1(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_NEW_USER1, response);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_NEW_USER1, t.getMessage());

            }
        });
    }

    public void resendOTP(String email, String type) {
        Call<ResponseBody> call = getApi().resendOtp(email, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_RESEND_OTP, response);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_RESEND_OTP, t.getMessage());

            }
        });
    }

    public void newUserStep2(String firstname, String lastname, String email,
                             String password, String mobileno, String device_id,
                             String device_token, String device_type, String address, String latitude,
                             String longitude, String mobile_code, String country, String city,
                             MultipartBody.Part part_pan, MultipartBody.Part part_voter, MultipartBody.Part part_aadhar) {

        HashMap<String, String> map = new HashMap<>();
        map.put(BaseArguments.ARG_FIRST_NAME, firstname);
        map.put(BaseArguments.ARG_LAST_NAME, lastname);
        map.put(BaseArguments.ARG_EMAIL, email);
        map.put(BaseArguments.ARG_PASSWORD, password);
        map.put(BaseArguments.ARG_MOBILE_NO, mobileno);
        map.put(BaseArguments.ARG_DEVICE_ID, device_id);
        map.put(BaseArguments.ARG_DEVICE_TOKEN, device_token);
        map.put(BaseArguments.ARG_DEVICE_TYPE, device_type);
        map.put(BaseArguments.ARG_ADDRESS, address);
        map.put(BaseArguments.ARG_LATITUDE, latitude);
        map.put(BaseArguments.ARG_LONGITUDE, longitude);
        map.put(BaseArguments.ARG_MOBILE_CODE, mobile_code);

        Call<ResponseBody> call = getApi().registerUser(RetrofitUtils.createMultipartRequest(map), part_pan, part_voter, part_aadhar);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                apiHitListener.onSuccessResponse(ApiIds.ID_NEW_USER2, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_NEW_USER2, t.getMessage());
            }
        });
    }

    public void userLogin(String email, String password) {
        Call<ResponseBody> call = getApi().login(email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                apiHitListener.onSuccessResponse(ApiIds.ID_USER_LOGIN, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_USER_LOGIN, t.getMessage());
            }
        });
    }

    public void forgetPasswordCall(String email) {
        Call<ResponseBody> call = getApi().forgetPassword(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_FORGET_PASSWORD, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_FORGET_PASSWORD, t.getMessage());

            }
        });
    }

    public void newPassword(int otp, String type, String password, String email) {
        Call<ResponseBody> call = getApi().newPassword(otp, type, password, email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_VERIFY_OTP, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_VERIFY_OTP, t.getMessage());

            }
        });
    }




    public void updateProfile(String auth, Map<String, RequestBody> map,
                              MultipartBody.Part image) {
        Log.e(TAG, "updateProfile: " + image);
        Call<ResponseBody> call = getApi().updateProfile(auth, map, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_UPDATE_PROFILE, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_UPDATE_PROFILE, t.getMessage());

            }
        });
    }


    public void changePassword(String auth, String oldPassword, String newPassword) {
        Call<ResponseBody> call = getApi().changePassword(auth, oldPassword, newPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                apiHitListener.onSuccessResponse(ApiIds.ID_CHANGE_PASSWORD, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_CHANGE_PASSWORD, t.getMessage());

            }
        });
    }


    public void logout(String auth, String deviceId) {
        Call<ResponseBody> call = getApi().logout(auth, deviceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_LOGOUT, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_LOGOUT, t.getMessage());
            }
        });
    }


    public void Get_My_Device_List(String userId) {
        Call<ResponseBody> call = getApi().Get_My_Devices(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_GET_MYDEVICES, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_GET_MYDEVICES, t.getMessage());
            }
        });
    }

    public void Get_ALL_DEVICES_LIST() {
        Call<ResponseBody> call = getApi().Get_ALL_Devices();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_GET_ALL_DEVICES, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_GET_ALL_DEVICES, t.getMessage());
            }
        });
    }

    public void Add_Device(String user_id, String GatewayId, String RoomId, String deviceId, String devicename,
                           String devicetype, String device_ver, String devicefw_ver,
                           String deviceManufacturer, String devicemodelversion, String deviceModelName,
                           String devicemodeldesc, String statusData) {
        Call<ResponseBody> call = getApi().Add_Device(user_id,GatewayId,RoomId,deviceId,devicename,
                devicetype,device_ver,devicefw_ver,deviceManufacturer,devicemodelversion,deviceModelName,devicemodeldesc,statusData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_ADD_DEVICE, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_ADD_DEVICE, t.getMessage());
            }
        });


    }

    public void Send_OTP(String phone) {
        Call<ResponseBody> call = getApi().Send_OTP(phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_SEND_OTP_SUCCESS, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_SEND_OTP_SUCCESS, t.getMessage());
            }
        });

    }





    public void ADD_GATEWAY(String userId, String Gatewayid, String authCode, String lat, String aLong, String name, String mac, String apn, String WIFISSID, String secretKey, String type, String fw_ver, String manufacturer, String model_version, String model_name, String ip, String model_desc, String timezone) {
        Call<ResponseBody> call = getApi().ADD_GATEWAY(userId,Gatewayid,authCode,lat,aLong,name,mac,apn,WIFISSID,secretKey,type,fw_ver,manufacturer,model_version,model_name,ip,model_desc,timezone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_ADD_GATEWAY, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_ADD_GATEWAY, t.getMessage());
            }
        });
    }

    public void CheckDevice(String device_id,String User_id) {

        Call<ResponseBody> call = getApi().CHECK_DEVICE_INFO(device_id,User_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_CHECK_DEVICE_INFO, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_CHECK_DEVICE_INFO, t.getMessage());
            }
        });
    }

    public void GET_ROOMS(String userId) {

        Call<ResponseBody> call = getApi().GET_ROOMS(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_GET_ROOM_INFO, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_GET_ROOM_INFO, t.getMessage());
            }
        });
    }
    public void GET_GATEWAYS(String userId) {

        Call<ResponseBody> call = getApi().GET_GATEWAYS(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_GET_GATEWAY_INFO, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_GET_GATEWAY_INFO, t.getMessage());
            }
        });
    }

    public void ADD_ROOM(String userId, String gatewayID, String room_name) {
        Call<ResponseBody> call = getApi().ADD_ROOM(userId,gatewayID,room_name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_ADD_ROOM, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_ADD_ROOM, t.getMessage());
            }
        });

    }

    public void GET_DEVICES(String userId) {

        Call<ResponseBody> call = getApi().GET_DEVICES(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_GET_DEVICES, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_GET_DEVICES, t.getMessage());
            }
        });

    }

    public void DELETE_GATEWAY(String gatewayId) {
        Call<ResponseBody> call = getApi().DELETE_GATEWAY(gatewayId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_DELETE_GATEWAY, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_DELETE_GATEWAY, t.getMessage());
            }
        });
    }

    public void VERIFY_OTP(String otp,String mobile) {

        Call<ResponseBody> call = getApi().VERIFY_OTP(mobile,otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_VERIFY_OTP, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_VERIFY_OTP, t.getMessage());
            }
        });
    }

    public void REGISTERUSER(String name, String email, String phone, String password) {
        Call<ResponseBody> call = getApi().Register_user(phone,name,email,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
                apiHitListener.onSuccessResponse(ApiIds.ID_REGISTER_SUCCESS, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiHitListener.onFailResponse(ApiIds.ID_REGISTER_SUCCESS, t.getMessage());
            }
        });
    }
}