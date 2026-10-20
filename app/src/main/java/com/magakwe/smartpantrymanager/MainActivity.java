package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // 1. Initialize views
        recyclerView = findViewById(R.id.recyclerViewPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        fabAddItem = findViewById(R.id.fabAddItem);

        // 2. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter();
        recyclerView.setAdapter(adapter);

        // 3. Initialize Database
        database = AppDatabase.getDatabase(this);

        // 4. Button click placeholder (We will build the Add Screen next!)
        fabAddItem.setOnClickListener(v -> {
            Toast.makeText(this, "Add screen coming next!", Toast.LENGTH_SHORT).show();
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
}