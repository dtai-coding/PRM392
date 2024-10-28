package com.example.phoneapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.phoneapp.Adapter.ItemAdapter;
import com.example.phoneapp.Model.Item;
import com.example.phoneapp.Presenter.ItemManagementPresenter;
import com.example.phoneapp.Presenter.ItemManagementPresenterImpl;
import com.example.phoneapp.R;
import com.example.phoneapp.View.ItemManagementView;
import com.example.phoneapp.databinding.FragmentItemManagementBinding;

import java.util.List;
public class ItemManagementFragment extends Fragment implements ItemManagementView {

    private FragmentItemManagementBinding binding;
    private ItemManagementPresenter presenter;
    private ItemAdapter adapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private String currentItemID;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentItemManagementBinding.inflate(inflater, container, false);
        presenter = new ItemManagementPresenterImpl(this);

        // Setup Spinner
        setupItemTypeSpinner();
        setupRecyclerView();

        // Handle adding a new item
        binding.buttonAddItem.setOnClickListener(v -> {
            if (binding.editSection.getVisibility() == View.GONE) {
                showEditSection(null);
            } else {
                hideEditSection();
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.etItemName.getText().toString();
            String description = binding.etItemDescription.getText().toString();
            String priceText = binding.etItemPrice.getText().toString();
            String type = binding.spinnerItemType.getSelectedItem().toString();

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || selectedImageUri == null) {
                onError("Please fill in all fields and select an image.");
                return;
            }

            double price = Double.parseDouble(priceText);

            // Upload image first
            presenter.uploadImage(selectedImageUri, imageUrl -> {
                // Once the image is uploaded, create the item with the imageUrl
                Item newItem = new Item(null, name, description, price, imageUrl, type);

                if (currentItemID == null) {
                    presenter.addItem(newItem); // Add a new item with imageUrl
                } else {
                    newItem.setId(currentItemID); // Set ID for updating
                    presenter.updateItem(newItem); // Update item with imageUrl
                }
            });
        });

        // Handle selecting an image
        binding.buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        presenter.loadItems();
        return binding.getRoot();
    }

    // Setup Spinner with hardcoded types
    private void setupItemTypeSpinner() {
        Spinner spinner = binding.spinnerItemType;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.item_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        adapter = new ItemAdapter();
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewItems.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Item item) {
                currentItemID = item.getId(); // Store the ID for updating
                binding.etItemName.setText(item.getName());
                binding.etItemDescription.setText(item.getDescription());
                binding.etItemPrice.setText(String.valueOf(item.getPrice()));

                // Show the input fields for editing
                binding.editSection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDeleteClick(Item item) {
                presenter.deleteItem(item.getId());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(binding.ivSelectedImage);
        }
    }

    private void showEditSection(@Nullable Item item) {
        if (item == null) {
            // Clear input fields for a new item
            currentItemID = null;
            binding.etItemName.setText("");
            binding.etItemDescription.setText("");
            binding.etItemPrice.setText("");
        } else {
            // Populate input fields for editing an existing item
            currentItemID = item.getId();
            binding.etItemName.setText(item.getName());
            binding.etItemDescription.setText(item.getDescription());
            binding.etItemPrice.setText(String.valueOf(item.getPrice()));
        }
        binding.editSection.setVisibility(View.VISIBLE); // Show the edit section
    }

    @Override
    public void onItemAdded() {
        if (isAdded()) {
            Toast.makeText(getContext(), "Item added successfully!", Toast.LENGTH_SHORT).show();
        }
        hideEditSection();
        presenter.loadItems();

        if (selectedImageUri != null) {
            selectedImageUri = null;
        }
    }


    @Override
    public void onItemUpdated() {
        if (isAdded()) {
            Toast.makeText(getContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        }
        hideEditSection();
        presenter.loadItems();
    }

    @Override
    public void onItemDeleted() {
        if (isAdded()) {
            Toast.makeText(getContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
        }
        presenter.loadItems();
    }

    @Override
    public void onDataLoaded(List<Item> items) {
        adapter.setItems(items);
    }

    @Override
    public void onError(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideEditSection() {
        binding.editSection.setVisibility(View.GONE);
        clearInputFields();
    }

    private void clearInputFields() {
        binding.etItemName.setText("");
        binding.etItemDescription.setText("");
        binding.etItemPrice.setText("");
        currentItemID = null;
        binding.ivSelectedImage.setImageResource(R.drawable.ic_placeholder_image);
    }
}
