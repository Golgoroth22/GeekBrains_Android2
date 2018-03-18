package com.falin.valentin.a2_l1.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Valentin on 18.03.2018.
 */

public class NotesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetNoteFactory(getApplicationContext());
    }
}
