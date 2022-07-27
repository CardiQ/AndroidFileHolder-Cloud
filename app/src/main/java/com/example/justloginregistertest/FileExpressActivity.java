package com.example.justloginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.justloginregistertest.FileExplorer.FileExplorerActivity;

public class FileExpressActivity extends AppCompatActivity {

    //不是Button而是ImageButton易错
    private ImageButton button_file;
    private TextView textView;
    private Handler mHandler;
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_express);

        button_file=findViewById(R.id.btn_back_for_file);
        textView=findViewById(R.id.text_for_file);

        Intent intent1=getIntent();
        inputText=intent1.getStringExtra("text");

        //                  在handler中更新UI
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                textView.setText(inputText);
            };
        };
        class testThread extends Thread{
            public void run() {
                Message message = new Message();
                mHandler.sendMessage(message);
            }
        }
//                     启动线程
        new testThread().start();

        button_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(FileExpressActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }
}