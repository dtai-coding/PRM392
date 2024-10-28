package com.example.phoneapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.Activity.HomeActivity;
import com.example.phoneapp.Adapter.UserItemAdapter;
import com.example.phoneapp.Model.Item;
import com.example.phoneapp.Presenter.BodyPresenter;
import com.example.phoneapp.Presenter.BodyPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.BodyView;
import com.example.phoneapp.Adapter.ItemAdapter;
import com.example.phoneapp.databinding.FragmentBodyBinding;

import java.util.ArrayList;
import java.util.List;

public class BodyFragment extends Fragment implements BodyView {

    private FragmentBodyBinding binding;
    private BodyPresenter presenter;
    private UserItemAdapter itemAdapter;
    public String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new BodyPresenterImpl(this);
        presenter.loadNewestItems(4);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBodyBinding.inflate(inflater, container, false);

        HomeActivity homeActivity = (HomeActivity) getActivity();
        userId = homeActivity != null ? homeActivity.getUserId() : null;

        // Set up RecyclerView
        itemAdapter = new UserItemAdapter(new ArrayList<>(), this::showDetailedItem); // Pass the click handler
        binding.recyclerViewHomeItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHomeItems.setAdapter(itemAdapter);

        binding.tvWelcome.setText("Welcome to the App!");
        // Load items via presenter
        presenter.loadNewestItems(4);

        return binding.getRoot();
    }

    @Override
    public void showItems(List<Item> items) {
        itemAdapter.setItems(items);
    }

    @Override
    public void onDataLoadFailed(String message) {
        if (isAdded()) {
        Toast.makeText(getContext(), "Failed to load data: " + message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetailedItem(Item item) {
        // Replace the fragment to display item details
        FragmentManager fragmentManager = getParentFragmentManager();
        DetailedItemFragment fragment = DetailedItemFragment.newInstance(
                userId,
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl()
        );
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // container to host new fragment
                .addToBackStack(null)
                .commit();
    }
}