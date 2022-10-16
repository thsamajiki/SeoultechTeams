package com.hero.seoultechteams.view.main.account;

import static android.app.Activity.RESULT_OK;
import static com.hero.seoultechteams.view.main.account.EditProfileActivity.EXTRA_UPDATE_USER_DATA;
import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hero.seoultechteams.BaseFragment;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.main.account.contract.AccountContract;
import com.hero.seoultechteams.view.main.account.presenter.AccountPresenter;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment implements View.OnClickListener, AccountContract.View {

    private CircleImageView ivMyUserProfile;
    private TextView tvMyUserName, tvMyUserEmail;
    private MaterialButton btnEditProfile;
//    private ArrayList<MyNotificationData> myNotificationDataList = new ArrayList<>();
    public static final String EXTRA_MY_NOTIFICATION_DATA = "myNotificationData";

    private final AccountContract.Presenter presenter = new AccountPresenter(this,
            Injector.getInstance().provideGetAccountProfileUseCase());

    private final ActivityResultLauncher<Intent> // 화면 간 이동에 대한 결과값을 받음
            editProfileResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        UserEntity userData = data.getParcelableExtra(EXTRA_UPDATE_USER_DATA);
                        if (userData != null) {
                            setUserData(userData);
                        }
                    }
                }
            });

    private final ActivityResultLauncher<Intent> photoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        UserEntity userData = data.getParcelableExtra(EXTRA_UPDATE_USER_DATA);
                        if (userData != null) {
                            setUserData(userData);
                        }
                    }
                }
            }
    );


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserData(presenter.getUserEntity());
    }

    private void setUserData(UserEntity userData) {
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
                String myProfileImageUrl = presenter.getUserEntity().getProfileImageUrl();
                intentPhoto(myProfileImageUrl);
                break;
            case R.id.btn_edit_profile:
                intentEditProfile();
                break;
        }
    }

    private void intentPhoto(String profileImageUrl) {
        Log.d("photo", "intentPhoto: " + profileImageUrl);
        Intent intent = new Intent(requireActivity(), PhotoActivity.class);
        intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
        photoResultLauncher.launch(intent);
    }

    private void intentEditProfile() {
        Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
        editProfileResultLauncher.launch(intent);
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