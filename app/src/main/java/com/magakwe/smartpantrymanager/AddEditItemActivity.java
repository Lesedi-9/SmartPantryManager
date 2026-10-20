package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import data.AppDatabase;
import data.PantryItem;

public class AddEditItemActivity extends AppCompatActivity {

    private TextInputEditText etName, etQuantity, etUnit, etExpiry;
    private TextView tvTitle;
    private Button btnSave, btnDelete;
    private AppDatabase database;
    private int itemId = -1; // -1 means we are adding a new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize views
        tvTitle = findViewById(R.id.tvTitle);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        database = AppDatabase.getDatabase(this);

        // Check if we are editing an existing item
        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getIntExtra("item_id", -1);
            tvTitle.setText("Edit Ingredient");
            btnDelete.setVisibility(android.view.View.VISIBLE);
            loadItemData();
        } else {
            tvTitle.setText("Add New Ingredient");
        }

        btnSave.setOnClickListener(v -> saveItem());
        btnDelete.setOnClickListener(v -> deleteItem());
    }

    private void loadItemData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            PantryItem item = database.pantryItemDao().getItemById(itemId);
            if (item != null) {
                runOnUiThread(() -> {
                    etName.setText(item.name);
                    // Remove .0 from whole numbers for display
                    String qStr = (item.quantity % 1 == 0) ? String.valueOf((int) item.quantity) : String.valueOf(item.quantity);
                    etQuantity.setText(qStr);
                    etUnit.setText(item.unit);
                    etExpiry.setText(item.expiryDate);
                });
            }
        });
    }

    private void saveItem() {
        String name = etName.getText().toString().trim().toLowerCase();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim().toLowerCase();
        String expiry = etExpiry.getText().toString().trim();

        // --- INPUT VALIDATION ---
        if (name.isEmpty()) { etName.setError("Name is required!"); return; }
        if (quantityStr.isEmpty()) { etQuantity.setError("Quantity is required!"); return; }
        if (unit.isEmpty()) { etUnit.setError("Unit is required!"); return; }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid number!"); return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (itemId == -1) {
                // CREATE NEW
                database.pantryItemDao().insert(new PantryItem(name, quantity, unit, expiry));
            } else {
                // UPDATE EXISTING
                PantryItem itemToUpdate = new PantryItem(name, quantity, unit, expiry);
                itemToUpdate.id = itemId;
                database.pantryItemDao().update(itemToUpdate);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void deleteItem() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            PantryItem itemToDelete = new PantryItem("", 0, "", "");
            itemToDelete.id = itemId;
            database.pantryItemDao().delete(itemToDelete);

            runOnUiThread(() -> {
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}