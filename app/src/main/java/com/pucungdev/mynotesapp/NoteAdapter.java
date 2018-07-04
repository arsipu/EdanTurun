package com.pucungdev.mynotesapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucungdev.mynotesapp.entity.Note;

import java.util.ArrayList;
import java.util.LinkedList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private LinkedList<Note> data;
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Note> getListNote(){
        return data;
    }

    public void setListNote(LinkedList<Note> data){
        this.data = data;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.decription.setText(data.get(position).getDescription());
        holder.date.setText(data.get(position).getDate());

        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity,FormAddUpdateActivity.class);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION,position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE,getListNote().get(position));
                activity.startActivityForResult(intent,FormAddUpdateActivity.REQUEST_UPDATE);

            }
        }));

    }

    @Override
    public int getItemCount() {

        if (data!=null){
        return data.size();
        }
        else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title,decription,date;
        private CardView cvNote;

        public ViewHolder(View view) {
            super(view);
            title       = (TextView)view.findViewById(R.id.tv_title);
            date        = (TextView)view.findViewById(R.id.tv_date);
            decription  = (TextView)view.findViewById(R.id.tv_description);
            cvNote      = (CardView)view.findViewById(R.id.card_view);
        }
    }
}
