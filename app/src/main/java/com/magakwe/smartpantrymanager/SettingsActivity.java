package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private MaterialSwitch switchExpiryAlerts;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup Toolbar and Back Button
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);

        // Load saved state (default is true)
        boolean isAlertsEnabled = sharedPreferences.getBoolean("expiry_alerts", true);
        switchExpiryAlerts.setChecked(isAlertsEnabled);

        // Save state when the switch is toggled
        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("expiry_alerts", isChecked);
            editor.apply();
        });
    }

    // Handle the back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}