package com.example.phoneapp.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phoneapp.Activity.HomeActivity;
import com.example.phoneapp.Model.User;
import com.example.phoneapp.Presenter.UserProfilePresenter;
import com.example.phoneapp.Presenter.UserProfilePresenterImpl;
import com.example.phoneapp.View.UserProfileView;
import com.example.phoneapp.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment implements UserProfileView {

    private FragmentProfileBinding binding;
    private UserProfilePresenter presenter;
    private String userId; // Assume you get this from arguments or session

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UserProfilePresenterImpl(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        userId = homeActivity != null ? homeActivity.getUserId() : null;

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchUserInfo(userId);

        binding.saveBtn.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString().trim();
            String phone = binding.phoneEditText.getText().toString().trim();
            presenter.updateUserInfo(userId, name, phone);
        });
    }

    @Override
    public void showUserInfo(User user) {
        binding.nameEditText.setText(user.getName());
        binding.phoneEditText.setText(user.getPhone());
        binding.emailTextView.setText(user.getEmail()); // Display email as text, not editable
    }

    @Override
    public void showUpdateSuccess() {
        if (isAdded()) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showUpdateError(String error) {
        if (isAdded()) {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        // Optionally show a loading indicator
    }

    @Override
    public void hideLoading() {
        // Optionally hide the loading indicator
    }
}
