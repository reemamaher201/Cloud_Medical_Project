package com.teamRTL.cloudmedicalproject.UIs.Welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.teamRTL.cloudmedicalproject.Adapters.OnBoardingAdapter;
import com.teamRTL.cloudmedicalproject.Models.OnBoadringItem;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.UIs.Auth.LoginWithActivity;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager screenPager;
    OnBoardingAdapter onBoardingAdapter;
    TabLayout tabIndicator;
    TextView btnNext;
    int position = 0;
    TextView btnGetStarted;
    Animation btnAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //when this activity is about to be launch we need to check if its opened before or not

        if(restorePrefData()){
            Intent intent = new Intent(getApplicationContext(),LoginWithActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_on_boarding);

        //hide the action bar

        getSupportActionBar().hide();

        //initializing
        tabIndicator = findViewById(R.id.tab_indicator);
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted= findViewById(R.id.btn_getStarted);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        //fill list screen
        List<OnBoadringItem> bList = new ArrayList<>();
        bList.add(new OnBoadringItem("طاقم طبي متكامل", "طاقم طبي متكامل ومتخصص في الرعاية  الصحية التلطيفية للمريض والقائمين على رعايته من ذويه بهدف الحد أو التخفيف من معاناتهم في مواجهة المرض.", R.drawable.b1));
        bList.add(new OnBoadringItem("تواصل سهل و سريع", "إننا نعتمد على خبرتنا الممتازة والمتخصصة في الحفاظ على استمرارية التواصل مع المرضى وذويهم لتقديم خدمة رعاية تلطيفية سريعة في كل وقت.", R.drawable.b2));
        bList.add(new OnBoadringItem("الاهتمام و جدولة المواعيد", "إدارة كافة الخدمات المقدمة بسلاسة ودقة عالية تمنع حدوث الأخطاء بنسبة عالية جداً كما تساعد على تبسيط عناصر الرعاية الصحية لكل مريض وإدارة المواعيد الطبية لكل طبيب بعيداً عن استخدام السجلات الورقية المرهقة.", R.drawable.b3));

        //setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        onBoardingAdapter = new OnBoardingAdapter(this, bList);
        screenPager.setAdapter(onBoardingAdapter);
        screenPager.setCurrentItem(onBoardingAdapter.getCount() - 1);
        screenPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                // calculate the rotation angle based on the position of the page
                float rotation = (position < 0 ? 30f : -30f) * Math.abs(position);
                // apply the rotation to the page
                page.setRotationY(rotation);
            }
        });
        //setup tabLayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //next button clickListener

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < bList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                //when we reach to the last screen

                if (position == bList.size()-1) {

                    //TODO : show the GetStarted button and hide the indicator and the next button

                    loadLastScreen();

                }
            }
        });

        //tabLayout add change listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == bList.size()-1){
                    loadLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // get started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open login with Activity

                Intent logIntent = new Intent(getApplicationContext(), LoginWithActivity.class);
                startActivity(logIntent);

                /*
                    also we need to save a boolean value to storage so next time when the user run the app
                    we could know that he is already checked the login screen activity
                    we use the shared preferences to that process
                 */

                savePrefsData();
                finish();
            }
        });


    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isLoginActivityOpened = pref.getBoolean("isLoginOpened",false);
        return isLoginActivityOpened;

    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLoginOpened",true);
        editor.commit();

    }

    //show the GetStarted button and hide the indicator and the next button
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);

        //TODO : ADD an animation the get started button

        // setup animation
        btnGetStarted.setAnimation(btnAnim);

    }
}