package com.sravan.efactorapp.RestClient;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by ubuntu on 1/8/16.
 */

public interface ApiHitListener {

    void onSuccessResponse(int apiId, Response<ResponseBody> response);

    void onFailResponse(int apiId, String error);
}