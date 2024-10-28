package com.example.phoneapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.phoneapp.Adapter.ItemAdapter;
import com.example.phoneapp.Adapter.UserManagementAdapter;
import com.example.phoneapp.Model.Item;
import com.example.phoneapp.Model.User;
import com.example.phoneapp.Presenter.ItemManagementPresenter;
import com.example.phoneapp.Presenter.ItemManagementPresenterImpl;
import com.example.phoneapp.Presenter.UserManagementPresenter;
import com.example.phoneapp.Presenter.UserManagementPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.ItemManagementView;
import com.example.phoneapp.View.UserManagementView;
import com.example.phoneapp.databinding.FragmentItemManagementBinding;
import com.example.phoneapp.databinding.FragmentUserManagementBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class UserManagementFragment extends Fragment implements UserManagementView {

    private FragmentUserManagementBinding binding;
    private UserManagementPresenter presenter;
    private UserManagementAdapter adapter;
    private String currentUserKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserManagementBinding.inflate(inflater, container, false);
        presenter = new UserManagementPresenterImpl(this);

        setupRoleSpinner();
        setupRecyclerView();

        binding.buttonAddUser.setOnClickListener(v -> {
            if (binding.editSection.getVisibility() == View.GONE) {
                showEditSection(null, null);
            } else {
                hideEditSection();
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.etUsername.getText().toString();
            String phone = binding.etPhone.getText().toString();
            String email = binding.etEmail.getText().toString();
            String role = binding.spinnerRole.getSelectedItem().toString();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || role.isEmpty()) {
                onError("Please fill in all fields.");
                return;
            }

            if (currentUserKey == null) {  // New user, use Firebase Authentication
                String password = binding.etPassword.getText().toString();
                if (password.isEmpty()) {
                    onError("Password is required for new users.");
                    return;
                }

                // Call Firebase Authentication to create user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                presenter.addUser(uid, new User(name, phone, email, role));
                                binding.etPassword.setText("");  // Clear password field after creation
                            } else {
                                onError("Failed to create user: " + task.getException().getMessage());
                            }
                        });
            } else {  // Existing user, update details without Firebase Authentication
                presenter.updateUser(currentUserKey, new User(name, phone, email, role));
            }
        });

        presenter.loadUser();
        return binding.getRoot();
    }

    private void setupRoleSpinner() {
        Spinner spinner = binding.spinnerRole;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        adapter = new UserManagementAdapter();
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsers.setAdapter(adapter);

        adapter.setOnItemClickListener(new UserManagementAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(User user, String userKey) {
                currentUserKey = userKey;
                showEditSection(user, userKey);
            }

            @Override
            public void onDeleteClick(String userKey) {
                presenter.deleteUser(userKey);
            }
        });
    }

    private void showEditSection(@Nullable User user, @Nullable String userKey) {
        if (user == null) {
            currentUserKey = null;
            binding.etUsername.setText("");
            binding.etPhone.setText("");
            binding.etEmail.setText("");
            binding.etPassword.setVisibility(View.VISIBLE);
        } else {
            currentUserKey = userKey; // Use the provided Firebase key
            binding.etUsername.setText(user.getName());
            binding.etPhone.setText(user.getPhone());
            binding.etEmail.setText(user.getEmail());
            binding.etPassword.setVisibility(View.GONE);
        }
        binding.editSection.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserAdded() {
        if (isAdded()) {
            Toast.makeText(getContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
        }
        hideEditSection();
        presenter.loadUser();
    }

    @Override
    public void onUserUpdated() {
        if (isAdded()) {
            Toast.makeText(getContext(), "User updated successfully!", Toast.LENGTH_SHORT).show();
        }
        hideEditSection();
        presenter.loadUser();
    }

    @Override
    public void onUserDeleted() {
        if (isAdded()) {
            Toast.makeText(getContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
        }
        presenter.loadUser();
    }

    @Override
    public void onDataLoaded(List<Pair<User, String>> usersWithKeys) {
        adapter.setUsers(usersWithKeys);
    }

    @Override
    public void onError(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideEditSection() {
        binding.editSection.setVisibility(View.GONE);
        clearInputFields();
    }

    private void clearInputFields() {
        binding.etUsername.setText("");
        binding.etPhone.setText("");
        binding.etEmail.setText("");
        currentUserKey = null;
    }
}



