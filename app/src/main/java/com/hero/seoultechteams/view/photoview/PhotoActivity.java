package com.hero.seoultechteams.view.photoview;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.hero.seoultechteams.databinding.ActivityPhotoBinding;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityPhotoBinding binding;
    private ImageView btnBack;
    private PhotoView photoView;
    public static final String EXTRA_PROFILE_IMAGE_URL = "profileImageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        initView();
        loadImage();
    }

    public String getPhotoUrl() {
        return getIntent().getStringExtra(EXTRA_PROFILE_IMAGE_URL);
    }

//    private void initView() {
//        btnBack = findViewById(R.id.iv_back);
//        photoView = findViewById(R.id.photo_view);
//        btnBack.setOnClickListener(this);
//    }

    private void loadImage() {
        Glide.with(this).load(getPhotoUrl()).into(binding.photoView);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}