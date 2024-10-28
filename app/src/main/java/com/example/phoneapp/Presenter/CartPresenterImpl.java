package com.example.phoneapp.Presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.phoneapp.Model.Cart;
import com.example.phoneapp.Model.CartDetail;
import com.example.phoneapp.View.CartView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartPresenterImpl implements CartPresenter {

    private final CartView cartView;
    private final DatabaseReference cartRef;

    public CartPresenterImpl(CartView cartView) {
        this.cartView = cartView;
        this.cartRef = FirebaseDatabase.getInstance().getReference("carts");
    }


    @Override
    public void loadCartItems(String userId) {
        // Fetch cart items
        cartRef.child(userId).child("cartDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartDetail> cartItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartDetail item = itemSnapshot.getValue(CartDetail.class);
                    if (item != null) {
                        cartItems.add(item);
                    }
                }
                if (!cartItems.isEmpty()) {
                    cartView.displayCartItems(cartItems);
                } else {
                    cartView.showError("No items found in the cart.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError(error.getMessage());
            }
        });

        // Fetch total price
        cartRef.child(userId).child("totalPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double totalPrice = snapshot.getValue(Double.class);
                if (totalPrice != null) {
                    cartView.displayTotalPrice(totalPrice); // Display total price in view
                } else {
                    cartView.showError("Total price not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError("Failed to retrieve total price: " + error.getMessage());
            }
        });
    }



    @Override
    public void checkCartItemExistence(String userId, String itemId) {
        DatabaseReference itemRef = cartRef.child(userId).child("cartDetails").child(itemId);

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CartDetail existingItem = snapshot.getValue(CartDetail.class);
                    cartView.onCartItemExist(existingItem); // Item exists, return it
                } else {
                    cartView.onCartItemExist(null); // Item doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError("Failed to check cart: " + error.getMessage());
            }
        });
    }

    @Override
    public void updateCartItem(String userId, String itemId, int newQuantity) {
        DatabaseReference itemRef = cartRef.child(userId).child("cartDetails").child(itemId).child("quantity");

        itemRef.setValue(newQuantity)
                .addOnSuccessListener(aVoid -> {
                    cartView.showAddToCartMessage("Quantity updated in cart");
                    updateTotalPrice(userId); // Update total price after quantity change
                })
                .addOnFailureListener(e -> cartView.showError(e.getMessage()));
    }


    @Override
    public void addItemToCart(String userId, CartDetail item) {
        if (userId == null || item.getItemId() == null) {
            Log.e("CartError", "Cannot add item to cart: userId or itemId is null.");
            return;
        }

        // Check if cart exists for the user; if not, create it
        cartRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Create a new cart with default values
                    Cart newCart = new Cart();
                    newCart.userId = userId;
                    newCart.totalPrice = 0; // Start with 0 until items are added
                    newCart.cartDetailIds = new ArrayList<>();

                    cartRef.child(userId).setValue(newCart);
                }

                // Now add the item to cart details
                DatabaseReference itemRef = cartRef.child(userId).child("cartDetails").child(item.getItemId());
                itemRef.setValue(item)
                        .addOnSuccessListener(aVoid -> {
                            cartView.showAddToCartMessage("Item added to cart");
                            updateTotalPrice(userId); // Update total price after adding item
                        })
                        .addOnFailureListener(e -> cartView.showError(e.getMessage()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError("Failed to check cart existence: " + error.getMessage());
            }
        });
    }

    private void updateTotalPrice(String userId) {
        cartRef.child(userId).child("cartDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total = 0.0;
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartDetail item = itemSnapshot.getValue(CartDetail.class);
                    if (item != null) {
                        total += item.getItemPrice() * item.getQuantity();
                    }
                }
                cartRef.child(userId).child("totalPrice").setValue(total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError("Failed to calculate total price: " + error.getMessage());
            }
        });
    }

    @Override
    public void deleteCartItem(String userId, String itemId) {
        DatabaseReference cartDetailsRef = cartRef.child(userId).child("cartDetails");
        DatabaseReference totalPriceRef = cartRef.child(userId).child("totalPrice");

        // First, check if this is the only item in the cart
        cartDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 1) {
                    // If there's only one item, clear the cart and set total price to 0
                    cartDetailsRef.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Update total price to 0
                                totalPriceRef.setValue(0).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (cartView.isAdded()) { // Ensure fragment is added
                                            cartView.showAddToCartMessage("Cart is now empty");
                                        }
                                    } else {
                                        cartView.showError("Failed to reset total price");
                                    }
                                });
                            })
                            .addOnFailureListener(e -> cartView.showError(e.getMessage()));
                } else {
                    // Otherwise, proceed with deleting the specified item
                    DatabaseReference itemRef = cartDetailsRef.child(itemId);
                    itemRef.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                if (cartView.isAdded()) { // Ensure fragment is added
                                    cartView.showAddToCartMessage("Item removed from cart");
                                }
                                updateTotalPrice(userId); // Recalculate total price
                            })
                            .addOnFailureListener(e -> cartView.showError(e.getMessage()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartView.showError("Failed to access cart items: " + error.getMessage());
            }
        });
    }

}
