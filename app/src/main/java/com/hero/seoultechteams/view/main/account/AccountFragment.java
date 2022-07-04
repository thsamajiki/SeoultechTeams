package com.hero.seoultechteams.view.main.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.BaseFragment;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.hero.seoultechteams.view.main.account.EditProfileActivity.EXTRA_UPDATE_USER_DATA;
import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;


public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView ivMyUserProfile;
    private TextView tvMyUserName, tvMyUserEmail;
    private MaterialButton btnEditProfile;
//    private ArrayList<MyNotificationData> myNotificationDataList = new ArrayList<>();
    private static final int EDIT_PROFILE_REQ = 1010;
    public static final String EXTRA_MY_USER_DATA = "userData";
    public static final String EXTRA_MY_NOTIFICATION_DATA = "myNotificationData";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflater는 xml로 정의된 view (또는 menu 등)를 실제 view 객체로 만든다
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        setOnClickListener();
        //getMyNotificationFromDatabase();
        return view;
    }

    private void initView(View view) {
        ivMyUserProfile = view.findViewById(R.id.iv_my_user_profile);
        tvMyUserName = view.findViewById(R.id.tv_my_user_name);
        tvMyUserEmail = view.findViewById(R.id.tv_my_user_email);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
    }

    private void setOnClickListener() {
        ivMyUserProfile.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserData(convertToUserData());
    }

    private void setUserData(UserData userData) {
        if (TextUtils.isEmpty(userData.getProfileImageUrl())) {
            Glide.with(requireActivity()).load(R.drawable.ic_user).into(ivMyUserProfile);
        } else {
            Glide.with(requireActivity()).load(userData.getProfileImageUrl()).into(ivMyUserProfile);
        }
        tvMyUserName.setText(userData.getName());
        tvMyUserEmail.setText(userData.getEmail());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_user_profile:
                String myProfileImageUrl = convertToUserData().getProfileImageUrl();
                intentPhoto(myProfileImageUrl);
                break;
            case R.id.btn_edit_profile:
                intentEditProfile();
                break;
        }
    }

    private void intentPhoto(String profileImageUrl) {
        Intent intent = new Intent(requireActivity(), PhotoActivity.class);
        intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
        startActivity(intent);
    }

    private void intentEditProfile() {
        Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
        startActivityForResult(intent, EDIT_PROFILE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQ && resultCode == RESULT_OK && data != null) {
            UserData userData = data.getParcelableExtra(EXTRA_UPDATE_USER_DATA);
            if (userData != null) {
                setUserData(userData);
            }
        }
    }

    private UserData convertToUserData() {
        FirebaseUser firebaseUser = getCurrentUser();
        UserData userData = new UserData();
        userData.setKey(firebaseUser.getUid());
        userData.setName(firebaseUser.getDisplayName());
        userData.setEmail(firebaseUser.getEmail());
        if (firebaseUser.getPhotoUrl() != null) {
            userData.setProfileImageUrl(firebaseUser.getPhotoUrl().toString());
        } else {
            userData.setProfileImageUrl(null);
        }

        return userData;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_account_actionbar_option, menu);
    }


//    private void getMyNotificationFromDatabase() {
//        MyNotificationRepository myNotificationRepository = new MyNotificationRepository(requireActivity());
//        MyNotificationData myNotificationData = new MyNotificationData();
//        myNotificationRepository.getMyNotificationList(new OnCompleteListener<ArrayList<MyNotificationData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<MyNotificationData> data) {
//                if (isSuccess && data != null) {
//                    myNotificationDataList.clear();
//                    myNotificationDataList.addAll(data);
//                } else {
//                    Toast.makeText(requireActivity(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, myNotificationData.getNotificationKey());
//    }
//
//    private MyNotificationData getMyNotificationData() {
//        return getActivity().getIntent().getParcelableExtra(EXTRA_MY_NOTIFICATION_DATA);
//    }
}