package com.example.justloginregistertest;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.justloginregistertest.FileExplorer.FileExplorerActivity;
import com.example.justloginregistertest.WebUtils.FileWebMaster;
/**
 * Created by littlecurl 2018/6/24
 */

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //请求码是唯一值即可
    String name;
    final Integer REQUEST_CODE = 1;
    FileWebMaster master=new FileWebMaster();

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

    // 打开系统的文件选择器
    public void pickFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    // 获取文件的真实路径//Intent { dat=content://com.android.providers.downloads.documents/document/raw:/storage/emulated/0/Download/files/text.txt flg=0x1 }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        // 通过ContentProvider查询文件路径
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // 未查询到，说明为普通文件，可直接通过URI获取文件路径
            String path = uri.getPath();
            /**
             * 进行上传
             */
            new Thread() {
                @Override
                public void run() {
                    master.uploadFile(path);
                }
            }.start();
            return;
        }
        if (cursor.moveToFirst()) {
            // 多媒体文件，从数据库中获取文件的真实路径
            int term = cursor.getColumnIndex("_data");
            String path = "/storage/self/primary/Download/files/text2.txt";
            //String path = cursor.getString(term >= 0 ? term : 0);
            /**
             * 进行上传
             */
            new Thread() {
                @Override
                public void run() {
                    master.uploadFile(path);
                }
            }.start();
        }
        cursor.close();
    }

    //下载文件
    public void loadFile(View view){
        String path="/data/data/com.example.justloginregistertest/files/term.txt";//？？暂用下载准备路径
        new Thread(){
            @Override
            public void run() {
                master.downloadFile(path);
            }
        }.start();
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
