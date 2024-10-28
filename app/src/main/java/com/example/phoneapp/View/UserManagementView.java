package com.example.phoneapp.View;

import android.util.Pair;

import com.example.phoneapp.Model.User;

import java.util.List;

public interface UserManagementView {
    void onUserAdded();
    void onUserUpdated();
    void onUserDeleted();
    void onDataLoaded(List<Pair<User, String>> users);
    void onError(String message);
}
