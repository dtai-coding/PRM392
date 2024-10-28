package com.example.phoneapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.Model.Item;
import com.example.phoneapp.R;
import com.bumptech.glide.Glide;
import com.example.phoneapp.databinding.RowItemBinding;

import java.util.List;
public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    private List<Item> itemList;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public UserItemAdapter(List<Item> itemList, OnItemClickListener clickListener) {
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    public void setItems(List<Item> newItems) {
        itemList.clear();
        itemList.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view binding
        RowItemBinding binding = RowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class UserItemViewHolder extends RecyclerView.ViewHolder {
        private final RowItemBinding binding;

        UserItemViewHolder(RowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Item item) {
            binding.tvItemName.setText(item.getName());
            binding.tvItemDescription.setText(item.getDescription());
            binding.tvItemPrice.setText(String.format("Price: $%.2f", item.getPrice()));

            if (item.getImageUrl() != null) {
                Glide.with(binding.getRoot().getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder_image)
                        .into(binding.ivItemImage);
            }
        }
    }
}
