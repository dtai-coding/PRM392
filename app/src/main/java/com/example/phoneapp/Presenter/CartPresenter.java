package com.example.phoneapp.Presenter;

import com.example.phoneapp.Model.CartDetail;

public interface CartPresenter {
    void loadCartItems(String userId);
    void addItemToCart(String userId, CartDetail item);
    void checkCartItemExistence(String userId, String itemId);
    void updateCartItem(String userId, String itemId, int newQuantity);
    void deleteCartItem(String userId, String itemId);
}