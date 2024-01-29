package com.hero.seoultechteams.view.main.account.setting.policy;

import android.os.Bundle;
import android.view.View;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityServicePolicyBinding;

public class ServicePolicyActivity extends BaseActivity<ActivityServicePolicyBinding> implements View.OnClickListener {

    @Override
    protected ActivityServicePolicyBinding getViewBinding() {
        return ActivityServicePolicyBinding.inflate(getLayoutInflater());
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