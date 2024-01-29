package com.hero.seoultechteams.view.main.account.setting.policy;

import android.os.Bundle;
import android.view.View;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends BaseActivity<ActivityPrivacyPolicyBinding> implements View.OnClickListener {

    @Override
    protected ActivityPrivacyPolicyBinding getViewBinding() {
        return ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) finish();
    }
}