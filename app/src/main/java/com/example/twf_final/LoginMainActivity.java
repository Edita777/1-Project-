package com.example.twf_final;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import at.wifi.swdev.noteapp.R;
import com.example.twf_final.view.LoginAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginMainActivity extends AppCompatActivity {
    TabLayout tabLayout;

    ViewPager viewPager;

    FloatingActionButton insta,google,tiktok;
    float v=0;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login_fragment);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.login_page);
        insta = findViewById(R.id.fab_insta);
        google = findViewById(R.id.fab_google);
        tiktok = findViewById(R.id.fab_tiktok);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        insta.setTranslationY(300);
        google.setTranslationY(300);
        tiktok.setTranslationY(300);
        tabLayout.setTranslationY(300);

        insta.setAlpha(v);
        google.setAlpha(v);
        tiktok.setAlpha(v);
        tabLayout.setAlpha(v);

        insta.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tiktok.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.instagram.com/");
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.google.com/");
            }
        });

        tiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.tiktok.com/");
            }
        });
    }

    private void openWebView(String url) {
        Intent intent = new Intent(LoginMainActivity.this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}

