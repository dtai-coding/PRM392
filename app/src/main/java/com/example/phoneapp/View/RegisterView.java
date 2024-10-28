package com.example.phoneapp.View;

import android.content.Context;

public interface RegisterView {
    void showRegisterSuccess();
    void showRegisterError(String error);
    Context getContext();
    void showLoading();
    void hideLoading();
}
