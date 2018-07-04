package com.pucungdev.mynotesapp.db;

import android.provider.BaseColumns;

public class DatabaseContact {
    static String TABLE_NOTE = "note";

    public static class NoteColums implements BaseColumns{

        //Note title
        public static final String TITLE = "title";
        //Note description
        public static final String DESCRIPTION = "description";
        //Note date
        public static final String DATE = "date";

    }


}

