package com.claire.sqliteandrecyclerviewandsearchviewfilter.model;

import java.util.Date;
import java.util.Locale;

/**
 * Created by claire on 2017/11/10.
 */

public class Item {
    private long id;
    private long datetime;
    private String title;
    private String content;

    public Item(){

    }

    public Item(long id, long datetime, String title, String content) {
        this.id = id;
        this.datetime = datetime;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    //裝置區域的日期時間
    public String getLocationDatetime(){
        return String.format(Locale.getDefault(), "%tF %<tR", new Date(datetime));
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
