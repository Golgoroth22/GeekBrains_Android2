package com.falin.valentin.a2_l1;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.falin.valentin.a2_l1.data.WeatherDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherService extends IntentService {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();
    private static String LOCATION_NOT_FOUND = "Your location not defined";
    public static String currentWeather = "";
    public static String currentCity = "";

    public WeatherService() {
        super(WeatherService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    JSONObject jsonObject = WeatherDataLoader.getJSONData(ListActivity.appLocation);
                    if (jsonObject == null) {
                        currentCity = LOCATION_NOT_FOUND;
                    } else {
                        renderWeather(jsonObject);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject mainObject = jsonObject.getJSONObject("main");
            currentWeather = String.format("%.2f", mainObject.getDouble("temp")) + " ℃";
            currentCity = jsonObject.get("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }
}


