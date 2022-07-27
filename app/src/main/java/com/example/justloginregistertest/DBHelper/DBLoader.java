package com.example.justloginregistertest.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBLoader extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context mycontext;
    /**
    * 数据库操作语句
    */
    public static final String CREATE_FILE="create table file(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "password TEXT,"+
            "create_time DATETIME,"+
            "update_time DATETIME)";

    /**
    * 构造方法
    * 参数：上下文（即this），数据库名，工厂用null，版本号
    */
    public DBLoader(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mycontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db = this.getReadableDatabase();//打开或新建数据库，磁盘空间满时用只读方式打开
        db.execSQL(CREATE_FILE);//规范数据库，id主键、创建时间、更新时间必需
        Toast.makeText(mycontext,"Create table file succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
