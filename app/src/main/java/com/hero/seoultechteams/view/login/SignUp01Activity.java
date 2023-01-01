package com.hero.seoultechteams.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivitySignUp01Binding;
import com.hero.seoultechteams.view.login.contract.SignUp01Contract;
import com.hero.seoultechteams.view.login.presenter.SignUp01Presenter;


public class SignUp01Activity extends AppCompatActivity implements View.OnClickListener, SignUp01Contract.View {

    private ActivitySignUp01Binding binding;
    private final SignUp01Contract.Presenter presenter = new SignUp01Presenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUp01Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.btnNextStepSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_next_step_sign_up:
                enterNextStepSignUp();
                break;
        }
    }

    private void enterNextStepSignUp() {
        String email = binding.editEmail.getText().toString();
        String pwd = binding.editPwd.getText().toString();
        String pwdConfirm = binding.editPwdConfirm.getText().toString();

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