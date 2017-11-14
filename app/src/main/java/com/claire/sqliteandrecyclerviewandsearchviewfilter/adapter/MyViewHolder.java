package com.claire.sqliteandrecyclerviewandsearchviewfilter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.claire.sqliteandrecyclerviewandsearchviewfilter.R;

/**
 * Created by claire on 2017/11/13.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title_text, datetime_text;
    public ImageView image_edit, image_delete;

    public MyViewHolder(View itemView) {
        super(itemView);
        title_text = itemView.findViewById(R.id.title_Text);
        datetime_text = itemView.findViewById(R.id.datetime_Text);
        image_edit = itemView.findViewById(R.id.image_edit);
        image_delete = itemView.findViewById(R.id.image_delete);
    }
}
