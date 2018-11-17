package com.mouffronjoachim.contactlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private final ArrayList<String> characters;
    private LayoutInflater mInflater;

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public RecyclerAdapter(Context context,
                           ArrayList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.characters = wordList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_recycler_view, parent, false);
        return new CustomViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String currentMessage = characters.get(position);
        holder.write(currentMessage);
    }
}
