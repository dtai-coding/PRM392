package com.example.phoneapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.phoneapp.Presenter.DashboardPresenter;
import com.example.phoneapp.Presenter.DashboardPresenterImpl;
import com.example.phoneapp.View.DashboardView;
import com.example.phoneapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements DashboardView {

    private FragmentDashboardBinding binding;
    private DashboardPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        presenter = new DashboardPresenterImpl(this);

        // Load dashboard data
        presenter.loadDashboardData();

        return binding.getRoot();
    }

    @Override
    public void showTotalUsers(int count) {
        binding.tvTotalUsers.setText("Total Users: " + count);
    }

    @Override
    public void showTotalItems(int count) {
        binding.tvTotalItems.setText("Total Items: " + count);
    }

    @Override
    public void showTotalCarts(int count) {
        binding.tvTotalCarts.setText("Total Carts: " + count);
    }

    @Override
    public void showTotalProfit(double profit) {
        binding.tvTotalProfit.setText("Total Profit: $" + String.format("%.2f", profit));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

