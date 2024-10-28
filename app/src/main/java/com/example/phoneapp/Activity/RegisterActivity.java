package com.example.phoneapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phoneapp.Presenter.RegisterPresenter;
import com.example.phoneapp.Presenter.RegisterPresenterImpl;
import com.example.phoneapp.View.RegisterView;
import com.example.phoneapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private ActivityRegisterBinding binding;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up View Binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the presenter
        registerPresenter = new RegisterPresenterImpl(this);

        // Set button click listener
        binding.registerBtn.setOnClickListener(v -> {
            String email = binding.Email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String name = binding.fullName.getText().toString().trim();
            String phone = binding.phone.getText().toString().trim();

            // Validate inputs
            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                binding.fullName.requestFocus(); // Optional: Focus on the name field
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                binding.Email.requestFocus(); // Optional: Focus on the email field
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                binding.password.requestFocus(); // Optional: Focus on the password field
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Phone cannot be empty", Toast.LENGTH_SHORT).show();
                binding.phone.requestFocus(); // Optional: Focus on the phone field
                return;
            }

            // Call the presenter to handle registration
            registerPresenter.registerUser(email, password, name, phone);
        });

        binding.toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        // Navigate back to LoginActivity or MainActivity
        finish();
    }

    @Override
    public void showRegisterError(String error) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public Context getContext() {
        return this;  // Return activity context
    }

    @Override
    public void showLoading() {
        // Show loading indicator (if needed)
    }

    @Override
    public void hideLoading() {
        // Hide loading indicator (if needed)
    }
}
