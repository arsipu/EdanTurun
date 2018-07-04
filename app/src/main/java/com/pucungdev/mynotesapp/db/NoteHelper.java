package com.pucungdev.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pucungdev.mynotesapp.entity.Note;

import java.util.ArrayList;

public class NoteHelper {

    private static final String DATABASE_TABLE = DatabaseContact.TABLE_NOTE;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }


    public NoteHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }


    public void close(){
        databaseHelper.close();
    }

    public ArrayList<Note> query(){
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,null,null,null,null,DatabaseContact.NoteColums._ID+" DESC",null);
        cursor.moveToFirst();

        Note note;

        if (cursor.getCount()>0){
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContact.NoteColums._ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContact.NoteColums.TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContact.NoteColums.DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContact.NoteColums.DATE)));

                list.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return list;
    }

    public long insert(Note note){
        ContentValues arg = new ContentValues();
        arg.put(DatabaseContact.NoteColums.TITLE,note.getTitle());
        arg.put(DatabaseContact.NoteColums.DESCRIPTION,note.getDescription());
        arg.put(DatabaseContact.NoteColums.DATE,note.getDate());

        return database.insert(DATABASE_TABLE,null,arg);
    }


    public int update(Note note){
        ContentValues arg = new ContentValues();
        arg.put(DatabaseContact.NoteColums.TITLE,note.getTitle());
        arg.put(DatabaseContact.NoteColums.DESCRIPTION,note.getDescription());
        arg.put(DatabaseContact.NoteColums.DATE,note.getDate());
        return database.update(DATABASE_TABLE,arg,DatabaseContact.NoteColums._ID+" = '"+note.getId()+"'",null);
    }

    public int delete(int id){
       return database.delete(DATABASE_TABLE,DatabaseContact.NoteColums._ID+" = '"+id+"'",null);
    }


}
