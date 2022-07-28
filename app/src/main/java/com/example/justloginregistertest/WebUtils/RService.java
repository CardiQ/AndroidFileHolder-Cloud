package com.example.justloginregistertest.WebUtils;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface RService {

    @Multipart
    @POST("post2")
    Call<ResponseBody> upLoadFile(//？？没用UploadBean
                                  @Part MultipartBody.Part file);

    //测试 下载文件
    @Streaming
    @GET("get2")
    Call<ResponseBody> downloadFile();//原有@URL
}


