package com.example.phoneapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.phoneapp.Presenter.MainPresenter;
import com.example.phoneapp.Presenter.MainPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.MainView;
import com.example.phoneapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainView {

    private ActivityMainBinding binding;
    private MainPresenter mainPresenter;
    private static final String QR_CODE_URL = "https://firebasestorage.googleapis.com/v0/b/phoneapp-2c850.appspot.com/o/mobile_phone.jpg?alt=media&token=0fb4204a-151a-4907-bacc-186dab4ed42c";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the presenter
        mainPresenter = new MainPresenterImpl(this);

        loadImageFromFirebase(QR_CODE_URL);


        // Handle button click via presenter
        binding.loginButton.setOnClickListener(v -> mainPresenter.onLoginButtonClick());

    }

    @Override
    public void navigateToLogin() {
        // Navigate to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void loadImageFromFirebase(String url) {
        // Use Glide to load the image directly from the URL into the ImageView
        Glide.with(this)
                .load(url)
                .into(binding.ivImage)
                .onLoadFailed(getResources().getDrawable(R.drawable.error_placeholder, null)); // Optional error handling
    }
}