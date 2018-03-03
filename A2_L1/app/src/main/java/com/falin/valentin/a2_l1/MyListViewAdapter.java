package com.falin.valentin.a2_l1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.falin.valentin.a2_l1.data.FakeDB;
import com.falin.valentin.a2_l1.data.NotesTable;

public class MyListViewAdapter extends BaseAdapter {
    static String EXTRA_ID = "id";
    private Context context;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase database;

    MyListViewAdapter(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
        FakeDB.getDb().clear();
        FakeDB.getDb().addAll(NotesTable.getAllNotes(database));
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return FakeDB.getDb().size();
    }

    @Override
    public Object getItem(int i) {
        return FakeDB.getDb().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    void addElement() {
        Note note = new Note(" ", " ");
        FakeDB.getDb().add(note);
        NotesTable.addNote(note, database);
        Intent intent = new Intent(context, ListFullViewItemActivity.class);
        intent.putExtra(EXTRA_ID, FakeDB.getDb().size() - 1);
        context.startActivity(intent);
    }

    void deleteAll() {
        FakeDB.getDb().clear();
        NotesTable.deleteAll(database);
        notifyDataSetChanged();
    }

    void updateList() {
        FakeDB.getDb().clear();
        FakeDB.getDb().addAll(NotesTable.getAllNotes(database));
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.note_detail_view, viewGroup, false);
        }
//        TextView titleTextView = view.findViewById(R.id.short_item_title);
//        titleTextView.setText(FakeDB.getDb().get(i).getTitle());
//        titleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ListFullViewItemActivity.class);
//                intent.putExtra(EXTRA_ID, i);
//                context.startActivity(intent);
//            }
//        });
        final String noteTitle = FakeDB.getDb().get(i).getTitle();

        TextView firstLetterTextView = view.findViewById(R.id.card_view_first_letter_text);
        firstLetterTextView.setText(noteTitle.substring(0, 1).toUpperCase());

        TextView titleTextView = view.findViewById(R.id.card_view_title_text);
        titleTextView.setText(noteTitle);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListFullViewItemActivity.class);
                intent.putExtra(EXTRA_ID, i);
                context.startActivity(intent);
            }
        });

        ImageView deleteNoteImageView = view.findViewById(R.id.card_view_close_note_image);
        deleteNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesTable.deleteNote(noteTitle, database);
                FakeDB.getDb().remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
