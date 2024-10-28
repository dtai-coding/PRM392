package com.example.phoneapp.View;

import com.example.phoneapp.Model.CartDetail;

import java.util.List;

public interface CartView {
    void displayCartItems(List<CartDetail> cartItems);
    void showAddToCartMessage(String message);
    void showError(String message);
    void onCartItemExist(CartDetail existingItem);
    void displayTotalPrice(double totalPrice);
    boolean isAdded();
}
