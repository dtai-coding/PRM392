package com.example.phoneapp.Presenter;

import android.util.Pair;

import com.example.phoneapp.Model.User;
import com.example.phoneapp.View.UserManagementView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserManagementPresenterImpl implements UserManagementPresenter {
    private final UserManagementView view;
    private final DatabaseReference usersRef;

    public UserManagementPresenterImpl(UserManagementView view) {
        this.view = view;
        this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public void addUser(String uid, User user) {
        if (uid != null) {
            usersRef.child(uid).setValue(user)
                    .addOnSuccessListener(aVoid -> view.onUserAdded())
                    .addOnFailureListener(e -> view.onError("Failed to add user: " + e.getMessage()));
        }
    }


    @Override
    public void updateUser(String userKey, User user) {
        usersRef.child(userKey).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onUserUpdated();
            } else {
                view.onError("Failed to update user.");
            }
        });
    }

    @Override
    public void deleteUser(String userKey) {
        usersRef.child(userKey).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onUserDeleted();
            } else {
                view.onError("Failed to delete user.");
            }
        });
    }

    @Override
    public void loadUser() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Pair<User, String>> usersWithKeys = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null) {
                        String userKey = data.getKey(); // Firebase ID for this user
                        usersWithKeys.add(new Pair<>(user, userKey));
                    }
                }
                view.onDataLoaded(usersWithKeys);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                view.onError("Failed to load users: " + error.getMessage());
            }
        });
    }

}