package com.pucungdev.mynotesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pucungdev.mynotesapp.db.DatabaseContact;

public class DatabaseHelper extends SQLiteOpenHelper {


    //DatabaseContact databaseContact = new DatabaseContact();

    private static String DATABASE_NAME = "noteappdb";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
                    DatabaseContact.TABLE_NOTE ,
                    DatabaseContact.NoteColums._ID,
                    DatabaseContact.NoteColums.TITLE ,
                    DatabaseContact.NoteColums.DESCRIPTION,
                    DatabaseContact.NoteColums.DATE
            );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,  null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContact.TABLE_NOTE);
        onCreate(db);
    }
}
