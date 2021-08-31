package com.sravan.efactorapp.RestClient;


import com.sravan.efactorapp.retrofit.RestService;

public class BaseArguments {
    public static final String ARG_EMAIL = "email";
    public static final String ARG_TYPE = "type";

    //Registration screen params
    public static final String ARG_FIRST_NAME = "firstname";
    public static final String ARG_LAST_NAME = "lastname";
    public static final String ARG_PASSWORD = "password";
    public static final String ARG_MOBILE_NO = "mobileno";
    public static final String ARG_ADDRESS = "address";
    public static final String ARG_LATITUDE = "latitude";
    public static final String ARG_LONGITUDE = "longitude";
    public static final String ARG_MOBILE_CODE = "mobile_code";
    public static final String ARG_CITY = "city";
    public static final String ARG_COUNTRY = "country";
    public static final String ARG_STATE = "state";
    public static final String ARG_IMAGE = "image";
    public static final String ARG_DEVICE_ID = "device_id";
    public static final String ARG_DEVICEID2 = "deviceid";
    public static final String ARG_DEVICE_TOKEN = "device_token";
    public static final String ARG_DEVICE_TYPE = "device_type";
    public static final String ARG_LOGIN_FROM = "loginfrom";
    public static final String ARG_OTP = "otp";
    public static final String ARG_Authorization = "Authorization";
    public static final String ARG_OLDPASSWORD = "oldpassword";
    public static final String ARG_NEW_PASSWORD = "newpassword";
    public static final String ARG_LATLNG = "latlng";
    public static final String ARG_PLACE_ID = "placeid";
    public static final String ARG_SENSOR = "sensor";
    public static final String ARG_KEY = "key";
    public static final String ARG_STATUS = "status";
    public static final String ARG_ACCOUNT_NAME = "account_name";
    public static final String ARG_BANK_NAME = "bank_name";
    public static final String ARG_BRANCH_CODE = "branch_code";
    public static final String ARG_PHONEPE = "phonepe";
    public static final String ARG_GPAY = "gpay";
    public static final String ARG_PAYTM = "paytm";
    public static final String ARG_ACCOUNT_NO = "account_no";
    public static final String ARG_PACKAGE_CATEGORY = "package_category";
    public static final String ARG_PACKAGE_PRICE = "package_price";
    public static final String ARG_cloth_category_id = "cloth_category_id";
    public static final String ARG_cloth_type_id = "cloth_type_id";
    public static final String ARG_cloth_name_id = "cloth_name_id";
    public static final String ARG_price = "price";
    public static final String ARG_CATOGERYID = "category_id";
    public static final String ARG_order_id = "order_id";
    public static final String ARG_delivery_boy_id = "delivery_boy_id";
    public static final String ARG_dryclean_pricing_id = "dryclean_pricing_id";
    public static final String package_category_id = "package_category_id";
    public static final String package_id = "package_id";


//.........................API NAMES.............................//

    public static final String NEW_USER = "newuser";
    public static final String RESEND_OTP = "resendotp";
    public static final String NEWUSERSTEP2 = "newuserstep2";
    public static final String USER_LOGIN = "api.php?action=login";
    public static final String FORGET_PASSWORD = "forgotpassword";
    public static final String VERIFY_OTP = "verifyotp";
    public static final String TERMS_CONDITIONS = "terms_conditions";
    public static final String PRIVACY_POLICY = "privacy_policy";
    public static final String COUNTRIES = "countries";
    public static final String UPDATE_PROFILE = "updateprofile";
    public static final String CHANGE_PASSWORD = "changepassword";
    public static final String GEOCODE = "geocode/json";
    public static final String PLACE_DETAILS = "place/details/json";
    public static final String LOGOUT = "logout";
    public static final String GO_ONLINE = "go_online/{status}";
    public static final String UPDATE_BANK_DETAILS = "updatebankdetails";
    public static final String WALLETRECHARGE = "wallet_recharge";
    public static final String GET_BANK_DETAILS = "get_bank_details";
    public static final String AddPackage_Fold_IRON = "add_package";
    public static final String CLOTH_CATEGORIES = "clothcategories";
    public static final String CLOTH_TYPES = "clothtypes";
    public static final String CLOTH_NAMES = "clothnames";
    public static final String ADD_DRYCLEAN_PRICING = "add_dryclean_pricing";
    public static final String ORDER_LIST = "order_list";
    public static final String DECLINE_ORDER = "decline_order";
    public static final String ACCEPT_ORDER = "accept_order";
    public static final String DELIVERY_BOY_LIST = "delivery_boy_list";
    public static final String ASSIGN_PICKUP_BOY = "assign_delivery_boy";
    public static final String PROCESS_ORDER = "process_order";
    public static final String DRYCLEAN_PRICING_LIST = "dryclean_pricing_list";
    public static final String DELETE_DRYCLEAN_PRICING = "delete_dryclean_pricing";
    public static final String UPDATE_DRYCLEAN_PRICING = "update_dryclean_pricing";
    public static final String WALLET_HISTORY = "get_wallet_history";

    public static final String ORDER_DETAILS = "order_details";
    public static final String UPDATE_TOKEN = "update_token";
    public static final String GET_PACKAGE_DATA = "get_package_data";
    public static final String PUSHER_AUTH_PRIVATE = RestService.API_BASE_URL + "pusher_auth_private";
    public static final String PUSHER_KEY = "86778d9a12b448737a9e";

    public static String UserWallateRechagesCallBack = RestService.API_BASE_URL + "razorpay_wallet_callback";
}
