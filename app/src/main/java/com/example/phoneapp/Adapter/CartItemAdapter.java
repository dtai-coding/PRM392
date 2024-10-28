package com.example.phoneapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phoneapp.Activity.HomeActivity;
import com.example.phoneapp.Model.CartDetail;
import com.example.phoneapp.Presenter.CartPresenter;
import com.example.phoneapp.Presenter.CartPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.databinding.RowCartBinding;

import java.util.ArrayList;
import java.util.List;

// CartItemAdapter.java
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private final CartPresenter cartPresenter;
    private List<CartDetail> cartItems = new ArrayList<>();
    private final String userId; // Hold userId

    public CartItemAdapter(String userId, CartPresenter cartPresenter) {
        this.userId = userId;
        this.cartPresenter = cartPresenter;
    }
    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Initialize view binding
        RowCartBinding binding = RowCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartDetail item = cartItems.get(position);
        holder.bind(item); // Bind item data to views
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setItems(List<CartDetail> items) {
        this.cartItems.clear();
        cartItems.addAll(items);
    }


    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private final RowCartBinding binding;

        CartItemViewHolder(RowCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartDetail item) {
            binding.tvItemName.setText(item.getItemName());
            binding.tvItemQuantity.setText(String.valueOf(item.getQuantity()));
            binding.tvItemPrice.setText(String.format("Price: $%.2f", item.getItemPrice()));

            // Load image
            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_image) // Existing placeholder
                    .error(R.drawable.ic_placeholder_image) // Optional error image
                    .into(binding.ivCartImage);

            Log.d("CartItemAdapter", "Image URL: " + item.getImageUrl());

            // Quantity increase button
            binding.btnIncreaseQuantity.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() + 1;
                cartPresenter.updateCartItem(userId, item.getItemId(), newQuantity);
            });

            // Quantity decrease button
            binding.btnDecreaseQuantity.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    int newQuantity = item.getQuantity() - 1;
                    cartPresenter.updateCartItem(userId, item.getItemId(), newQuantity);
                }
            });

            // Delete button
            binding.btnDeleteItem.setOnClickListener(v -> {
                cartPresenter.deleteCartItem(userId, item.getItemId());
            });
        }
    }
}
