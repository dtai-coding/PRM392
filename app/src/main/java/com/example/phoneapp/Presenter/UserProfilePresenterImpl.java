package com.example.phoneapp.Presenter;

import androidx.annotation.NonNull;

import com.example.phoneapp.Model.User;
import com.example.phoneapp.View.UserProfileView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfilePresenterImpl implements UserProfilePresenter {
    private final UserProfileView view;

    public UserProfilePresenterImpl(UserProfileView view) {
        this.view = view;
    }

    @Override
    public void fetchUserInfo(String userId) {
        view.showLoading();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        view.showUserInfo(user);
                    } else {
                        view.showUpdateError("User data is null");
                    }
                } else {
                    view.showUpdateError("User not found");
                }
                view.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.showUpdateError(error.getMessage());
                view.hideLoading();
            }
        });
    }

    @Override
    public void updateUserInfo(String userId, String name, String phone) {
        view.showLoading();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.child("name").setValue(name);
        userRef.child("phone").setValue(phone).addOnCompleteListener(task -> {
            view.hideLoading();
            if (task.isSuccessful()) {
                view.showUpdateSuccess();
            } else {
                view.showUpdateError("Failed to update user information");
            }
        });
    }
}
