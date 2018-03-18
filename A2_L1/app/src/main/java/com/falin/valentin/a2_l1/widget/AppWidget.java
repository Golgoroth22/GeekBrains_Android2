package com.falin.valentin.a2_l1.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.falin.valentin.a2_l1.R;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String ITEM_ON_CLICK_ACTION = "android.appwidget.action.ITEM_ON_CLICK";
    public static final String NOTE_TEXT = "note_text";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        setList(views, context, appWidgetId);
        setListClick(views, context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equalsIgnoreCase(UPDATE_WIDGET_ACTION)) {
            int appWidgetIds[] = widgetManager.getAppWidgetIds(
                    new ComponentName(context, AppWidget.class));
            widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_view);
        }
        super.onReceive(context, intent);
    }

    void setList(RemoteViews remoteViews, Context context, int appWidgetId) {
        Intent adapterIntent = new Intent(context, NotesWidgetService.class);
        adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.appwidget_list_view, adapterIntent);
        remoteViews.setEmptyView(R.id.appwidget_list_view, R.id.appwidget_text);
    }

    void setListClick(RemoteViews remoteViews, Context context) {
        Intent clickIntent = new Intent(context, AppWidget.class);
        clickIntent.setAction(ITEM_ON_CLICK_ACTION);
        PendingIntent listClickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickIntent,
                0);
        remoteViews.setPendingIntentTemplate(R.id.appwidget_list_view, listClickPendingIntent);
    }
}

