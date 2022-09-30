package com.hero.seoultechteams.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;

import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.contract.LoginContract;
import com.hero.seoultechteams.view.login.presenter.LoginPresenter;
import com.hero.seoultechteams.view.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {

    private MaterialButton btnLogin;
    private TextView btnSignUp;
    private TextInputEditText editEmail, editPwd;
    private final LoginContract.Presenter presenter = new LoginPresenter(this,
            Injector.getInstance().provideGetUserUseCase());


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

//    private void firebaseLogin(final String email, String pwd) {
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        getUserFromDatabase();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }





//    private void getUserFromDatabase() {
//        UserRepositoryImpl userRepository = new UserRepositoryImpl(this);
//        userRepository.getUser(new OnCompleteListener<UserData>() {
//            @Override
//            public void onComplete(boolean isSuccess, UserData data) {
//                if (isSuccess && data != null) {
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finishAffinity();
//                }
//
//            }
//        }, getCurrentUser().getUid());
//    }
}