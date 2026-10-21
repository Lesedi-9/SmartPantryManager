package com.magakwe.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import data.AppDatabase;
import data.PantryItem;
import data.Recipe;
import data.RecipeIngredient;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private RecipeAdapter adapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbarSuggestions);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewSuggestions);
        tvEmpty = findViewById(R.id.tvSuggestionsEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnRecipeClickListener(recipe -> {
            android.content.Intent intent = new android.content.Intent(this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.id);
            startActivity(intent);
        });

        database = AppDatabase.getDatabase(this);
        findMatches();
    }

    private void findMatches() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<PantryItem> pantry = database.pantryItemDao().getAllItems();
            List<Recipe> allRecipes = database.recipeDao().getAllRecipes();
            List<Recipe> matchingRecipes = new ArrayList<>();

            for (Recipe recipe : allRecipes) {
                List<RecipeIngredient> requiredIngredients = database.recipeDao().getIngredientsForRecipe(recipe.id);

                if (RecipeMatcher.isStrictMatch(requiredIngredients, pantry)) {
                    matchingRecipes.add(recipe);
                }
            }

            runOnUiThread(() -> {
                if (matchingRecipes.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setRecipeList(matchingRecipes);
                }
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Closes this screen and goes back to the Pantry
        return true;
    }
}