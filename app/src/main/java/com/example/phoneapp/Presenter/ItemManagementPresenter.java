package com.example.phoneapp.Presenter;

import android.net.Uri;

import com.example.phoneapp.Model.Item;

public interface ItemManagementPresenter {
    void addItem(Item item);
    void updateItem(Item item);
    void deleteItem(String itemId);
    void loadItems();

    void uploadImage(Uri imageUri, OnImageUploadCallback callback);

    interface OnImageUploadCallback {
        void onSuccess(String imageUrl);
    }
}
