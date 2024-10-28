package com.example.phoneapp.Presenter;

public interface UserProfilePresenter {
    void fetchUserInfo(String userId);
    void updateUserInfo(String userId, String name, String phone);
}
