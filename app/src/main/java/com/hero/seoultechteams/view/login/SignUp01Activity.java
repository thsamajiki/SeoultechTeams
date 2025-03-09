package com.hero.seoultechteams.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivitySignUp01Binding;
import com.hero.seoultechteams.view.login.contract.SignUp01Contract;
import com.hero.seoultechteams.view.login.presenter.SignUp01Presenter;


public class SignUp01Activity extends BaseActivity<ActivitySignUp01Binding> implements View.OnClickListener, SignUp01Contract.View {

    private final SignUp01Contract.Presenter presenter = new SignUp01Presenter(this);

    @Override
    protected ActivitySignUp01Binding getViewBinding() {
        return ActivitySignUp01Binding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.btnNextStepSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.btn_next_step_sign_up) {
            enterNextStepSignUp();
        }
    }

    private void enterNextStepSignUp() {
        String email = binding.editEmail.getText().toString().trim();
        String pwd = binding.editPwd.getText().toString().trim();
        String pwdConfirm = binding.editPwdConfirm.getText().toString().trim();

        if (presenter.checkLoginForm(email, pwd, pwdConfirm)) {
            Intent intent = new Intent(this, SignUp02Activity.class);
            intent.putExtra("email", email);
            intent.putExtra("pwd", pwd);
            startActivity(intent);
        }
    }

    @Override
    public void onInvalidUserInfo(InvalidUserInfoType invalidUserInfoType) {
        Toast.makeText(this, invalidUserInfoType.getMessage(), Toast.LENGTH_SHORT).show();
    }
}