package com.claire.sqliteandrecyclerviewandsearchviewfilter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.claire.sqliteandrecyclerviewandsearchviewfilter.R;
import com.claire.sqliteandrecyclerviewandsearchviewfilter.model.Item;
import com.claire.sqliteandrecyclerviewandsearchviewfilter.model.ItemDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by claire on 2017/11/13.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context mContext;
    private List<Item> items;
    private List<Item> filterList;
    private ItemDAO itemDAO;

    public MyRecyclerViewAdapter (Context mContext, List<Item> items){
        this.mContext = mContext;
        this.items = items;

        //將原始列表複製到過濾器列表中
        this.filterList = new ArrayList<Item>();
        this.filterList.addAll(this.items);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,  int position) {
        final Item singleItem = filterList.get(position); //filter
        holder.title_text.setText(singleItem.getTitle());
        holder.datetime_text.setText(singleItem.getLocationDatetime());

        //edit
        holder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               editTaskDialog(singleItem);
            }
        });

        //delete
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("刪除資料\n" + singleItem.getTitle());
                dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filterList.remove(singleItem);

                        itemDAO = new ItemDAO(mContext);
                        itemDAO.delete(singleItem.getId());
                        notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });

    }

    private void editTaskDialog(final Item singleItem) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.add_item, null);

        final EditText editTitle = subView.findViewById(R.id.editTitle);
        final EditText editContent = subView.findViewById(R.id.editContent);
        final TextView modifyDateTime = subView.findViewById(R.id.add_dateTime);

        if (singleItem != null){
            //如果資料不是空的,先取得目前項目資料
            editTitle.setText(singleItem.getTitle());
            editContent.setText(singleItem.getContent());
            modifyDateTime.setText(singleItem.getLocationDatetime());

            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("修改");
            dialog.setView(subView);
            dialog.create();

            dialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String titleStr = editTitle.getText().toString();
                    singleItem.setTitle(titleStr);

                    String contentStr = editContent.getText().toString();
                    singleItem.setContent(contentStr);

                    singleItem.setDatetime(new Date().getTime());
                    long modifyTime = new Date().getTime();
                    singleItem.setDatetime(modifyTime);
                    modifyDateTime.setText("modify:" + singleItem.getLocationDatetime());

                    if (TextUtils.isEmpty(titleStr)){
                        Snackbar.make(editTitle, "沒有資料", Snackbar.LENGTH_SHORT).show();
                    }else {

                        //要先初始化
                        itemDAO = new ItemDAO(mContext);
                        itemDAO.update(singleItem);

                        //set on UI Thread
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }

                }
            });
            dialog.setNegativeButton("取消", null);
            dialog.show();
        }


    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : items.size());
    }

    //SearView
    public void filter(final String text){
        filterList.clear();

        if (TextUtils.isEmpty(text)){
            filterList.addAll(items);
        } else {
            for (Item item : items){
                if (item.getTitle().contains(text) ||
                        item.getLocationDatetime().contains(text)){
                    filterList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
