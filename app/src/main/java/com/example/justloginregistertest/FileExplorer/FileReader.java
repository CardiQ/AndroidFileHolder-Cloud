package com.example.justloginregistertest.FileExplorer;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class FileReader extends AppCompatActivity {
    //读取文本文件
    private FileInputStream in=null;
    private FileOutputStream out=null;
    private BufferedReader reader=null;
    private StringBuilder content=new StringBuilder();

    public String load(String name){
        try{
            //打开文件data，没有就创建，路径/data/data/com.example.justloginregistertest/files
            /*out=openFileOutput(name,Context.MODE_APPEND);
            out.close();*/
            File file=new File(name);
            in = new FileInputStream(file);
            reader=new BufferedReader(new InputStreamReader(in));
            //按行读取
            String line="";
            while((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
