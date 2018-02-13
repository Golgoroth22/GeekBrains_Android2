package com.falin.valentin.a2_l1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private List<Note> list = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public MyListViewAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list.add(new Note("Note 1", "Curabitur metus nulla, viverra in ipsum sit amet, placerat vulputate nulla. Pellentesque vitae libero in ipsum egestas egestas. Duis diam velit, bibendum ac libero id, pellentesque interdum est."));
        list.add(new Note("Note 2", "Duis gravida venenatis enim, in luctus dolor tristique id. Donec dignissim erat a dapibus efficitur. Interdum et malesuada fames ac ante ipsum primis in faucibus."));
        list.add(new Note("Note 3", "Donec eget tincidunt quam. Etiam in sem commodo, cursus lectus vel, vulputate dui."));
        list.add(new Note("Note 4", "Curabitur consectetur nulla in ipsum sollicitudin, nec venenatis purus scelerisque. Donec maximus, sapien in dictum tincidunt, lacus mi varius erat, ac viverra erat turpis in purus. Cras eget imperdiet turpis. Proin faucibus tortor in est sollicitudin, sit amet molestie eros efficitur. Quisque a posuere neque."));
        list.add(new Note("Note 5", "Cras ac dolor vitae neque ultrices iaculis ac vitae purus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut a sapien vel nunc hendrerit consequat eu ut orci. Aenean tortor odio, tincidunt in tincidunt non, mattis nec quam. In sed rutrum tellus, eleifend tincidunt tellus."));
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
        TextView titleTextView = view.findViewById(R.id.item_title);
        titleTextView.setText(list.get(i).getTitle());

        TextView textView = view.findViewById(R.id.item_text);
        textView.setText(list.get(i).getText());

        return view;
    }
}
