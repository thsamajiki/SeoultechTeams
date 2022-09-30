package com.hero.seoultechteams.view.main.account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.listener.OnFileUploadListener;
import com.hero.seoultechteams.storage.FirebaseStorageAPI;
import com.hero.seoultechteams.utils.LoadingProgress;
import com.hero.seoultechteams.utils.RealPathUtil;
import com.hero.seoultechteams.view.main.account.contract.EditProfileContract;
import com.hero.seoultechteams.view.main.account.presenter.EditProfilePresenter;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, TextWatcher, EditProfileContract.View {

    private ImageView btnBack;
    private TextView tvEditProfileComplete;
    private TextInputEditText editUserName;
    private CircleImageView ivProfile;
    private FloatingActionButton fabProfileImageEdit;
    private String myCurrentUserName;
    private static final int PERMISSION_REQ_CODE = 1010;    // 권한 요청 코드
    private static final int PHOTO_REQ_CODE = 2020;         // 사진 요청 코드
    public static final String EXTRA_UPDATE_USER_DATA = "updateUser";
    private ActivityResultLauncher<Intent> intentGalleryLauncher
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
                        Glide.with(EditProfileActivity.this).load(newImage).into(ivProfile);
                        tvEditProfileComplete.setEnabled(true);
                    }
                }
            });

    private final EditProfileContract.Presenter presenter = new EditProfilePresenter(this,
            Injector.getInstance().provideUpdateUserUseCase(),
            Injector.getInstance().provideGetAccountProfileUseCase());

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
        UserEntity user = presenter.getUserEntity();
        String myCurrentProfileImageUri = user.getProfileImageUrl();

        if (myCurrentProfileImageUri == null) {
            Glide.with(this).load(R.drawable.ic_user).into(ivProfile);
        } else {
            Glide.with(this).load(myCurrentProfileImageUri).into(ivProfile);
        }

        myCurrentUserName = user.getName();
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
                String myNewUserName = editUserName.getText().toString();
                presenter.updateMyUserData(myNewUserName);

                break;
            case R.id.fab_profile_image_edit:
                if (checkStoragePermission()) {
                    intentGallery();
                }
                break;
        }
    }

//    private void updateMyUserData(String profileImageUri) {
//        UserRepositoryImpl userRepository = new UserRepositoryImpl(this);
//        FirebaseUser firebaseUser = getCurrentUser();
//        String myNewUserName = editUserName.getText().toString();
//
//        UserData userData = new UserData();
//        userData.setName(myNewUserName);
//        userData.setEmail(firebaseUser.getEmail());
//        userData.setKey(firebaseUser.getUid());
//        userData.setProfileImageUrl(profileImageUri);
//
//        userRepository.updateUser((isSuccess, data) -> {
//            if (isSuccess) {
//                Toast.makeText(EditProfileActivity.this, "사용자 정보가 변경되었습니다!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_UPDATE_USER_DATA, userData);
//                setResult(RESULT_OK, intent);
//                finish();
//            } else {
//                Toast.makeText(EditProfileActivity.this, "사용자 정보 변경에 실패했습니다!", Toast.LENGTH_SHORT).show();
//            }
//        }, userData);
//    }


    private void intentGallery() {  // 인텐트를 이용하여 사진 갤러리를 열기
        Intent pickIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        intentGalleryLauncher.launch(pickIntent);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PHOTO_REQ_CODE && resultCode == RESULT_OK && data != null) {
//            String newImage = RealPathUtil.getRealPath(this, data.getData());
//            presenter.setNewProfileImage(newImage);
//            Glide.with(this).load(newImage).into(ivProfile);
//            tvEditProfileComplete.setEnabled(true);
//        }
//    }

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
            tvEditProfileComplete.setEnabled(!s.toString().equals(myCurrentUserName));
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