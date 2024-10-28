package com.example.phoneapp.Presenter;


import androidx.annotation.NonNull;

import com.example.phoneapp.View.DashboardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardPresenterImpl implements DashboardPresenter {
    private final DashboardView dashboardView;
    private final DatabaseReference usersRef;
    private final DatabaseReference itemsRef;
    private final DatabaseReference cartsRef;

    public DashboardPresenterImpl(DashboardView view) {
        this.dashboardView = view;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        itemsRef = database.getReference("Items");
        cartsRef = database.getReference("carts");
    }

    @Override
    public void loadDashboardData() {
        // Fetch total users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dashboardView.showTotalUsers((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dashboardView.showError("Failed to load user count: " + error.getMessage());
            }
        });

        // Fetch total items
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dashboardView.showTotalItems((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dashboardView.showError("Failed to load item count: " + error.getMessage());
            }
        });

        // Fetch total carts and calculate total price
        cartsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cartCount = 0;
                double totalPrice = 0.0;
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    cartCount++;
                    Double cartTotal = cartSnapshot.child("totalPrice").getValue(Double.class);
                    totalPrice += cartTotal != null ? cartTotal : 0.0;
                }
                dashboardView.showTotalCarts(cartCount);
                dashboardView.showTotalProfit(totalPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dashboardView.showError("Failed to load cart data: " + error.getMessage());
            }
        });
    }
}
