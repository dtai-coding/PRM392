package com.example.phoneapp.Presenter;

import android.net.Uri;

import com.example.phoneapp.Model.Item;
import com.example.phoneapp.View.ItemManagementView;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import java.util.*;

public class ItemManagementPresenterImpl implements ItemManagementPresenter {
    private final ItemManagementView view;
    private final DatabaseReference itemsRef;
    private final StorageReference storageRef;

    public ItemManagementPresenterImpl(ItemManagementView view) {
        this.view = view;
        this.itemsRef = FirebaseDatabase.getInstance().getReference("Items");
        this.storageRef = FirebaseStorage.getInstance().getReference("item_images");
    }

    @Override
    public void addItem(Item item) {
        String itemId = itemsRef.push().getKey(); // Generate a unique ID
        item.setId(itemId); // Set this ID in the item

        itemsRef.child(itemId).setValue(item)
                .addOnSuccessListener(aVoid -> view.onItemAdded())
                .addOnFailureListener(e -> view.onError("Failed to add item: " + e.getMessage()));
    }


    @Override
    public void updateItem(Item item) {
        itemsRef.child(item.getId()).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onItemUpdated();
            } else {
                view.onError("Failed to update item.");
            }
        });
    }

    @Override
    public void deleteItem(String itemId) {
        itemsRef.child(itemId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onItemDeleted();
            } else {
                view.onError("Failed to delete item.");
            }
        });
    }

    @Override
    public void loadItems() {
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Item item = data.getValue(Item.class);
                    items.add(item);
                }
                view.onDataLoaded(items);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                view.onError("Failed to load items: " + error.getMessage());
            }
        });
    }

    public void uploadImage(Uri imageUri, ItemManagementPresenter.OnImageUploadCallback callback) {
        StorageReference imageRef = storageRef.child(UUID.randomUUID().toString() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Pass the image URL to the callback
                            callback.onSuccess(uri.toString());
                        })
                )
                .addOnFailureListener(e -> view.onError("Image upload failed: " + e.getMessage()));
    }
}

