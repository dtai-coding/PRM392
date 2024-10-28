package com.example.phoneapp.View;

import com.example.phoneapp.Model.Item;

import java.util.List;

public interface BodyView {
    void showItems(List<Item> items);
    void onDataLoadFailed(String message);
}

