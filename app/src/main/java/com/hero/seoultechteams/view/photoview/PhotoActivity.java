package com.hero.seoultechteams.view.photoview;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityPhotoBinding;


public class PhotoActivity extends BaseActivity<ActivityPhotoBinding> implements View.OnClickListener {

    public static final String EXTRA_PROFILE_IMAGE_URL = "profileImageUrl";

    @Override
    protected ActivityPhotoBinding getViewBinding() {
        return ActivityPhotoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadImage();
    }

    public String getPhotoUrl() {
        return getIntent().getStringExtra(EXTRA_PROFILE_IMAGE_URL);
    }

    private void loadImage() {
        Glide.with(this).load(getPhotoUrl()).into(binding.photoView);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        }
    }
}