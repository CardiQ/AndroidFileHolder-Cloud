package com.example.justloginregistertest.FileExplorer;

import java.io.File;
import java.io.IOException;

public class FileNewer {
    final static String dpath="/data/data/com.example.justloginregistertest/download/";
    final static String fpath="/data/data/com.example.justloginregistertest/download/";

    public static void DirectoryMaker(String dname){
        File tempFile=new File(dpath+dname);
        if(!tempFile.exists()){
            tempFile.mkdir();
        }
    }

    public static void FileMaker(String dname,String fname){
        File tempFile=new File(dpath+dname);
        File file;
        if(!tempFile.exists()){
            tempFile.mkdir();
        }else {
            file=new File(dpath+dname+File.separator+fpath+fname);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
