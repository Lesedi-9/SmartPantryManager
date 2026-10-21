package com.magakwe.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
    private OnRecipeClickListener listener;

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Let's make the text a bit nicer for the list
        TextView tv = new TextView(parent.getContext());
        tv.setPadding(32, 32, 32, 32);
        tv.setTextSize(20f);
        tv.setTextColor(parent.getContext().getResources().getColor(android.R.color.black));
        return new RecipeViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        ((TextView) holder.itemView).setText("🍽️ " + recipe.name);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
