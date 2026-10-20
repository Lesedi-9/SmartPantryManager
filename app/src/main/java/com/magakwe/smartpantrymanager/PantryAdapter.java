package com.magakwe.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import data.PantryItem;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList = new ArrayList<>();

    // We will use this later for editing/deleting items when clicked
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setPantryList(List<PantryItem> pantryList) {
        this.pantryList = pantryList;
        notifyDataSetChanged(); // Refreshes the list
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem currentItem = pantryList.get(position);

        holder.tvItemName.setText(currentItem.name);

        // Format: "3.0 pieces" -> "3 pieces" (removes the .0 if it's a whole number)
        String quantityStr = (currentItem.quantity % 1 == 0) ?
                String.valueOf((int) currentItem.quantity) : String.valueOf(currentItem.quantity);

        holder.tvItemQuantity.setText(quantityStr + " " + currentItem.unit);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemQuantity;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
        }
    }
}