package com.example.phoneapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.phoneapp.Activity.HomeActivity;
import com.example.phoneapp.Adapter.CartItemAdapter;
import com.example.phoneapp.Model.CartDetail;
import com.example.phoneapp.Presenter.CartPresenter;
import com.example.phoneapp.Presenter.CartPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.CartView;
import com.example.phoneapp.databinding.FragmentCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.util.ArrayList;
import java.util.List;

// CartFragment.java
// CartFragment.java
public class CartFragment extends Fragment implements CartView {
    private FragmentCartBinding binding;
    private CartPresenter cartPresenter;
    private CartItemAdapter cartItemAdapter;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        HomeActivity homeActivity = (HomeActivity) getActivity();
        userId = homeActivity != null ? homeActivity.getUserId() : null;

        cartPresenter = new CartPresenterImpl(this);
        cartItemAdapter = new CartItemAdapter(userId, cartPresenter);

        binding.tvTotalItems.setText(String.format("Total Items: ", cartItemAdapter.getItemCount()));
        // Setup RecyclerView
        binding.rvCartDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCartDetails.setAdapter(cartItemAdapter);

        // Set up button click for checkout
        binding.btnCheckout.setOnClickListener(v -> {
            QRCodeFragment qrCodeFragment = new QRCodeFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        });

        cartPresenter.loadCartItems(userId);
        fetchAndDisplayTotalPrice();

        return binding.getRoot();
    }

    @Override
    public void displayCartItems(List<CartDetail> cartItems) {
        if (cartItemAdapter != null) {
            cartItemAdapter.setItems(cartItems);
            cartItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showAddToCartMessage(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void showError(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onCartItemExist(CartDetail existingItem) {

    }

    @Override
    public void displayTotalPrice(double totalPrice) {
        if (isAdded()) {
            binding.tvTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));
        }
    }

    private void fetchAndDisplayTotalPrice() {
        DatabaseReference cartDetailsRef = FirebaseDatabase.getInstance().getReference("carts")
                .child(userId).child("cartDetails");

        cartDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    DatabaseReference totalPriceRef = FirebaseDatabase.getInstance().getReference("carts")
                            .child(userId).child("totalPrice");

                    totalPriceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot priceSnapshot) {
                            if (priceSnapshot.exists()) {
                                double totalPrice = priceSnapshot.getValue(Double.class);
                                binding.tvTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
                            } else {
                                binding.tvTotalPrice.setText("Total: $0");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("CartFragment", "Failed to fetch total price", error.toException());
                        }
                    });
                } else {
                    binding.tvTotalPrice.setText("Total: $0");
                    cartItemAdapter.setItems(new ArrayList<>());
                    cartItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartFragment", "Failed to check cart details", error.toException());
            }
        });
    }

    // Additional methods for CartView
}
