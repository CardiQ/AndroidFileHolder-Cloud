package com.example.justloginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private String name;
    private Handler mHandler;
    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //注册控件
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        tv1 = findViewById(R.id.tv_name);

        //设置事件监听器
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        //接收信息
        Intent intent = getIntent();
        /**
         * ？？有一个问题，就是需要每个活动之间都要传递这个name值，应该有其他方式改变这个name值；
         * 目前使之为一个定值77
         */
            name = intent.getStringExtra("nameMain");

            //                  在handler中更新UI
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    tv1.setText(name);
                }                ;
            };
            class testThread extends Thread {
                public void run() {
                    Message message = new Message();
                    mHandler.sendMessage(message);
                }
            }
//                     启动线程
            new testThread().start();
        }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.button1:
                intent = new Intent(this, MainActivity.class);
                finish();
                break;
            case R.id.button2:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
        finish();//跳转后没有结束生命周期会崩
    }
}