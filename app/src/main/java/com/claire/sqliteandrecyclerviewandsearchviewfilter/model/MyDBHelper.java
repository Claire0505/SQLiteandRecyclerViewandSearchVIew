package com.claire.sqliteandrecyclerviewandsearchviewfilter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by claire on 2017/11/10.
 * SQLiteOpenHelper類別，在應用程式中執行建立資料庫與表格的工作
 */

public class MyDBHelper extends SQLiteOpenHelper{
    //資料庫名稱
    public static final String DATABASE_NAME = "mydata.db";
    //資料庫版本，資料結構改變的時後要更改這個數字，通常是加一
    public static final int VERSION = 1;
    //資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    //需要資料庫的元件呼叫這個方法
    public static SQLiteDatabase getDatabase (Context context){
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, DATABASE_NAME, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立應用程式的表格
        db.execSQL(ItemDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //刪除原有表格
        db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.TABLE_NAME);

        //呼叫onCreate建立新版的表格
        onCreate(db);
    }
}
