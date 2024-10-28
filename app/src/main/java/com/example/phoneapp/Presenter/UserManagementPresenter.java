package com.example.phoneapp.Presenter;

import com.example.phoneapp.Model.User;

public interface UserManagementPresenter {
    void addUser(String uid, User user) ;
    void updateUser(String userId, User user);
    void deleteUser(String userId);
    void loadUser();
}
