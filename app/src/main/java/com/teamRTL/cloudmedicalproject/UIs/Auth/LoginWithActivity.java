package com.teamRTL.cloudmedicalproject.UIs.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityLoginWithBinding;

public class LoginWithActivity extends AppCompatActivity {
private ActivityLoginWithBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWithBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //login button forward you to login page
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this,LoginActivity.class));
            }
        });

            //signup button forward you to signup page
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this,SignUpActivity.class));
            }
        });

        //instagram button forward
        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com")));
            }
        });


        //twitter button forward
        binding.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com")));
            }
        });


        //facebook button forward
        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com")));
            }
        });

    }
}