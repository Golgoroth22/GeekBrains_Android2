package com.falin.valentin.a2_l1;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WeatherService extends IntentService {
    public WeatherService() {
        super(WeatherService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
