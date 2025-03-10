package com.hero.seoultechteams.view.main.account.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivitySettingBinding;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.LoginActivity;
import com.hero.seoultechteams.view.main.account.OpenSourceLicenseDialog;
import com.hero.seoultechteams.view.main.account.setting.contract.SettingContract;
import com.hero.seoultechteams.view.main.account.setting.notice.NoticeListActivity;
import com.hero.seoultechteams.view.main.account.setting.policy.PrivacyPolicyActivity;
import com.hero.seoultechteams.view.main.account.setting.policy.ServicePolicyActivity;
import com.hero.seoultechteams.view.main.account.setting.presenter.SettingPresenter;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> implements View.OnClickListener, SettingContract.View {

    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;
    public static final String EXTRA_SETTING_DATA = "settingData";

    private final SettingContract.Presenter presenter = new SettingPresenter(this,
            Injector.getInstance().provideGetAccountProfileUseCase(),
            Injector.getInstance().provideRemoveUserUseCase());

    @Override
    protected ActivitySettingBinding getViewBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnClickListener();
        readyPlayStoreReview();
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.layoutItemNotice.setOnClickListener(this);
        binding.layoutItemFont.setOnClickListener(this);
        binding.layoutItemDeleteCache.setOnClickListener(this);
        binding.layoutItemInquiry.setOnClickListener(this);
        binding.layoutItemReview.setOnClickListener(this);
        binding.layoutItemServicePolicy.setOnClickListener(this);
        binding.layoutItemPrivacyPolicy.setOnClickListener(this);
        binding.layoutItemOpenSource.setOnClickListener(this);
        binding.layoutItemDropOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.layout_item_notice) {
            onNoticeLayoutClick();
        } else if (view.getId() == R.id.layout_item_font) {
            openFontPopUp();
        } else if (view.getId() == R.id.layout_item_delete_cache) {
            openDeleteCachePopUp();
        } else if (view.getId() == R.id.layout_item_inquiry) {
            openInquiryPopUp();
        } else if (view.getId() == R.id.layout_item_review) {
            launchReviewDialog(reviewManager, reviewInfo);
        } else if (view.getId() == R.id.layout_item_service_policy) {
            onClickServicePolicy();
        } else if (view.getId() == R.id.layout_item_privacy_policy) {
            onClickPrivacyPolicy();
        } else if (view.getId() == R.id.layout_item_open_source) {
            openOpenSource();
        } else if (view.getId() == R.id.layout_item_drop_out) {
            openDropOutDialog();
        }
    }

    private void onNoticeLayoutClick() {
        Intent intent = new Intent(this, NoticeListActivity.class);
        startActivity(intent);
    }

    private void openFontPopUp() {
    }

    private void openDeleteCachePopUp() {
        String deleteCachePopUpMessage = "캐시 데이터 삭제 완료";
        String negativeText = "닫기";

        new MaterialAlertDialogBuilder(this).setMessage(deleteCachePopUpMessage)
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void openInquiryPopUp() {
        String openInquiryTitle = "문의하기";
        String openInquiryMessage = "chs8275@gmail.com으로\n문의 부탁드립니다 :)";
        String positiveText = "확인";

        new MaterialAlertDialogBuilder(this).setTitle(openInquiryTitle)
                .setMessage(openInquiryMessage)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void readyPlayStoreReview() {
        reviewManager = new FakeReviewManager(this);
        reviewManager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    reviewManager.launchReviewFlow(SettingActivity.this, reviewInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                        }
                    });
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SettingActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchReviewDialog(ReviewManager reviewManager, ReviewInfo reviewInfo) {
        if (reviewInfo == null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=my packagename "));
            startActivity(intent);
        }
        Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        });
    }

    private void onClickServicePolicy() {
        Intent intent = new Intent(this, ServicePolicyActivity.class);
        startActivity(intent);
    }

    private void onClickPrivacyPolicy() {
        Intent intent = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    private void openOpenSource() {
        OpenSourceLicenseDialog openSourceLicenseDialog = new OpenSourceLicenseDialog(this);
        openSourceLicenseDialog.getOpenSourceLicenseDialog();
    }

    private void openDropOutDialog() {
        String openDropOutTitle = "탈퇴";
        String openDropOutMessage = "정말로 탈퇴하시겠습니까?";
        String positiveText = "예";
        String negativeText = "아니오";

        new MaterialAlertDialogBuilder(this).setTitle(openDropOutTitle)
                .setMessage(openDropOutMessage)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dropOut();
                    }
                })
                .setNegativeButton(negativeText, null)
                .create()
                .show();
    }

    private void dropOut() {
        UserEntity user = presenter.getUserEntity();
        presenter.dropOut(user);
    }

    @Override
    public void onGetSetting() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedGetSetting() {
        Toast.makeText(SettingActivity.this, "설정을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDropOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void failedDropOut() {
        Toast.makeText(SettingActivity.this, "회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show();
    }
}