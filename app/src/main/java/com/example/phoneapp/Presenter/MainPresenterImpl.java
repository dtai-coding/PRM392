package com.example.phoneapp.Presenter;

import com.example.phoneapp.View.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    public MainPresenterImpl(MainView view) {
        this.mainView = view;
    }

    @Override
    public void onLoginButtonClick() {
        // Notify the view to navigate to the login screen
        mainView.navigateToLogin();
    }
}
