package com.fanliga.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

//import androidx.appcompat.app.AppCompatActivity;

public class AppBaseActivity extends AppCompatActivity {

    public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.fanliga.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
    private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();
    public static final IntentFilter INTENT_FILTER = createIntentFilter();
    public Context context;
    public SessionData sessionData;
    public UniversalCode universalCode;
    public Activity activity;

    public void loadLocalVariables(Context context) {
        this.context = context;
        sessionData = new SessionData(context);
        universalCode = new UniversalCode(context);
        /*registerBaseActivityReceiver();
        unRegisterBaseActivityReceiver();*/
        activity = (Activity) context;
    }

    private static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
        return filter;
    }

    protected void registerBaseActivityReceiver() {
        registerReceiver(baseActivityReceiver, INTENT_FILTER);
    }

    protected void unRegisterBaseActivityReceiver() {
        unregisterReceiver(baseActivityReceiver);
    }

    public class BaseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION)) {
                finish();
            }
        }
    }

    protected void closeAllActivities() {
        sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
    }

}
