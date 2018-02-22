package com.falin.valentin.a2_l1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falin.valentin.a2_l1.data.FakeDB;
import com.falin.valentin.a2_l1.data.WeatherDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();
    private MyListViewAdapter adapter;
    private TextView contentListViewText;
    private EditText contentCityInputEditText;
    private ImageButton contentCityApplyButton;
    private TextView contentDegreesTextView;

    private static String city = "";
    private static String cityDegrees = "";

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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

        setListView();

        contentListViewText = findViewById(R.id.content_list_view_text);
        contentCityApplyButton = findViewById(R.id.content_check_city_button);
        contentCityApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = contentCityInputEditText.getText().toString();
                updateWeatherData();
            }
        });
        contentCityInputEditText = findViewById(R.id.content_city_edit_text);
        contentCityInputEditText.setText(city);
        contentDegreesTextView = findViewById(R.id.content_degrees_text_view);
        contentDegreesTextView.setText(cityDegrees);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkNoteBookSize();
    }

    private void setListView() {
        ListView listView = findViewById(R.id.content_list_view);
        adapter = new MyListViewAdapter(this);
        listView.setAdapter(adapter);
    }

    private void updateWeatherData() {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getApplicationContext(), city);
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
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "One or more fields not found in the JSON data");
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
            case R.id.action_delete_all: {
                adapter.deleteAll();
                checkNoteBookSize();
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
