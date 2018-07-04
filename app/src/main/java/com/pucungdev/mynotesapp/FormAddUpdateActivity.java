package com.pucungdev.mynotesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pucungdev.mynotesapp.db.NoteHelper;
import com.pucungdev.mynotesapp.entity.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_NOTE = "extra_note";

    private EditText edtTitle,edtDesc;
    private Button btnSave;

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;
    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;


    private Note note;
    private int position;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);
        edtTitle    = (EditText) findViewById(R.id.edt_title);
        edtDesc     = (EditText) findViewById(R.id.edt_description);
        btnSave     = (Button) findViewById(R.id.btn_submit);
        btnSave.setOnClickListener(this);


        noteHelper  = new NoteHelper(this);
        noteHelper.open();

        note        = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note!=null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit){
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtTitle.setText(note.getTitle());
            edtDesc.setText(note.getDescription());
        }else{
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave.setText(btnTitle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit){

            String title = edtTitle.getText().toString().trim();
            String description = edtDesc.getText().toString().trim();

            boolean isEmpty = false;

            /*
            Jika fieldnya masih kosong maka tampilkan error
             */
            if (TextUtils.isEmpty(title)){
                isEmpty = true;
                edtTitle.setError("Field can not be blank");
            }


            if (!isEmpty){
                Note newNote = new Note();

                newNote.setTitle(title);
                newNote.setDescription(description);
                Intent intent = new Intent();

                if (isEdit){
                    newNote.setDate(note.getDate());
                    newNote.setId(note.getId());
                    noteHelper.update(newNote);
                    intent.putExtra(EXTRA_POSITION,position);
                    setResult(RESULT_UPDATE,intent);
                    finish();
                } else {
                    newNote.setDate(getCurrentDate());
                    noteHelper.insert(newNote);
                    setResult(RESULT_ADD);
                    finish();
                }


            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isEdit) {

            getMenuInflater().inflate(R.menu.form_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.delete :
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home :
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {

        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        }else{
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage)
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isDialogClose){
                            finish();
                        } else {
                            noteHelper.delete(note.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private String getCurrentDate() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
