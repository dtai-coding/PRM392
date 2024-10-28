package com.example.phoneapp.Presenter;

import com.example.phoneapp.View.LoginView;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private FirebaseAuth auth;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginView.onLoginSuccess();
            } else {
                loginView.onLoginFailed("Login failed: " + task.getException().getMessage());
            }
        });
    }
}


