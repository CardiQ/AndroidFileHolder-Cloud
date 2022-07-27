package com.example.justloginregistertest.WebUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 服务器/APi 接口
 */
public interface MyService {
    @GET("get1")//对应GET请求-后端方法名为get1
    Call<ResponseBody> myGet(@Query("id") int id);//前端方法名为对象的myGet方法-对应查找后端的“id”

    @POST("post1")//对应POST请求-后端方法post1
    @FormUrlEncoded
    Call<ResponseBody> myPost(@Field("username") String username
            , @Field("password") String password);
}
