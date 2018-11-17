package com.mouffronjoachim.contactlist;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    private final TextView message;
    final RecyclerAdapter mAdapter;

    public CustomViewHolder(final View itemView, RecyclerAdapter adapter) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        this.mAdapter = adapter;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(itemView.getContext())
                    .setTitle("")
                    .setMessage("")
                    .show();
            }
        });
    }

    public void write(String messageCustomized) {
        message.setText(messageCustomized);
    }
}
