package com.falin.valentin.a2_l1.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataLoader {
    //private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?lat=%.4f&lon=%.4f&units=metric";
    private static final String KEY = "x-api-key";
    private static final String API_KEY = "17caaf60d16c4528b56b46666e0ab108";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;

    public static JSONObject getJSONData(Location location) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, location.getLatitude(), location.getLongitude()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, API_KEY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVar;
            while ((tempVar = reader.readLine()) != null) {
                rawData.append(tempVar).append(NEW_LINE);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }
}
