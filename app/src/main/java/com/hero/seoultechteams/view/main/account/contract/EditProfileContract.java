package com.hero.seoultechteams.view.main.account.contract;

import android.widget.EditText;

import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.user.entity.UserEntity;

public abstract class EditProfileContract {
    public interface View {
        void updatedMyUserData(UserEntity userData);

        void failedUpdateMyUserData();

        void failedImageUpload();

        void initProgressDialog();

        void dismissProgressDialog();

        void setProgressDialog(int percent);
    }

    public interface Presenter {
        UserEntity getUserEntity();

        void updateMyUserData(String editUserName);

        boolean isNewProfileImage();

        void setNewProfileImage(String newImage);
    }
}