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


public class SignUp01Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private MaterialButton btnNextStepSignUp;
    private TextInputEditText editEmail, editPwd, editPwdConfirm;

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

        if (checkLoginForm(email, pwd, pwdConfirm)) {
            Intent intent = new Intent(this, SignUp02Activity.class);
            intent.putExtra("email", email);
            intent.putExtra("pwd", pwd);
            startActivity(intent);
        }

    }

    private boolean checkLoginForm(String email, String pwd, String pwdConfirm) {

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkEmailValid(email)) {
            Toast.makeText(this, "이메일 양식을 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkPwdValid(pwd)) {
            Toast.makeText(this, "비밀번호는 6자리 이상입니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(pwdConfirm) || !pwd.equals(pwdConfirm)) {
            Toast.makeText(this, "입력한 패스워드와 다릅니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkPwdValid(String pwd) {
        return pwd.length() > 6;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}