package com.sravan.efactorapp.retrofit;

import com.sravan.efactorapp.RestClient.BaseArguments;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface Rest {

    @POST(BaseArguments.NEW_USER)
    @FormUrlEncoded
    Call<ResponseBody> newUser1(@Field(BaseArguments.ARG_EMAIL) String email);

    @POST(BaseArguments.RESEND_OTP)
    @FormUrlEncoded
    Call<ResponseBody> resendOtp(@Field(BaseArguments.ARG_EMAIL) String email,
                                 @Field(BaseArguments.ARG_TYPE) String type);

    @POST(BaseArguments.NEWUSERSTEP2)
    @Multipart
    Call<ResponseBody> registerUser(
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part part_pan,
            @Part MultipartBody.Part part_voter,
            @Part MultipartBody.Part part_aadhar);
    
    @Multipart
    @POST(BaseArguments.UPDATE_PROFILE)
    Call<ResponseBody> updateProfile(@Header(BaseArguments.ARG_Authorization) String auth,
                                     @PartMap Map<String, RequestBody> map,

                                     @Part MultipartBody.Part image);

    @POST(BaseArguments.USER_LOGIN)
    @FormUrlEncoded
    Call<ResponseBody> login(@Field(BaseArguments.ARG_EMAIL) String email,
                             @Field(BaseArguments.ARG_PASSWORD) String password);

    @POST(BaseArguments.FORGET_PASSWORD)
    @FormUrlEncoded
    Call<ResponseBody> forgetPassword(@Field(BaseArguments.ARG_EMAIL) String email);

    @POST(BaseArguments.VERIFY_OTP)
    @FormUrlEncoded
    Call<ResponseBody> newPassword(@Field(BaseArguments.ARG_OTP) int otp,
                                   @Field(BaseArguments.ARG_TYPE) String type,
                                   @Field(BaseArguments.ARG_PASSWORD) String password,
                                   @Field(BaseArguments.ARG_EMAIL) String email);


    @POST(BaseArguments.CHANGE_PASSWORD)
    @FormUrlEncoded
    Call<ResponseBody> changePassword(@Header(BaseArguments.ARG_Authorization) String auth,
                                      @Field(BaseArguments.ARG_OLDPASSWORD) String oldpassword,
                                      @Field(BaseArguments.ARG_NEW_PASSWORD) String newpassword);


    @POST(BaseArguments.LOGOUT)
    @FormUrlEncoded
    Call<ResponseBody> logout(@Header(BaseArguments.ARG_Authorization) String auth,
                              @Field(BaseArguments.ARG_DEVICEID2) String deviceid);

    @FormUrlEncoded
    @POST("api.php?action=mydevices")
    Call<ResponseBody> Get_My_Devices(@Field("userid") String user_id);

    @GET("api.php?action=getdevices")
    Call<ResponseBody> Get_ALL_Devices();

    @FormUrlEncoded
    @POST("api.php?action=add_device")
    Call<ResponseBody> Add_Device(@Field("user_id") String userid, @Field("gateway_id") String GatewayId,
                                  @Field("room_id") String roomId, @Field("device_id") String deviceid,
                                  @Field("device_name") String deviceName, @Field("device_type") String deviceType,
                                  @Field("device_ver") String deviceVer, @Field("device_fw_ver") String devicefw_ver,
                                  @Field("device_manufacturer") String deviceManufacturer,
                                  @Field("device_model_version") String devicemodelversion,
                                  @Field("device_model_name") String deviceModelName,
                                  @Field("device_model_desc") String devicemodeldesc,@Field("device_status_data") String statusData);

    @FormUrlEncoded
    @POST("api.php?action=otp")
    Call<ResponseBody> Send_OTP(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api.php?action=signup")
    Call<ResponseBody> Register_user(@Field("phone") String phone,@Field("name") String name,@Field("email") String email,@Field("password") String password);

    @POST("api.php?action=get_ticket_types")
    Call<ResponseBody> Ticket_Type();


    @FormUrlEncoded
    @POST("api.php?action=update_glucose_weight")
    Call<ResponseBody> Update_Readings(@Field("userid") String userid, @Field("glucose") String glucose,@Field("weight") String weight);

    @FormUrlEncoded
    @POST("api.php?action=getreadings")
    Call<ResponseBody> Readings_History(@Field("userid") String userid);


    @FormUrlEncoded
    @POST("api.php?action=raiseticket")
    Call<ResponseBody> Raise_Ticket(@Field("userid") String userid, @Field("ticket_type_id") String ticketId, @Field("title") String title, @Field("description") String discription, @Field("userfile[]") File images);

    @FormUrlEncoded
    @POST("api.php?action=getreadings")
    Call<ResponseBody> GET_GLUCO_READING(@Field("userid") String userid,@Field("date") String date);

    @FormUrlEncoded
    @POST("api.php?action=update_glucose_weight")
    Call<ResponseBody> ADD_GLUCO_READING(@Field("userid") String userid,@Field("glucose") String glucosereading,@Field("weight") String weight,@Field("date") String date,@Field("time") String time);

    @FormUrlEncoded
    @POST("api.php?action=addgateway")
    Call<ResponseBody> ADD_GATEWAY(@Field("user_id") String userId,@Field("gateway_id") String gatewayId,
                                   @Field("gateway_authcode") String gatewayAuthcode,@Field("gateway_lati") String gatewayLat,
                                   @Field("gateway_longi") String gatewayLong,@Field("gateway_name") String gatewayName,
                                   @Field("gateway_mac") String gateway_MAC,@Field("gateway_apn") String Gateway_APN,@Field("gateway_wifi_ssid") String wifiSSID,
                                   @Field("gateway_secret_key") String gatewaykey,@Field("gateway_type") String getwayType,@Field("gateway_fw_ver") String fw_ver,
                                   @Field("gateway_manufacturer") String manufacturer,@Field("gateway_model_version") String modelVersion,@Field("gateway_model_name") String modelName,
                                   @Field("gateway_ip") String ip,@Field("gateway_model_desc") String gatewayModel,@Field("gateway_timezone") String gatewayTimezone);

    @FormUrlEncoded
    @POST("api.php?action=check_device")
    Call<ResponseBody> CHECK_DEVICE_INFO(@Field("device_id") String device_id,@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("api.php?action=getrooms")
    Call<ResponseBody> GET_ROOMS(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("api.php?action=getgateways")
    Call<ResponseBody> GET_GATEWAYS(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("api.php?action=addroom")
    Call<ResponseBody> ADD_ROOM(@Field("user_id") String userId,@Field("gateway_id") String gatewayID,@Field("roomname") String room_name);


    @FormUrlEncoded
    @POST("api.php?action=getdevices_room")
    Call<ResponseBody> GET_DEVICES(@Field("room_id") String userId);

    @FormUrlEncoded
    @POST("api.php?action=deletegateway")
    Call<ResponseBody> DELETE_GATEWAY(@Field("id") String gatewayId);

    @FormUrlEncoded
    @POST("api.php?action=otpverification")
    Call<ResponseBody> VERIFY_OTP(@Field("phone") String phone,@Field("otp") String otp);
}