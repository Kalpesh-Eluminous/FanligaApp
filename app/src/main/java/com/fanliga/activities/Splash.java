package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.fanliga.R;
import com.fanliga.databinding.ActivitySplashBinding;
import com.fanliga.utils.AppBaseActivity;

import java.util.Locale;

public class Splash extends AppBaseActivity {

    Context context;
    ActivitySplashBinding splashBinding;

    private int SPLASH_TIME_OUT = 3000;

    private String languageCodeGerman = "de";
    private String languageCodeEnglish = "en";

    String device_default_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        context = Splash.this;
        loadLocalVariables(context);

        device_default_lang = Locale.getDefault().getLanguage();
        Log.e("DEFAULT_LOCALE ", "" + device_default_lang);

        // always set app default language to german while sending APK to client
        // universalCode.setLocale(context, languageCodeGerman);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(context, MainActivity.class)); // Login
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}