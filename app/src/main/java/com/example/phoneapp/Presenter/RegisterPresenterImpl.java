package com.example.phoneapp.Presenter;


import com.example.phoneapp.Model.User;
import com.example.phoneapp.R;
import com.example.phoneapp.View.RegisterView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView registerView;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterPresenterImpl(RegisterView view) {
        this.registerView = view;
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public void registerUser(String email, String password, String name, String phone) {
        // Register the user using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the newly registered user
                FirebaseUser user = auth.getCurrentUser();

                // Save additional details in Realtime Database
                if (user != null) {
                    String[] roles = registerView.getContext().getResources().getStringArray(R.array.roles);
                    String defaultRole = roles[0];

                    User newUser = new User(name, phone, email, defaultRole);
                    database.child(user.getUid()).setValue(newUser)
                            .addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    registerView.showRegisterSuccess();
                                } else {
                                    registerView.showRegisterError("Database Error: " + dbTask.getException().getMessage());
                                }
                            });
                }
            } else {
                registerView.showRegisterError("Registration Error: " + task.getException().getMessage());
            }
        });
    }
}
