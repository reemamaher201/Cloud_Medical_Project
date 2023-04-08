package com.teamRTL.cloudmedicalproject.UIs.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teamRTL.cloudmedicalproject.R;

public class LoginActivity extends AppCompatActivity {
    TextView dont_have_account,forget_password;
    AppCompatEditText emailLogin,passwordLogin;
    AppCompatButton btn_login;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        dont_have_account = findViewById(R.id.dont_have_account);
        forget_password = findViewById(R.id.forget_password);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        btn_login = findViewById(R.id.btn_login);


        //Do not have an account forward you to sign up page
        dont_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}