package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fanliga.R;
import com.fanliga.databinding.ActivityRegistrationBinding;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;

public class Registration extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityCallbackInterface activityCallbackInterface;
    ActivityRegistrationBinding registrationBinding;

    private final int register = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        context = Registration.this;
        activityCallbackInterface = Registration.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        // on click listners
        registrationBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterAPI();
            }
        });
    }

    private void callRegisterAPI() {
        if (registrationBinding.etFName.getText().toString().trim().isEmpty()
                && registrationBinding.etLName.getText().toString().trim().isEmpty()
                && registrationBinding.etEmail.getText().toString().trim().isEmpty()
                && registrationBinding.etMobile.getText().toString().trim().isEmpty()) {
            registrationBinding.tvFNameError.setVisibility(View.VISIBLE);
            registrationBinding.tvLNameError.setVisibility(View.VISIBLE);
            registrationBinding.tvEmailError.setVisibility(View.VISIBLE);
            registrationBinding.tvMobileNameError.setVisibility(View.VISIBLE);
        } else if (registrationBinding.etFName.getText().toString().trim().isEmpty()) {
            registrationBinding.tvFNameError.setVisibility(View.VISIBLE);
            registrationBinding.tvLNameError.setVisibility(View.GONE);
            registrationBinding.tvEmailError.setVisibility(View.GONE);
            registrationBinding.tvMobileNameError.setVisibility(View.GONE);
        } else if (registrationBinding.etLName.getText().toString().trim().isEmpty()) {
            registrationBinding.tvFNameError.setVisibility(View.GONE);
            registrationBinding.tvLNameError.setVisibility(View.VISIBLE);
            registrationBinding.tvEmailError.setVisibility(View.GONE);
            registrationBinding.tvMobileNameError.setVisibility(View.GONE);
        } else if (registrationBinding.etEmail.getText().toString().trim().isEmpty()) {
            registrationBinding.tvFNameError.setVisibility(View.GONE);
            registrationBinding.tvLNameError.setVisibility(View.GONE);
            registrationBinding.tvEmailError.setVisibility(View.VISIBLE);
            registrationBinding.tvMobileNameError.setVisibility(View.GONE);
        }
        else if (registrationBinding.etMobile.getText().toString().trim().isEmpty()) {
            registrationBinding.tvFNameError.setVisibility(View.GONE);
            registrationBinding.tvLNameError.setVisibility(View.GONE);
            registrationBinding.tvEmailError.setVisibility(View.GONE);
            registrationBinding.tvMobileNameError.setVisibility(View.VISIBLE);
        } else {
            registrationBinding.tvFNameError.setVisibility(View.GONE);
            registrationBinding.tvLNameError.setVisibility(View.GONE);
            registrationBinding.tvEmailError.setVisibility(View.GONE);
            registrationBinding.tvMobileNameError.setVisibility(View.GONE);

            // call registration API method here below
            registrationWS();
        }
    }

    // login user api method
    public void registrationWS() {
        String wsUrl = AppConstant.REGISTER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", registrationBinding.etFName.getText().toString().trim());
        params.put("last_name", registrationBinding.etLName.getText().toString().trim());
        params.put("email", registrationBinding.etEmail.getText().toString().trim());
        params.put("mobile_no", registrationBinding.etMobile.getText().toString().trim());
        // POST Parameters:
        new WS_Called(context, wsUrl, params, register, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case register:
                if (jsonObject.getString("status").equals("true")) {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, Login.class));
                    finish();
                } else if (jsonObject.getString("status").equals("false")) {
                    Toast.makeText(context, "Please Enter Valid Data For Registration", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case register:
                Toast.makeText(context, "ERROR! Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}