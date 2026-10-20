package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import data.AppDatabase;
import data.PantryItem;

public class AddEditItemActivity extends AppCompatActivity {

    private TextInputEditText etName, etQuantity, etUnit, etExpiry;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize views
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        Button btnSave = findViewById(R.id.btnSave);

        // Initialize Database
        database = AppDatabase.getDatabase(this);

        btnSave.setOnClickListener(v -> {
            // --- INPUT VALIDATION ---
            String name = etName.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Ingredient name is required!");
                return;
            }

            if (quantityStr.isEmpty()) {
                etQuantity.setError("Quantity is required!");
                return;
            }

            if (unit.isEmpty()) {
                etUnit.setError("Unit is required!");
                return;
            }

            // Convert quantity to double
            double quantity;
            try {
                quantity = Double.parseDouble(quantityStr);
            } catch (NumberFormatException e) {
                etQuantity.setError("Invalid quantity format!");
                return;
            }

            // Ensure everything is lowercase for the strict matching later!
            name = name.toLowerCase();
            unit = unit.toLowerCase();

            // --- SAVE TO DATABASE ---
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);

            AppDatabase.databaseWriteExecutor.execute(() -> {
                database.pantryItemDao().insert(newItem);

                // Go back to the main screen on the UI thread
                runOnUiThread(() -> {
                    Toast.makeText(AddEditItemActivity.this, "Ingredient added!", Toast.LENGTH_SHORT).show();
                    finish(); // This closes this screen and takes you back to MainActivity
                });
            });
        });
    }
}