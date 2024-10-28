package com.example.phoneapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.phoneapp.Model.CartDetail;
import com.example.phoneapp.Presenter.CartPresenter;
import com.example.phoneapp.Presenter.CartPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.CartView;
import com.example.phoneapp.databinding.FragmentDetailedItemBinding;

import java.util.List;

public class DetailedItemFragment extends Fragment implements CartView {

    private FragmentDetailedItemBinding binding;
    private CartPresenter cartPresenter;
    private String userId;
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String imageUrl;
    private String itemDescription;


    public static DetailedItemFragment newInstance(String userId, String itemId, String name, String description, double price, String imageUrl) {
        DetailedItemFragment fragment = new DetailedItemFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("itemId", itemId);
        args.putString("itemName", name);
        args.putString("itemDescription", description);
        args.putDouble("itemPrice", price);
        args.putString("imageUrl", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailedItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        cartPresenter = new CartPresenterImpl(this); // Initialize CartPresenter

        // Load item data from arguments
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            itemId = getArguments().getString("itemId");
            itemName = getArguments().getString("itemName");
            itemPrice = getArguments().getDouble("itemPrice");
            itemDescription = getArguments().getString("itemDescription");
            imageUrl = getArguments().getString("imageUrl");

            binding.tvDetailedName.setText(itemName);
            binding.tvDetailedPrice.setText(String.format("Price: $%.2f", itemPrice));
            binding.tvDetailedDescription.setText(itemDescription);

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(binding.ivDetailedImage);

            binding.btnOrder.setOnClickListener(v -> {
                // Check if item already exists in the cart
                cartPresenter.checkCartItemExistence(userId, itemId);
            });
        }

        return view;
    }


    @Override
    public void onCartItemExist(CartDetail existingItem) {
        if (userId == null || itemId == null) {
            Log.e("CartError", "userId or itemId is null. userId: " + userId + ", itemId: " + itemId);
            Toast.makeText(getContext(), "Unable to add item to cart. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If item exists, increment quantity
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + 1;

            cartPresenter.updateCartItem(userId, itemId, newQuantity);
        } else {
            // If item doesn't exist, add a new entry
            cartPresenter.addItemToCart(userId, new CartDetail(itemId, itemName, itemPrice, 1,imageUrl));
        }
    }

    @Override
    public void showAddToCartMessage(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showError(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayCartItems(List<CartDetail> cartItems) {
        // No display logic here since this is DetailedItemFragment; handle in CartFragment
    }

    @Override
    public void displayTotalPrice(double totalPrice) {
        // No display logic here since this is DetailedItemFragment; handle in CartFragment
    }
}
