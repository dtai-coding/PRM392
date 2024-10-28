package com.example.phoneapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.Model.Item;
import com.example.phoneapp.R;
import com.bumptech.glide.Glide;
import com.example.phoneapp.databinding.RowItemManageBinding;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Item item);
        void onDeleteClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view binding
        RowItemManageBinding binding = RowItemManageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item); // Call bind method to set values
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final RowItemManageBinding binding;

        ItemViewHolder(RowItemManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Item item) {
            binding.tvItemName.setText(item.getName());
            binding.tvItemDescription.setText(item.getDescription());
            binding.tvItemPrice.setText(String.format("Price: $%.2f", item.getPrice()));
            binding.tvItemType.setText(String.format("Type: %s", item.getType())); // Bind type

            // Load item image with Glide
            if (item.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder_image) // Placeholder image
                        .into(binding.ivItemImage);
            } else {
                binding.ivItemImage.setImageResource(R.drawable.ic_placeholder_image); // Default image if URL is null
            }

            // Set up button listeners
            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(item);
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(item);
            });
        }
    }
}

