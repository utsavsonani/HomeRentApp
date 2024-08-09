package com.example.homerentapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.homerentapp.databinding.ActivityPostPropertyBinding;

import java.util.ArrayList;

public class PostPropertyActivity extends AppCompatActivity {

    private ActivityPostPropertyBinding binding;
    private static final int GALLERY_REQUEST_CODE = 1;

    private ArrayList<Uri> imageUri = new ArrayList<>();
    private ViewPageAdapter adapter;

    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPostPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager2 = findViewById(R.id.viewPager);

        // Initialize the adapter with the empty list
        adapter = new ViewPageAdapter(this, imageUri);
        viewPager2.setAdapter(adapter);

        binding.postProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostPropertyActivity.this, PostHomeActivity.class));
            }
        });

        binding.uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getClipData() != null) {
                binding.imageView.setVisibility(View.INVISIBLE);
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUri.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                binding.imageView.setVisibility(View.INVISIBLE);
                imageUri.add(data.getData());
            }

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged();
        }
    }
}
