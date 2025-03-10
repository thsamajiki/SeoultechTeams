package com.hero.seoultechteams.view.main.account;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityEditProfileBinding;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.utils.LoadingProgress;
import com.hero.seoultechteams.utils.RealPathUtil;
import com.hero.seoultechteams.view.main.account.contract.EditProfileContract;
import com.hero.seoultechteams.view.main.account.presenter.EditProfilePresenter;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding> implements View.OnClickListener, TextWatcher, EditProfileContract.View {

    private String myCurrentUserName;
    private static final int PERMISSION_REQ_CODE = 1010;
    public static final String EXTRA_UPDATE_USER_DATA = "updateUser";

    private final EditProfileContract.Presenter presenter = new EditProfilePresenter(this,
            Injector.getInstance().provideUpdateUserUseCase(),
            Injector.getInstance().provideGetAccountProfileUseCase());


    @Override
    protected ActivityEditProfileBinding getViewBinding() {
        return ActivityEditProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupListeners();
        setMyCurrentUserData();
    }

    private void setupListeners() {
        binding.editUserName.addTextChangedListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.tvEditProfileComplete.setOnClickListener(this);
        binding.fabProfileImageEdit.setOnClickListener(this);
    }

    private void setMyCurrentUserData() {
        UserEntity user = presenter.getUserEntity();
        String myCurrentProfileImageUri = user.getProfileImageUrl();

        if (myCurrentProfileImageUri == null) {
            Glide.with(this).load(R.drawable.ic_user).into(binding.ivMyUserProfile);
        } else {
            Glide.with(this).load(myCurrentProfileImageUri).into(binding.ivMyUserProfile);
        }

        myCurrentUserName = user.getName();
        binding.editUserName.setText(myCurrentUserName);
        binding.editUserName.setSelection(binding.editUserName.getText().length());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.tv_edit_profile_complete) {
            String myNewUserName = binding.editUserName.getText().toString();
            presenter.updateMyUserData(myNewUserName);
        } else if (view.getId() == R.id.fab_profile_image_edit) {
            if (checkStoragePermission()) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(EXTERNAL_CONTENT_URI, "image/*");
        openGalleryLauncher.launch(pickIntent);
    }

    private final ActivityResultLauncher<Intent> openGalleryLauncher
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        String newImage = RealPathUtil.getRealPath(EditProfileActivity.this, data.getData());
                        presenter.setNewProfileImage(newImage);
                        Glide.with(EditProfileActivity.this).load(newImage).into(binding.ivMyUserProfile);
                        binding.tvEditProfileComplete.setEnabled(true);
                    }
                }
            });

    private boolean checkStoragePermission() {
        String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (ActivityCompat.checkSelfPermission(this, readPermission)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, writePermission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{readPermission, writePermission},
                    PERMISSION_REQ_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals(" ")) {
            binding.tvEditProfileComplete.setText(s.toString().trim());
            return;
        }
        if (s.length() == 0) {
            binding.tvEditProfileComplete.setEnabled(false);
        } else {
            binding.tvEditProfileComplete.setEnabled(!s.toString().equals(myCurrentUserName));
        }
    }

    @Override
    public void updatedMyUserData(UserEntity userData) {
        Toast.makeText(EditProfileActivity.this, "사용자 정보가 변경되었습니다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_UPDATE_USER_DATA, userData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedUpdateMyUserData() {
        Toast.makeText(EditProfileActivity.this, "사용자 정보 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedImageUpload() {
        Toast.makeText(this, "사진 업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initProgressDialog() {
        LoadingProgress.initProgressDialog(this);
    }

    @Override
    public void dismissProgressDialog() {
        LoadingProgress.dismissProgressDialog();
    }

    @Override
    public void setProgressDialog(int percent) {
        LoadingProgress.setProgress(percent);
    }
}