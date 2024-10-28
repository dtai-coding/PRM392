package com.example.phoneapp.Presenter;

public interface BodyPresenter {
    void loadItems();
    public void loadNewestItems(int limit);
    public void loadItemsByType(String type);
}