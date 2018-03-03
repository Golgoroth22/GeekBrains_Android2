package com.falin.valentin.a2_l1.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.falin.valentin.a2_l1.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesTable {
    public static final String TABLE_NOTES_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_TEXT = "text";

    private static String[] notesAllColums = {
            COLUMN_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT
    };

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES_NAME + " (" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_TEXT + " TEXT);");
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_NAME);
    }

    public static void addNote(Note note, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_TEXT, note.getText());

        db.insert(TABLE_NOTES_NAME, null, values);
    }

    public static void editNote(Note note, Note newNote, SQLiteDatabase db) {
        String noteTitle = note.getTitle();
        if (noteTitle == null) {
            noteTitle = " ";
        }
        db.execSQL("UPDATE " + TABLE_NOTES_NAME + " SET " + COLUMN_NOTE_TITLE + " = '" +
                newNote.getTitle() + "', " + COLUMN_NOTE_TEXT + " = '" + newNote.getText() +
                "' WHERE " + COLUMN_NOTE_TITLE + " = '" + noteTitle + "';");
    }

    public static void deleteNote(String noteTitle, SQLiteDatabase db) {
        db.delete(TABLE_NOTES_NAME, COLUMN_NOTE_TITLE + " = '" + noteTitle + "'", null);
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NOTES_NAME, null, null);
    }

    public static List<Note> getAllNotes(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NOTES_NAME, notesAllColums, null, null, null, null, null);
        return getResultFromCursor(cursor);
    }

    private static List<Note> getResultFromCursor(Cursor cursor) {
        List<Note> result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(COLUMN_NOTE_TITLE);
            int textIndex = cursor.getColumnIndex(COLUMN_NOTE_TEXT);

            int i = 0;

            do {
                result.add(new Note());
                result.get(i).setId(cursor.getInt(idIndex));
                result.get(i).setTitle(cursor.getString(titleIndex));
                result.get(i).setText(cursor.getString(textIndex));

                i++;
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return result == null ? new ArrayList<Note>(0) : result;
    }
}
