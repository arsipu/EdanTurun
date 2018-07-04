package com.pucungdev.mynotesapp;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener{


    private int position;
    private OnItemClickCallback itemClickCallback;

    public CustomOnItemClickListener(int position, OnItemClickCallback itemClickCallback) {
        this.position = position;
        this.itemClickCallback = itemClickCallback;
    }

    @Override
    public void onClick(View view) {
        itemClickCallback.onItemClicked(view,position);
    }


    public interface OnItemClickCallback{
        void onItemClicked(View view,int position);
    }
}
