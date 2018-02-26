package com.falin.valentin.a2_l1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falin.valentin.a2_l1.data.FakeDB;
import com.falin.valentin.a2_l1.data.WeatherDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();
    public static String internalFileName = "internal_file.notes";
    private String filePath;

    private MyListViewAdapter adapter;
    private TextView contentListViewText;
    private TextView contentCityTextView;
    private TextView contentDegreesTextView;

    private static String city = "";
    private static String cityDegrees = "";

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        filePath = getFilesDir() + "/" + internalFileName;

        loadFromFile();

        getLocation();

        initUIComponents();

        initListView();

        initViews();

        checkNoteBookSize();
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 3, 0, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWeatherData(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void initUIComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addElement();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.full_view_item_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViews() {
        contentListViewText = findViewById(R.id.content_list_view_text);
        contentCityTextView = findViewById(R.id.content_city_text_view);
        contentCityTextView.setText(city);
        contentDegreesTextView = findViewById(R.id.content_degrees_text_view);
        contentDegreesTextView.setText(cityDegrees);
    }

    private void initListView() {
        ListView listView = findViewById(R.id.content_list_view);
        adapter = new MyListViewAdapter(this);
        listView.setAdapter(adapter);
    }

    private void updateWeatherData(final Location location) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(location);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "City not found", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject mainObject = jsonObject.getJSONObject("main");
            cityDegrees = String.format("%.2f", mainObject.getDouble("temp")) + " ℃";
            contentDegreesTextView.setText(cityDegrees);
            contentCityTextView.setText(jsonObject.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void loadFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            List<Note> list = (List<Note>) objectInputStream.readObject();
            FakeDB.getDb().clear();
            FakeDB.getDb().addAll(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.full_view_item_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_delete_all_data: {
                adapter.deleteAll();
                ListFullViewItemActivity.saveToFile(filePath);
                return true;
            }
//            case R.id.action_add: {
//                adapter.addElement();
//                return true;
//            }
            default: {
                return false;
            }
        }
    }

    private void checkNoteBookSize() {
        if (FakeDB.getDb().size() == 0) {
            contentListViewText.setVisibility(View.VISIBLE);
        } else {
            contentListViewText.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clear_notebook) {
            adapter.deleteAll();
            checkNoteBookSize();
        } else if (id == R.id.nav_add_note) {
            adapter.addElement();
        } else if (id == R.id.contacts) {
            Intent intent = new Intent(this, DevActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.full_view_item_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
