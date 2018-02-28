package com.falin.valentin.a2_l1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.falin.valentin.a2_l1.data.FakeDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ListFullViewItemActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int note_id;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_full_view_item);

        filePath = getFilesDir() + "/" + ListActivity.internalFileName;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        note_id = intent.getIntExtra(MyListViewAdapter.EXTRA_ID, 0);

        EditText titleText = findViewById(R.id.item_title);
        titleText.setText(FakeDB.getDb().get(note_id).getTitle());

        EditText text = findViewById(R.id.item_text);
        text.setText(FakeDB.getDb().get(note_id).getText());
    }

    public static void saveToFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;

            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(file, false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(FakeDB.getDb());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListActivity.class);
        saveChangesInNote();
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_full_view_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_note: {
                FakeDB.getDb().remove(note_id);
                saveToFile(filePath);
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);

                return true;
            }
            case R.id.action_edit_note: {
                saveChangesInNote();
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);

                return true;
            }
            default: {
                return false;
            }
        }
    }

    private void saveChangesInNote() {
        EditText titleText = findViewById(R.id.item_title);
        String titleTextText = titleText.getText().toString();

        EditText text = findViewById(R.id.item_text);
        String textText = text.getText().toString();

        FakeDB.getDb().set(note_id, new Note(titleTextText, textText));
        saveToFile(filePath);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
}
