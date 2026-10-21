package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;
import data.AppDatabase;
import data.Recipe;
import data.RecipeIngredient;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbarRecipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        AppDatabase db = AppDatabase.getDatabase(this);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            List<RecipeIngredient> ingredients = db.recipeDao().getIngredientsForRecipe(recipeId);

            if (recipe != null) {
                StringBuilder ingString = new StringBuilder();
                for (RecipeIngredient ing : ingredients) {
                    ingString.append("• ").append(ing.quantity).append(" ").append(ing.unit)
                            .append(" ").append(ing.name).append("\n");
                }

                runOnUiThread(() -> {
                    tvTitle.setText(recipe.name);
                    tvIngredients.setText(ingString.toString());
                    tvSteps.setText(recipe.steps);
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}