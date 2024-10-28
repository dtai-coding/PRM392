package com.example.phoneapp.View;

import com.example.phoneapp.Model.Item;

import java.util.List;

public interface ItemManagementView {
    void onItemAdded();
    void onItemUpdated();
    void onItemDeleted();
    void onDataLoaded(List<Item> items);
    void onError(String message);
}
