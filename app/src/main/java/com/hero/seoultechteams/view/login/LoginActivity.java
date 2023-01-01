package com.hero.seoultechteams.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityLoginBinding;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.contract.LoginContract;
import com.hero.seoultechteams.view.login.presenter.LoginPresenter;
import com.hero.seoultechteams.view.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {

    private ActivityLoginBinding binding;
    private final LoginContract.Presenter presenter = new LoginPresenter(this,
            Injector.getInstance().provideGetUserUseCase());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.btnSignUp.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                Intent intent = new Intent(this, SignUp01Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        final String email = binding.editEmail.getText().toString();
        final String pwd = binding.editPwd.getText().toString();

        presenter.firebaseLogin(email, pwd);
    }

    @Override
    public void onLogin(UserEntity userEntity) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finishAffinity();
    }

    @Override
    public void failedLogin() {
        Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidLogin(InvalidLoginInfoType invalidLoginInfoType) {
        Toast.makeText(this, invalidLoginInfoType.getMessage(), Toast.LENGTH_SHORT).show();
    }
}