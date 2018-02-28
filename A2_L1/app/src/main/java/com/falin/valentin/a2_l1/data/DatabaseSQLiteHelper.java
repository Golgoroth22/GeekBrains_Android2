package com.falin.valentin.a2_l1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;
    static final String TABLE_NOTES_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_TEXT = "text";

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOTE_TITLE + " TEXT, " + COLUMN_NOTE_TEXT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_NAME);
        onCreate(db);

    }
}
