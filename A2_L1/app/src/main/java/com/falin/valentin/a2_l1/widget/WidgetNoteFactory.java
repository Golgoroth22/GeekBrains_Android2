package com.falin.valentin.a2_l1.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.falin.valentin.a2_l1.R;
import com.falin.valentin.a2_l1.data.FakeDB;
import com.falin.valentin.a2_l1.data.Note;

import java.util.List;

public class WidgetNoteFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    private List<Note> noteList;

    public WidgetNoteFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        noteList = FakeDB.getDb();
    }

    @Override
    public void onDataSetChanged() {
        noteList.clear();
        noteList = FakeDB.getDb();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        remoteViews.setTextViewText(R.id.item_widget_text, noteList.get(position).getTitle());

        Intent clickIntent = new Intent();
        clickIntent.putExtra(AppWidget.NOTE_TEXT, noteList.get(position).getText());

        remoteViews.setOnClickFillInIntent(R.id.item_widget_text, clickIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
