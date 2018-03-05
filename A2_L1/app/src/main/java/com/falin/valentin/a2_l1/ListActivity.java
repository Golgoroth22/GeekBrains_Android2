package com.falin.valentin.a2_l1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falin.valentin.a2_l1.data.DatabaseSQLiteHelper;
import com.falin.valentin.a2_l1.data.FakeDB;
import com.falin.valentin.a2_l1.data.NotesTable;
import com.falin.valentin.a2_l1.data.WeatherDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();
    public static String EXTRA_ID = "id";

    //private MyListViewAdapter adapter;
    private NoteDetailAdapter adapter;

    public static SQLiteDatabase database;

    private TextView contentListViewText;
    private TextView contentCityTextView;
    private TextView contentDegreesTextView;
    private RecyclerView recyclerView;

    private static String city = "";
    private static String cityDegrees = "";

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getLocation();

        initUIComponents();

        initDB();

        //initListView();
        adapter = new NoteDetailAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.content_list_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        initViews();

        checkNoteBookSize();
    }

    private void initDB() {
        database = new DatabaseSQLiteHelper(getApplicationContext()).getWritableDatabase();
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
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

//    private void initListView() {
//        ListView listView = findViewById(R.id.content_list_view);
//        adapter = new MyListViewAdapter(this, database);
//        listView.setAdapter(adapter);
//    }

    private void updateWeatherData(final Location location) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(location);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (contentCityTextView.getText().equals("")) {
                                contentCityTextView.setText("Your location not defined");
                            }
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

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.updateList();
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
                return true;
            }
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

    private class NoteDetailAdapter extends RecyclerView.Adapter<NoteDetailViewHolder> {
        Context context;

        public NoteDetailAdapter(Context context) {
            this.context = context;
            FakeDB.getDb().clear();
            FakeDB.getDb().addAll(NotesTable.getAllNotes(database));
        }

        @Override
        public NoteDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.note_detail_view, parent, false);
            return new NoteDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteDetailViewHolder holder, int position) {
            final int id = position;
            holder.firstLetterTextView.setText(FakeDB.getDb().get(id).getTitle().substring(0, 1).toUpperCase());
            holder.titleTextView.setText(FakeDB.getDb().get(id).getTitle());
            holder.titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ListFullViewItemActivity.class);
                    intent.putExtra(EXTRA_ID, id);
                    context.startActivity(intent);
                }
            });
            holder.deleteNoteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotesTable.deleteNote(FakeDB.getDb().get(id).getTitle(), database);
                    FakeDB.getDb().remove(id);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (FakeDB.getDb().size() != 0) {
                return FakeDB.getDb().size();
            }
            return 0;
        }

        void addElement() {
            Note note = new Note(" ", " ");
            FakeDB.getDb().add(note);
            NotesTable.addNote(note, database);
            Intent intent = new Intent(context, ListFullViewItemActivity.class);
            intent.putExtra(EXTRA_ID, FakeDB.getDb().size() - 1);
            context.startActivity(intent);
        }

        void deleteAll() {
            FakeDB.getDb().clear();
            NotesTable.deleteAll(database);
            notifyDataSetChanged();
        }

        void updateList() {
            FakeDB.getDb().clear();
            FakeDB.getDb().addAll(NotesTable.getAllNotes(database));
            notifyDataSetChanged();
        }
    }

    private class NoteDetailViewHolder extends RecyclerView.ViewHolder {
        TextView firstLetterTextView;
        TextView titleTextView;
        ImageView deleteNoteImageView;

        NoteDetailViewHolder(View itemView) {
            super(itemView);
            firstLetterTextView = itemView.findViewById(R.id.card_view_first_letter_text);
            titleTextView = itemView.findViewById(R.id.card_view_title_text);
            deleteNoteImageView = itemView.findViewById(R.id.card_view_close_note_image);
        }
    }
}
