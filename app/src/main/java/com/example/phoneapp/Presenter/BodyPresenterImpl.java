// BodyPresenterImpl.java
package com.example.phoneapp.Presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.phoneapp.Model.Item;
import com.example.phoneapp.View.BodyView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BodyPresenterImpl implements BodyPresenter {

    private final BodyView bodyView;
    private final DatabaseReference itemsRef;

    public BodyPresenterImpl(BodyView bodyView) {
        this.bodyView = bodyView;
        itemsRef = FirebaseDatabase.getInstance().getReference("Items");
    }

    @Override
    public void loadItems() {
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    items.add(item);
                }
                bodyView.showItems(items);  // Update view with items
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BodyPresenterImpl", "Database error: " + error.getMessage());
                bodyView.onDataLoadFailed(error.getMessage());
            }
        });
    }

    public void loadNewestItems(int limit) {
        itemsRef.orderByChild("timestamp").limitToLast(limit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    items.add(itemSnapshot.getValue(Item.class));
                }
                bodyView.showItems(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bodyView.onDataLoadFailed(error.getMessage());
            }
        });
    }

    public void loadItemsByType(String type) {
        itemsRef.orderByChild("type").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    items.add(itemSnapshot.getValue(Item.class));
                }
                bodyView.showItems(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bodyView.onDataLoadFailed(error.getMessage());
            }
        });
    }
}

