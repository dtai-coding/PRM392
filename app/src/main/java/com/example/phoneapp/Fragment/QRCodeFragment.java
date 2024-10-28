package com.example.phoneapp.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phoneapp.R;
import com.example.phoneapp.databinding.FragmentDetailedItemBinding;
import com.example.phoneapp.databinding.FragmentQrCodeBinding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.phoneapp.databinding.FragmentQrCodeBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class QRCodeFragment extends Fragment {

    private FragmentQrCodeBinding binding;
    private static final String QR_CODE_URL = "https://firebasestorage.googleapis.com/v0/b/phoneapp-2c850.appspot.com/o/qrcode.png?alt=media&token=a09598ee-4674-4f0b-9675-32f6c34e3054";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false);

        // Load the hard-coded QR code URL
        loadQRCodeFromFirebase(QR_CODE_URL);

        return binding.getRoot();
    }

    private void loadQRCodeFromFirebase(String url) {
        // Use Glide to load the image directly from the URL into the ImageView
        Glide.with(this)
                .load(url)
                .into(binding.ivQRCode)
                .onLoadFailed(getResources().getDrawable(R.drawable.error_placeholder, null)); // Optional error handling
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
