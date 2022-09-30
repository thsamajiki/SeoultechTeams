package com.hero.seoultechteams.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.view.login.contract.SignUp01Contract;
import com.hero.seoultechteams.view.login.presenter.SignUp01Presenter;


public class SignUp01Activity extends AppCompatActivity implements View.OnClickListener, SignUp01Contract.View {

    private ImageView ivBack;
    private MaterialButton btnNextStepSignUp;
    private TextInputEditText editEmail, editPwd, editPwdConfirm;
    private SignUp01Contract.Presenter presenter = new SignUp01Presenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up01);
        initView();
        setOnClickListener();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        btnNextStepSignUp = findViewById(R.id.btn_next_step_sign_up);
        editEmail = findViewById(R.id.edit_email);
        editPwd = findViewById(R.id.edit_pwd);
        editPwdConfirm = findViewById(R.id.edit_pwd_confirm);
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(this);
        btnNextStepSignUp.setOnClickListener(this);
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
        String email = editEmail.getText().toString();
        String pwd = editPwd.getText().toString();
        String pwdConfirm = editPwdConfirm.getText().toString();

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