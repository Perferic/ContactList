package com.mouffronjoachim.contactlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;



public class CustomViewHolder extends RecyclerView.ViewHolder {
    private TextView message;

    public CustomViewHolder(TextView v) {
        super(v);
        message = v;
    }

    public void write(String messageCustomized) {
        message.setText(messageCustomized);
    }
}
