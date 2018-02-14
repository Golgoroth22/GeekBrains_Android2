package com.falin.valentin.a2_l1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.falin.valentin.a2_l1.data.FakeDB;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public MyListViewAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new FakeDB().getDb();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_item, viewGroup, false);
        }
        TextView titleTextView = view.findViewById(R.id.short_item_title);
        titleTextView.setText(list.get(i).getTitle());

        return view;
    }
}
