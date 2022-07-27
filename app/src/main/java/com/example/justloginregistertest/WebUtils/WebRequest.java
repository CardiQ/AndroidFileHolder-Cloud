package com.example.justloginregistertest.WebUtils;

import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WebRequest {
    //成员声明
    Retrofit mRetrofit;
    MyService mService;

    //String url="http://192.168.4.11:8080/test/";//TD 5G
    String url="http://192.168.0.4:8080/test/";//LAB
    String TAG = "请求内容：";

    /**
     * 1.添加依赖
     * 2.创建Retrofit对象
     */
    public void build1() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        //在创建对象时就确定了服务器/APi
        mService = mRetrofit.create(MyService.class);
    }

    /*public void build2() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mService = mRetrofit.create(MyService.class);
    }*/

    public void get(int id) {
        //调用后端方法
        Call<ResponseBody> call = mService.myGet(id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {//响应成功
                Log.d(TAG, "myGet: " + response.body().toString());
                //textView.setText("myGet: " + response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {//响应失败

            }
        });
    }

    public void post(String usname,String pwd) throws IOException {
        Call<ResponseBody> call = mService.myPost(usname,pwd);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "myPost: " + response.body().toString());
                //textView.setText("myPost: " + response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
