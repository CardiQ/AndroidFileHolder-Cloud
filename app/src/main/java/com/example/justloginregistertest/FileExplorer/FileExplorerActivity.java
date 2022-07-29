package com.example.justloginregistertest.FileExplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justloginregistertest.FileExpressActivity;
import com.example.justloginregistertest.MainActivity;
import com.example.justloginregistertest.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileExplorerActivity extends AppCompatActivity {

    TextView pathTv;
    ImageButton backBtn;
    ListView fileLv;
    File root;
    File currentParent;
    File[] currentFiles;

    private int flag;
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        pathTv = findViewById(R.id.tv_filepath);
        backBtn = findViewById(R.id.btn_back);
        fileLv = findViewById(R.id.lv_file);


        //接收控制flag
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);

        //注意采用了静态申请权限，需手动开关
        fileExplorer();
    }

    //获取根目录
    public File getRootFile(Context context) {
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // /storage/emulated/0/Android/data/com.example.demo/files
            File externalFileDir = context.getExternalFilesDir(null);
            do {
                externalFileDir = Objects.requireNonNull(externalFileDir).getParentFile();
            } while (Objects.requireNonNull(externalFileDir).getAbsolutePath().contains("/Android"));
            file = Objects.requireNonNull(externalFileDir);

            /**
             * 判断是否装载SD卡，但有问题？？
             * 不可行；以下写了两种方法；
             */
            /*String status = Environment.getExternalStorageState();
            boolean isLoadSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (!status.equals(Environment.MEDIA_MOUNTED)||!isLoadSDCard)
                file=null;*/
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile().toURI());
        }
        return file;
    }

    public void fileExplorer() {
        //获取根目录再判断是否装载SD卡，！！注意与视频旧写法不同
        //root =  this.getExternalFilesDir(null);
        if (flag == 2) {
            //这是访问SD卡
            root = getRootFile(this);
        } else if (flag == 1) {
            //这是访问数据库
            root = new File("/data/data/com.example.justloginregistertest");
        }
        if (root != null) {
            //获取SD卡根目录
            //root= getExternalFilesDir(null);
            currentParent = root;
            //获取当前文件夹下所有文件到文件数组
            currentFiles = currentParent.listFiles();

            inflateListView(currentFiles);
        } else {
            Toast.makeText(this, "SD卡没有被装载", Toast.LENGTH_SHORT).show();
        }

        setListener();
    }

    //渲染ListView
    private void inflateListView(File[] currentFiles) {
        //对应参数要求建立含Map的List,存放文字和图标键值对的map
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < currentFiles.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("filename", currentFiles[i].getName());
            //如果是文件
            if (currentFiles[i].isFile()) {
                map.put("icon", R.drawable.file);//是文件放文件图标
            } else {
                map.put("icon", R.drawable.icon);//是文件夹放文件夹图标
            }
            list.add(map);
        }

        //创建适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.item_file_explorer, new String[]{"filename", "icon"}, new int[]{R.id.item_tv, R.id.item_icon});
        fileLv.setAdapter(simpleAdapter);
        pathTv.setText("当前路径：" + currentParent.getAbsolutePath());
    }

    //设置监听事件
    private void setListener() {
        fileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isFile()) {
                    /**
                     *目前只能读取文本文件？？
                     */
                    String term = currentFiles[position].toString();//路径确切
                    inputText = new FileReader().load(term);

                    if (!TextUtils.isEmpty(inputText)) {
                        Intent intentFile = new Intent();
                        intentFile.setClass(FileExplorerActivity.this, FileExpressActivity.class);
                        intentFile.putExtra("text",inputText);
                        startActivity(intentFile);
                        finish();
                    } else
                        Toast.makeText(FileExplorerActivity.this, "无法打开此文件", Toast.LENGTH_SHORT).show();
                } else {
//                获取当前点击的文件夹当中的所有文件
                    File[] temp = currentFiles[position].listFiles();
                    if (temp == null || temp.length == 0) {
                        Toast.makeText(FileExplorerActivity.this, "当前文件夹没有内容或者不能被访问", Toast.LENGTH_SHORT).show();
                    } else {
//                    修改被点击的这项父目录
                        currentParent = currentFiles[position];
                        currentFiles = temp;
//                    数据源发生改变，重新设置适配器内容
                        inflateListView(currentFiles);
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 判断当前的目录是否为sd卡的根目录，如果是根目录，就直接退出activity。
                 * 如果不是根目录，就获取当前目录的父目录，然后获得父目录的文件，在重新加载listview
                 * */
                if (currentParent == null || currentParent.getAbsolutePath().equals(root.getAbsolutePath())) {
                    Intent intent = new Intent(FileExplorerActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    currentParent = currentParent.getParentFile();
                    currentFiles = currentParent.listFiles();
                    inflateListView(currentFiles);
                }
            }
        });
    }
}