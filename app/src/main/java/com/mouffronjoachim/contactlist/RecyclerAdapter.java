package com.mouffronjoachim.contactlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private final ArrayList<String> characters;

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public RecyclerAdapter(Context context, ArrayList<String> wordList) {
        this.characters = wordList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        String currentMessage = characters.get(position);
        holder.write(currentMessage);
    }
}
