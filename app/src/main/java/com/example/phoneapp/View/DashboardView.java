package com.example.phoneapp.View;

public interface DashboardView {
    // Define methods for updating the Dashboard UI
    void showTotalUsers(int count);
    void showTotalItems(int count);
    void showTotalCarts(int count);
    void showTotalProfit(double profit);
    void showError(String message);
}