package com.teamRTL.cloudmedicalproject.UIs.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.teamRTL.cloudmedicalproject.R;

public class LoginWithActivity extends AppCompatActivity {
    AppCompatButton  btn_login,btn_signup;
    ImageView facebook,instagram,twitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with);
        getSupportActionBar().hide();

        //initializing button
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        facebook = findViewById(R.id.facebook);
        instagram =findViewById(R.id.instegram);
        twitter = findViewById(R.id.twitter);

        //login button forward you to login page
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this,LoginActivity.class));
            }
        });

            //signup button forward you to signup page
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this,SignUpActivity.class));
            }
        });

        //instagram button forward
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com")));
            }
        });


        //twitter button forward
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com")));
            }
        });


        //facebook button forward
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com")));
            }
        });

    }
}