package com.falin.valentin.a2_l1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        NotesTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        NotesTable.onUpgrade(db);
        onCreate(db);
    }
}
