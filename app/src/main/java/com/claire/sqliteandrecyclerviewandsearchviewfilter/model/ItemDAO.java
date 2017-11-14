package com.claire.sqliteandrecyclerviewandsearchviewfilter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claire on 2017/11/10.
 * 資料功能類別 (資料存取物件 Data Access Object, DAO )
 */

public class ItemDAO {
    //表格名稱
    public static final String TABLE_NAME = "item";

    //編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    //其它表格名稱(參考來自 Item.class 的物件)
    public static final String DATETIME_COLUMN = "datetime";
    public static final String TITLE_COLUMN = "title";
    public static final String CONTENT_COLUMN = "content";

    //使用上面宣告的變數來建立表格的SQL指令 (注意字串前後的空白)
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATETIME_COLUMN + " INTEGER NOT　NULL, " +
            TITLE_COLUMN + " TEXT NOT NULL, " +
            CONTENT_COLUMN + " TEXT NOT NULL " + ")";

    //資料庫物件
    private SQLiteDatabase db;

    //建構子
    public ItemDAO(Context context){
        db = MyDBHelper.getDatabase(context);
    }
    //關閉資料庫
    public void closeDB(){
        db.close();
    }

    //新增參數指定的物件
    public Item insert(Item item){
        //建立新增資料的 ContentValues 物件
        ContentValues cv = new ContentValues();

        //加入 ContentValues 物件包裝的新增資料
        //cv.put(欄位名稱, 欄位資料)
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());

        //新增一筆資料並取的編號 db.insert(表格名稱, 沒有指定欄位值的預設值, 包裝新增資料的 cv 物件
        long id = db.insert(TABLE_NAME, null, cv);

        //設定編號
        item.setId(id);
        //回傳結果
        return item;

    }

    //修改參數指定的物件
    public boolean update(Item item){
        //建立修改資料的 ContentValues 物件
        ContentValues cv = new ContentValues();

        //加入 contentValues 物件的包裝的修改資料
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());

        //設定修改資料的條件編號 格式為「欄位名稱 = 資料」
        String where = KEY_ID + "=" + item.getId();

        //執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    //刪除參數指定的編號的資料
    public boolean delete(long id){
        //設定條件為編號，格式為「欄位名稱 = 資料」
        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    //讀取所有記事資料
    //Cursor 是一個 JAVA 介面，代表一個查詢後的結果，透過它的方法，可指向結果中的其中一筆記錄
    public List<Item> getAll(){
        List<Item> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        
        while (cursor.moveToNext()){
            result.add(getRecord(cursor));
        }
        
        cursor.close();
        return result;
    }

    //取得指定編號的資料物件
    public Item get(long id){
        //準備回傳結果用的物件
        Item item = null;
        //使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        //執行查詢
        Cursor result = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        //如果有查詢結果
        if (result.moveToFirst()){
            //讀取包裝一筆資料物件
            item = getRecord(result);
        }

        result.close();
        return item;
    }

    //把 Cursor 目前的資料包裝為物件
    private Item getRecord(Cursor cursor) {
        //準備回傳結果用的物件
        Item result = new Item();

        result.setId(cursor.getLong(0));
        result.setDatetime(cursor.getLong(1));
        result.setTitle(cursor.getString(2));
        result.setContent(cursor.getString(3));

        //回傳結果
        return result;
    }

    //取得資料數量
    public int getCount(){
        int result = 0;
        //rawQuery是SQLite的查詢語法。使用rawQuery會以Cursor型別傳回執行結果或查詢資料
        Cursor cursor  = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()){
            result = cursor.getInt(0);
        }

        return result;
    }


}
