package com.hero.seoultechteams.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivitySignUp02Binding;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.contract.SignUp02Contract;
import com.hero.seoultechteams.view.login.presenter.SignUp02Presenter;
import com.hero.seoultechteams.view.main.MainActivity;


public class SignUp02Activity extends BaseActivity<ActivitySignUp02Binding> implements View.OnClickListener, SignUp02Contract.View {

    private final SignUp02Contract.Presenter presenter = new SignUp02Presenter(this,
            Injector.getInstance().provideSignUpUserUseCase());

    @Override
    protected ActivitySignUp02Binding getViewBinding() {
        return ActivitySignUp02Binding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.btnStart.setOnClickListener(this);
    }

    private String getEmail() {
        return getIntent().getStringExtra("email");
    }

    private String getPwd() {
        return getIntent().getStringExtra("pwd");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.btn_start) {
            finishSignUp();
        }
    }

    private void finishSignUp() {
        final String userName = binding.editUserName.getText().toString().trim();

        presenter.signUp(getEmail(), getPwd(), userName);
    }

    @Override
    public void onAddUser(UserEntity userEntity) {
        Intent intent = new Intent(SignUp02Activity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void failedUpdateProfile() {
        Toast.makeText(SignUp02Activity.this, "프로필 업데이트에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void failedSignUp() {
        Toast.makeText(SignUp02Activity.this, "회원가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidUserInfo(InvalidUserInfoType invalidUserInfoType) {
        Toast.makeText(this, "사용자명을 입력해주세요", Toast.LENGTH_SHORT).show();
    }
}