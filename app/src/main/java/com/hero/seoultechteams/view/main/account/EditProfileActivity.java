package com.hero.seoultechteams.view.main.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.user.UserRepository;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.listener.OnFileUploadListener;
import com.hero.seoultechteams.storage.FirebaseStorageAPI;
import com.hero.seoultechteams.utils.LoadingProgress;
import com.hero.seoultechteams.utils.RealPathUtil;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


public class EditProfileActivity extends BaseActivity implements View.OnClickListener, OnFileUploadListener, TextWatcher {

    private ImageView btnBack;
    private TextView tvEditProfileComplete;
    private TextInputEditText editUserName;
    private CircleImageView ivProfile;
    private FloatingActionButton fabProfileImageEdit;
    private String myNewProfileImageLocalUri;
    private String myCurrentUserName;
    private static final int PERMISSION_REQ_CODE = 1010;    // 권한 요청 코드
    private static final int PHOTO_REQ_CODE = 2020;         // 사진 요청 코드
    public static final String EXTRA_UPDATE_USER_DATA = "updateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        setMyCurrentUserData();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        tvEditProfileComplete = findViewById(R.id.tv_edit_profile_complete);
        editUserName = findViewById(R.id.edit_user_name);
        ivProfile = findViewById(R.id.iv_member_profile);
        fabProfileImageEdit = findViewById(R.id.fab_profile_image_edit);

        setOnClickListener();

        editUserName.addTextChangedListener(this);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
        tvEditProfileComplete.setOnClickListener(this);
        fabProfileImageEdit.setOnClickListener(this);
    }

    private void setMyCurrentUserData() {
        FirebaseUser firebaseUser = getCurrentUser();
        Uri myCurrentProfileImageUri = firebaseUser.getPhotoUrl();

        if (myCurrentProfileImageUri == null) {
            Glide.with(this).load(R.drawable.ic_user).into(ivProfile);
        } else {
            Glide.with(this).load(myCurrentProfileImageUri).into(ivProfile);
        }

        myCurrentUserName = firebaseUser.getDisplayName();
        editUserName.setText(myCurrentUserName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit_profile_complete:
                // 새로운 프로필 사진인지 검사가 완료되면, 활성화되고, 클릭하면 아래의 기능을 실행한다. -> 무엇이든 하나라도 변화가 있었다는 것을 뜻한다.
                // 프로필 사진을 업로드하고 사용자 데이터를 업로드할 것인가? 또는 사용자 데이터만 업로드할 것인가?
                if (isNewProfileImage()) {
                    uploadImageToFirebase();
                } else {
                    FirebaseUser firebaseUser = getCurrentUser();
                    Uri profileImageUri = firebaseUser.getPhotoUrl();
                    if (profileImageUri != null) {
                        updateMyUserData(profileImageUri.toString());
                    } else {
                        updateMyUserData(null);
                    }
                }
                break;
            case R.id.fab_profile_image_edit:
                if (checkStoragePermission()) {
                    intentGallery();
                }
                break;
        }
    }

    private void uploadImageToFirebase() {
        LoadingProgress.initProgressDialog(this);
        FirebaseStorageAPI.getInstance().setOnFileUploadListener(this);
        FirebaseStorageAPI.getInstance().uploadImage(FirebaseStorageAPI.DEFAULT_IMAGE_PATH, myNewProfileImageLocalUri);
    }

    private boolean isNewProfileImage() {
        return !TextUtils.isEmpty(myNewProfileImageLocalUri);
    }

    private void updateMyUserData(String profileImageUri) {
        UserRepository userRepository = new UserRepository(this);
        FirebaseUser firebaseUser = getCurrentUser();
        String myNewUserName = editUserName.getText().toString();

        UserData userData = new UserData();
        userData.setName(myNewUserName);
        userData.setEmail(firebaseUser.getEmail());
        userData.setKey(firebaseUser.getUid());
        userData.setProfileImageUrl(profileImageUri);

        userRepository.updateUser((isSuccess, data) -> {
            if (isSuccess) {
                Toast.makeText(EditProfileActivity.this, "사용자 정보가 변경되었습니다!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_UPDATE_USER_DATA, userData);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(EditProfileActivity.this, "사용자 정보 변경에 실패했습니다!", Toast.LENGTH_SHORT).show();
            }
        }, userData);
    }


    private void intentGallery() {  // 인텐트를 이용하여 사진 갤러리를 열기
        Intent pickIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PHOTO_REQ_CODE);
    }

    private boolean checkStoragePermission() {  // 저장소의 읽기 & 쓰기 권한 허용 여부 체크
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
            intentGallery();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_REQ_CODE && resultCode == RESULT_OK && data != null) {
            myNewProfileImageLocalUri = RealPathUtil.getRealPath(this, data.getData());
            Glide.with(this).load(myNewProfileImageLocalUri).into(ivProfile);
            tvEditProfileComplete.setEnabled(true);
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
            tvEditProfileComplete.setText(s.toString().trim());
            return;
        }
        if (s.length() == 0) {
            tvEditProfileComplete.setEnabled(false);
        } else {
            if (s.toString().equals(myCurrentUserName)) {
                if (isNewProfileImage()) {
                    tvEditProfileComplete.setEnabled(true);
                } else {
                    tvEditProfileComplete.setEnabled(false);
                }
            } else {
                tvEditProfileComplete.setEnabled(true);
            }
        }
    }

    @Override
    public void onFileUploadComplete(boolean isSuccess, String downloadUrl) {
        LoadingProgress.dismissProgressDialog();
        if (isSuccess) {
            updateMyUserData(downloadUrl);
        } else {
            Toast.makeText(this, "사진 업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFileUploadProgress(float percent) {
        LoadingProgress.setProgress((int) percent);
    }
}