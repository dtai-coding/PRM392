package com.example.phoneapp.View;

import com.example.phoneapp.Model.User;

public interface UserProfileView {
    void showUserInfo(User user);
    void showUpdateSuccess();
    void showUpdateError(String error);
    void showLoading();
    void hideLoading();
}
