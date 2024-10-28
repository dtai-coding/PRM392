package com.example.phoneapp.Model;

import java.util.List;

public class Cart {
    public String cartId;
    public String userId;
    public double totalPrice;
    public List<String> cartDetailIds;

    public Cart() {
    }

    public Cart(String cartId, String userId, double totalPrice, List<String> cartDetailIds) {
        this.cartId = cartId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.cartDetailIds = cartDetailIds;
    }

    public String getCartId() {
        return cartId;
    }
    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getCartDetailIds() {
        return cartDetailIds;
    }

    public void setCartDetailIds(List<String> cartDetailIds) {
        this.cartDetailIds = cartDetailIds;
    }
}
