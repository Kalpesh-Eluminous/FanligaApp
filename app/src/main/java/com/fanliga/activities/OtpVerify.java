package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fanliga.R;
import com.fanliga.databinding.ActivityOtpVerifyBinding;
import com.fanliga.utils.AppBaseActivity;
import com.goodiebag.pinview.Pinview;

public class OtpVerify extends AppBaseActivity {

    Context context;
    ActivityOtpVerifyBinding otpVerifyBinding;

    String otp, mobileNumber, otpEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpVerifyBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verify);

        context = OtpVerify.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            otp = bundle.getString("otp", "no-name");
            mobileNumber = bundle.getString("mobile", "no-name");
        }

        Log.e("OTP ", "" + otp);
        Log.e("MOBILE_NUM ", "" + mobileNumber);

        otpVerifyBinding.otpView.setValue(otp);

        // on click listeners
        otpVerifyBinding.otpView.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //Log.e("OTP_ENTERED ", "" + pinview.getValue().toString());
                otpEntered = pinview.getValue();
                Log.e("OTP_ENTERED ", "" + otpEntered);
            }
        });

        otpVerifyBinding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
    }

    private void verifyOtp() {
        /*if (otpVerifyBinding.otpView.getValue().isEmpty()) {
            otpVerifyBinding.tvOtpError.setVisibility(View.VISIBLE);
            otpVerifyBinding.tvOtpError.setText("Please enter OTP");
        } else if (otpVerifyBinding.otpView.getValue() != otp) {
            otpVerifyBinding.tvOtpError.setVisibility(View.VISIBLE);
            otpVerifyBinding.tvOtpError.setText("Invalid OTP Entered");
        } else if (otp.equals(otpVerifyBinding.otpView.getValue())) {
            // otp match, redirect user to app dashboard
            startActivity(new Intent(context, MainActivity.class));
            finish();
        } else {
            otpVerifyBinding.tvOtpError.setVisibility(View.VISIBLE);
            otpVerifyBinding.tvOtpError.setText("OTP Mismatch");
        }*/

        if (otp.equals(otpVerifyBinding.otpView.getValue())) {
            // otp match, redirect user to app dashboard
            startActivity(new Intent(context, MainActivity.class));
            finish();
        } else {
            otpVerifyBinding.tvOtpError.setVisibility(View.VISIBLE);
            otpVerifyBinding.tvOtpError.setText("OTP Mismatch");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        sessionData.editor.clear().commit();
    }
}