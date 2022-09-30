package com.hero.seoultechteams.view.main.account.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.view.main.account.OpenSourceLicenseDialog;
import com.hero.seoultechteams.view.main.account.setting.contract.SettingContract;
import com.hero.seoultechteams.view.main.account.setting.notice.NoticeListActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SettingContract.View {

    private ImageView btnBack;
    private RelativeLayout rlItemNotice, rlItemFont, rlItemDeleteCache, rlItemInquiry, rlItemReview, rlItemOpenSource;
    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;
    public static final String EXTRA_SETTING_DATA = "settingData";
//    private final SettingContract.Presenter presenter = new SettingPresenter(this, getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
//        presenter.getSettingFromDatabase();
        setOnClickListener();
        readyPlayStoreReview();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        rlItemNotice = findViewById(R.id.rl_item_notice);
        rlItemFont = findViewById(R.id.rl_item_font);
        rlItemDeleteCache = findViewById(R.id.rl_item_delete_cache);
        rlItemInquiry = findViewById(R.id.rl_item_inquiry);
        rlItemReview = findViewById(R.id.rl_item_review);
        rlItemOpenSource = findViewById(R.id.rl_item_open_source);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
        rlItemNotice.setOnClickListener(this);
        rlItemFont.setOnClickListener(this);
        rlItemDeleteCache.setOnClickListener(this);
        rlItemInquiry.setOnClickListener(this);
        rlItemReview.setOnClickListener(this);
        rlItemOpenSource.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_item_notice:
                intentNoticeList();
                break;
            case R.id.rl_item_font:
                openFontPopUp();
                break;
            case R.id.rl_item_delete_cache:
                openDeleteCachePopUp();
                break;
            case R.id.rl_item_inquiry:
                openInquiryPopUp();
                break;
            case R.id.rl_item_review:
                launchReviewDialog(reviewManager, reviewInfo);
                break;
            case R.id.rl_item_open_source:
                openOpenSource();
                break;
        }
    }

    private void intentNoticeList() {
        Intent intent = new Intent(this, NoticeListActivity.class);
        startActivity(intent);
    }

    private void openFontPopUp() {
        // TODO: 2021-01-12 선택 가능한 폰트들이 나오는 창이 아래에서 위로 나오게 하기
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
        MaterialAlertDialogBuilder openInquiryAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
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
        // TODO: 2021-01-12 플레이스토어에 있는 리뷰 작성 페이지로 바로 이동하도록 링크를 만들기

        //reviewManager = ReviewManagerFactory.create(this);
        reviewManager = new FakeReviewManager(this);
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    reviewInfo = task.getResult();
                    reviewManager.launchReviewFlow(SettingActivity.this, reviewInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                        }
                    });
                } else {
                    // There was some problem, continue regardless of the result.
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

    private void openOpenSource() {
        OpenSourceLicenseDialog openSourceLicenseDialog = new OpenSourceLicenseDialog(this);
        openSourceLicenseDialog.getOpenSourceLicenseDialog();
    }

    @Override
    public void onGetSetting() {
        Intent intent = new Intent();
//            intent.putExtra(EXTRA_SETTING_DATA, data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedGetSetting() {
        Toast.makeText(SettingActivity.this, "설정을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
    }
}