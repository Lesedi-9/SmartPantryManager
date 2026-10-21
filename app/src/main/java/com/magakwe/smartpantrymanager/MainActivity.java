package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import data.AppDatabase;
import data.PantryItem;
import data.PantryItemDao;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private TextView tvEmptyPantry;
    private FloatingActionButton fabAddItem;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_suggestions) {
                startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.action_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });

        // 1. Initialize views
        recyclerView = findViewById(R.id.recyclerViewPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        fabAddItem = findViewById(R.id.fabAddItem);

        // 2. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter();
        recyclerView.setAdapter(adapter);

        // 3. Listen for clicks on pantry items
        adapter.setOnItemClickListener(item -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddEditItemActivity.class);
            intent.putExtra("item_id", item.id); // Pass the ID to the next screen
            startActivity(intent);
        });

        // 4. Initialize Database
        database = AppDatabase.getDatabase(this);

        // 5. Button click placeholder (We will build the Add Screen next!)
        fabAddItem.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load data every time the screen opens so the list is up to date
        loadPantryData();
    }

    private void loadPantryData() {
        // Run database query in the background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<PantryItem> items = database.pantryItemDao().getAllItems();

            // Switch back to the main UI thread to update the RecyclerView
            runOnUiThread(() -> {
                adapter.setPantryList(items);

                // Handle empty state
                if (items.isEmpty()) {
                    tvEmptyPantry.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyPantry.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_suggestions) {
            startActivity(new android.content.Intent(this, SuggestedRecipesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}