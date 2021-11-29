package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanliga.R;
import com.fanliga.databinding.ActivityAppMenuBinding;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

public class AppMenu extends AppBaseActivity {

    Context context;
    ActivityAppMenuBinding appMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appMenuBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_menu);

        context = AppMenu.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
            appMenuBinding.llLogout.setVisibility(View.GONE);
        } else {
            appMenuBinding.llLogout.setVisibility(View.VISIBLE);
        }

        // on click listeners
        appMenuBinding.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ProfileActivity.class));
                }
            }
        });

        appMenuBinding.llFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });

        appMenuBinding.llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        appMenuBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // normal logout
                sessionData.editor.clear().commit();
                finishAffinity();
                Intent intent = new Intent(context, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}