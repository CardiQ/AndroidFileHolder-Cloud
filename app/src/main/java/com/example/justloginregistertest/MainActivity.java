package com.example.justloginregistertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.justloginregistertest.FileExplorer.FileExplorerActivity;
/**
 * Created by littlecurl 2018/6/24
 */

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentLog=getIntent();
        name=intentLog.getStringExtra("nameLog");

        initView();//封装简洁+望文生义
    }

    private void initView() {
        // 初始化控件对象
        Button mBtMainLogout = findViewById(R.id.bt_main_logout);
        // 绑定点击监听器
        mBtMainLogout.setOnClickListener(this);
    }

    //不用内部类，onClick方法写在外好在公用
    public void onClick(View view) {
        Intent intent = new Intent();
        int flag=1;
        switch (view.getId()) {
            /*case R.id.btn1:
                intent.setClass(this,ClockActivity.class);
                break;*/
            case R.id.btn2:
                intent.setClass(this,UserActivity.class);
                intent.putExtra("nameMain","77");
                break;
            case R.id.btn3:
                flag=1;
                intent.setClass(this,FileExplorerActivity.class);
                intent.putExtra("flag",flag);
                break;
            case R.id.btn4:
                flag=2;
                intent.setClass(this, FileExplorerActivity.class);
                intent.putExtra("flag",flag);
                break;
                /*  case R.id.btn5:
                intent.setClass(this,CallPhoneActivity.class);
                break;*/
            case R.id.bt_main_logout:
                intent = new Intent(this, loginActivity.class);
                break;
        }
        startActivity(intent);
        finish();//跳转后没有结束生命周期会崩
    }
}
