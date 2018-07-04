package com.pucungdev.mynotesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.pucungdev.mynotesapp.db.NoteHelper;
import com.pucungdev.mynotesapp.entity.Note;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvNotes;
    ProgressBar progressBar;
    FloatingActionButton fabAdd;

    private LinkedList<Note> list;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes     = (RecyclerView)findViewById(R.id.rv_notes);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        fabAdd      = (FloatingActionButton)findViewById(R.id.fab_add);

        adapter     = new NoteAdapter(this);
        noteHelper  = new NoteHelper(this);
        noteHelper.open();


        list = new LinkedList<>();

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        rvNotes.setAdapter(adapter);

        fabAdd.setOnClickListener(this);

        new LoadNoteAsyc().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    class LoadNoteAsyc extends AsyncTask<Void,Void,ArrayList<Note>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            list.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);
            list.addAll(notes);
            adapter.setListNote(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return noteHelper.query();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                new LoadNoteAsyc().execute();
                showSnackbarMessage("Satu item berhasil ditambahkan");
                // rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), 0);
            }
        }
        else if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE) {

            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE) {
                new LoadNoteAsyc().execute();
                showSnackbarMessage("Satu item berhasil diubah");
                // int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                // rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), position);
            }

            else if (resultCode == FormAddUpdateActivity.RESULT_DELETE) {
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                list.remove(position);
                adapter.setListNote(list);
                adapter.notifyDataSetChanged();
                showSnackbarMessage("Satu item berhasil dihapus");
            }
        }
        }



    private void showSnackbarMessage(String msg) {
        Snackbar.make(rvNotes, msg, Snackbar.LENGTH_SHORT).show();
    }
}
