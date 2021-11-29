package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanliga.R;
import com.fanliga.databinding.ActivityViewAdsWebViewBinding;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

public class ViewAdsWebView  extends AppBaseActivity {

    ActivityViewAdsWebViewBinding activityViewAdsWebViewBinding;
    Context context;
    String adsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewAdsWebViewBinding = DataBindingUtil.setContentView(this,R.layout.activity_view_ads_web_view);

        context = ViewAdsWebView.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            adsUrl = bundle.getString(AppConstant.ADS_URL, "0");
        }

        Log.e("ads url ", "" + adsUrl);


        activityViewAdsWebViewBinding.adsViewWebView.postDelayed(new Runnable() {

            @Override
            public void run() {
                activityViewAdsWebViewBinding.adsViewWebView.setWebViewClient(new MyWebViewClient());
                activityViewAdsWebViewBinding.adsViewWebView.setWebChromeClient(new WebChromeClient() );
                activityViewAdsWebViewBinding.adsViewWebView.getSettings().setJavaScriptEnabled(true);
                activityViewAdsWebViewBinding.adsViewWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                activityViewAdsWebViewBinding.adsViewWebView.getSettings().setBuiltInZoomControls(true);
                activityViewAdsWebViewBinding.adsViewWebView.getSettings().setSupportZoom(true);
                activityViewAdsWebViewBinding.adsViewWebView.getSettings().setUseWideViewPort(true);
                activityViewAdsWebViewBinding.adsViewWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                activityViewAdsWebViewBinding.adsViewWebView.setScrollbarFadingEnabled(false);
                activityViewAdsWebViewBinding.adsViewWebView.setInitialScale(30);
                activityViewAdsWebViewBinding.adsViewWebView.loadUrl(adsUrl);
            }
        }, 200);

        // on click listener
        activityViewAdsWebViewBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (view.getTitle().equals(""))
                view.reload();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}