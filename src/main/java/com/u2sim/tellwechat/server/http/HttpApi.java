package com.u2sim.tellwechat.server.http;

import com.u2sim.tellwechat.bean.server.wsApp.WsAppBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by hanguojing on 2017/11/27 14:48
 */

public interface HttpApi {
    String URL_TEST = "http://123.56.7.243:5001/";
    String URL_LOG = "http://123.56.7.243:5001/applog";


    @Headers("Content-Type:application/json")
    @POST("api")
    Call<WsAppBean> getWebSocketHost(@Body RequestBody body);



    @Multipart
    @POST(URL_LOG)
    Call<ResponseBody> uploadLog(@Part MultipartBody.Part id, @Part MultipartBody.Part file);

}
