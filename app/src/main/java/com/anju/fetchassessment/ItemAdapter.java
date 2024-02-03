package com.anju.fetchassessment;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private  List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        if (itemList != null) {
            this.itemList = itemList;
        } else {
            this.itemList = new ArrayList<>(); // Initialize an empty list to avoid NullPointerException
        }}
    public void setItems(List<Item> itemList) {
        this.itemList = itemList != null ? itemList : new ArrayList<>();
        notifyDataSetChanged(); // Notify the adapter of the data change
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (itemList == null || itemList.isEmpty() || position < 0 || position >= itemList.size()) {
            return;
        }
        Item item = itemList.get(position);
        holder.bind(item);

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
        }
        public void bind(Item item) {
            itemTextView.setText("ID: " + item.getId() + ", List ID: " + item.getListId() + ", Name: " + item.getName());
        }
    }
}


