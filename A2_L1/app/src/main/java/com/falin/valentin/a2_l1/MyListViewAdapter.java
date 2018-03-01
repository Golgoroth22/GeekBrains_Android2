package com.falin.valentin.a2_l1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        Note note = new Note();
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
            view = layoutInflater.inflate(R.layout.list_view_item, viewGroup, false);
        }
        TextView titleTextView = view.findViewById(R.id.short_item_title);
        titleTextView.setText(FakeDB.getDb().get(i).getTitle());
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListFullViewItemActivity.class);
                intent.putExtra(EXTRA_ID, i);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
