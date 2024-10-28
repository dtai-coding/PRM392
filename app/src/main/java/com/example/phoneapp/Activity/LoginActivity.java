package com.example.phoneapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phoneapp.Presenter.LoginPresenter;
import com.example.phoneapp.Presenter.LoginPresenterImpl;
import com.example.phoneapp.View.LoginView;
import com.example.phoneapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private ActivityLoginBinding binding; // ViewBinding
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LoginPresenterImpl(this);

        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.Email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            // Validate inputs
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

            // If validation passes, proceed with login
            presenter.login(email, password);
        });

        binding.toRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Uncomment if you have a forgot password feature
        // binding.forgotPassword.setOnClickListener(v -> {
        //     Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
        //     startActivity(intent);
        // });
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(String error) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
