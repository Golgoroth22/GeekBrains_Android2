package com.falin.valentin.a2_l1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.falin.valentin.a2_l1.data.FakeDB;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    static String EXTRA_ID = "id";
    private Context context;
    private LayoutInflater layoutInflater;

    MyListViewAdapter(Context context) {
        this.context = context;
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
        FakeDB.getDb().add(new Note("", ""));

        notifyDataSetChanged();
    }

    void deleteAll() {
        FakeDB.getDb().clear();
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
