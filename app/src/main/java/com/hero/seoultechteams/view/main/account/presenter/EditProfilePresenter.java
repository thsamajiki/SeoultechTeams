package com.hero.seoultechteams.view.main.account.presenter;

import android.text.TextUtils;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.domain.user.usecase.UpdateUserUseCase;
import com.hero.seoultechteams.listener.OnFileUploadListener;
import com.hero.seoultechteams.storage.FirebaseStorageAPI;
import com.hero.seoultechteams.view.main.account.contract.EditProfileContract;

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private EditProfileContract.View view;
    private UpdateUserUseCase updateUserUseCase;
    private GetAccountProfileUseCase getAccountProfileUseCase;
    private String myNewProfileImageLocalUri;

    public EditProfilePresenter(EditProfileContract.View view, UpdateUserUseCase updateUserUseCase, GetAccountProfileUseCase getAccountProfileUseCase) {
        this.view = view;
        this.updateUserUseCase = updateUserUseCase;
        this.getAccountProfileUseCase = getAccountProfileUseCase;
    }

    @Override
    public UserEntity getUserEntity() {
        return getAccountProfileUseCase.invoke();
    }

    @Override
    public void updateMyUserData(String editUserName) {

        String profileImageUri = myNewProfileImageLocalUri;


        if (isNewProfileImage()) {
            uploadImageToFirebase(profileImageUri, editUserName);
        } else {
            updateUserProfile(editUserName, profileImageUri);
        }
    }

    private void updateUserProfile(String editUserName, String profileImageUri) {
        UserEntity user = getAccountProfileUseCase.invoke();
        user.setName(editUserName);

        if (!TextUtils.isEmpty(profileImageUri)) {
            user.setProfileImageUrl(profileImageUri);
        }

        updateUserUseCase.invoke(new OnCompleteListener<UserEntity>() {
            @Override
            public void onComplete(boolean isSuccess, UserEntity data) {
                if (isSuccess) {
                    view.updatedMyUserData(data);
                } else {
                    view.failedUpdateMyUserData();
                }
            }
        }, user);
    }

    public boolean isNewProfileImage() {
        return !TextUtils.isEmpty(myNewProfileImageLocalUri);
    }

    public void uploadImageToFirebase(String myNewProfileImageLocalUri, String editUserName) {
        view.initProgressDialog();

        FirebaseStorageAPI.getInstance().setOnFileUploadListener(new OnFileUploadListener() {
            @Override
            public void onFileUploadComplete(boolean isSuccess, String downloadUrl) {
                view.dismissProgressDialog();
                if (isSuccess) {
                    updateUserProfile(editUserName, myNewProfileImageLocalUri);
                } else {
                    view.failedImageUpload();
                }
            }

            @Override
            public void onFileUploadProgress(float percent) {
//                view.failedImageUpload();
            }
        });
        FirebaseStorageAPI.getInstance().uploadImage(FirebaseStorageAPI.DEFAULT_IMAGE_PATH, myNewProfileImageLocalUri);
    }

    @Override
    public void setNewProfileImage(String newImage) {
        myNewProfileImageLocalUri = newImage;
    }
}
