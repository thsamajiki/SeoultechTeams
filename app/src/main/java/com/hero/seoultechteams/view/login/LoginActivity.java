package com.hero.seoultechteams.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;

import com.hero.seoultechteams.SplashActivity;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.user.UserRepository;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.view.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private MaterialButton btnLogin;
    private TextView btnSignUp;
    private TextInputEditText editEmail, editPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void initView() {
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnLogin = findViewById(R.id.btn_login);
        editEmail = findViewById(R.id.edit_email);
        editPwd = findViewById(R.id.edit_pwd);
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
        final String email = editEmail.getText().toString();
        final String pwd = editPwd.getText().toString();

        if (!checkEmailValid(email)) {
            Toast.makeText(this, "이메일 양식을 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkPwdValid(pwd)) {
            Toast.makeText(this, "패스워드가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseLogin(email, pwd);

    }

    private void firebaseLogin(final String email, String pwd) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        getUserFromDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkPwdValid(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }

        return pwd.length() >= 6;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void getUserFromDatabase() {
        UserRepository userRepository = new UserRepository(this);
        userRepository.getUser(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                if (isSuccess && data != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finishAffinity();
                }

            }
        }, getCurrentUser().getUid());
    }
}