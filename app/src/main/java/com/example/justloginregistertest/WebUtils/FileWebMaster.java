package com.example.justloginregistertest.WebUtils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileWebMaster {
    RService rService;
    Retrofit retrofitup, retrofitdown;
    final String url="http://192.168.0.4:8080/test/";



//使用retrofit上传文件

    /**
     * 上传文件
     *
     * @param filePathName 文件路径及文件名
     */
    public void uploadFile(String filePathName) {
        // 生成Retrofit
        retrofitup = new Retrofit.Builder()
                .baseUrl(url)//本机网络路径！！至关重要
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 生成Service
        rService = retrofitup.create(RService.class);

        // 要上传的文件
        File file = new File(filePathName);

        // 执行请求
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


        Call<ResponseBody> call = rService.upLoadFile(part);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("TAG", "UP SUCCEEDED");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("TAG", "UP FAILED");
                t.printStackTrace();
            }
        });
    }

    //下载文件 path=/data/data/com.example.justloginregistertest/files
    public void downloadFile(final String path) {
        //生成retrofit
        retrofitdown = new Retrofit.Builder()
                .baseUrl(url)
                //通过线程池获取一个线程，指定callback在子线程中运行。
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();

        // 生成Service
        rService = retrofitdown.create(RService.class);

        //下载请求
        RService request = retrofitdown.create(RService.class);
        Call<ResponseBody> call = request.downloadFile();//当你填进去的url是完整url的时候，设置的baseurl是无效的
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                //将Response写入到从磁盘中，详见下面分析   注意，这个方法是运行在子线程中的
                writeResponseToDisk(path, response);
                Log.d("TAG", "DOWN SUCCEEDED"+"测试    线程名 = " + Thread.currentThread().getName());
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
                Log.d("TAG", "DOWN FAILED" + "  线程名 = " + Thread.currentThread().getName());
            }
        });

    }

    private void writeResponseToDisk(String path, Response<ResponseBody> response) {
        //从response获取输入流以及总大小
        writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength());
    }

    private static int sBufferSize = 8192;
    //将输入流写入文件
    private void writeFileFromIS(File file, InputStream is, long totalLength) {
        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "CREATE FILE FAILED" + "  线程名 = " + Thread.currentThread().getName());
            }
        }
        OutputStream outputStream = null;
        long currentLength = 0;
        try {
            outputStream = new FileOutputStream(file);
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                outputStream.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                //Log.d("TAG", "下载 进度 = " + (int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            Log.d("TAG","下载完成 file.getAbsolutePath() = "+file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG","DOWNLOAD FAILED");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
