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
import com.fanliga.databinding.ActivityLoginBindingImpl;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;

public class Login extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityLoginBindingImpl loginBinding;
    ActivityCallbackInterface activityCallbackInterface;

    private final int getOtp = 1;

    String userId, fName, lName, email, role, apiAccessToken, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        context = Login.this;
        activityCallbackInterface = Login.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        // temp data setting for quick login
        /*loginBinding.etFName.setText("Abhilash");
        loginBinding.etLName.setText("Harsole");
        loginBinding.etMobile.setText("7028554777");*/

        // on click listeners
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginAPI();
            }
        });

        loginBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Registration.class));
            }
        });
    }

    private void callLoginAPI() {
        if (loginBinding.etFName.getText().toString().trim().isEmpty()
                && loginBinding.etLName.getText().toString().trim().isEmpty()
                && loginBinding.etMobile.getText().toString().trim().isEmpty()) {
            loginBinding.tvFNameError.setVisibility(View.VISIBLE);
            loginBinding.tvLNameError.setVisibility(View.VISIBLE);
            loginBinding.tvMobileNameError.setVisibility(View.VISIBLE);
        } else if (loginBinding.etFName.getText().toString().trim().isEmpty()) {
            loginBinding.tvFNameError.setVisibility(View.VISIBLE);
            loginBinding.tvLNameError.setVisibility(View.GONE);
            loginBinding.tvMobileNameError.setVisibility(View.GONE);
        } else if (loginBinding.etLName.getText().toString().trim().isEmpty()) {
            loginBinding.tvFNameError.setVisibility(View.GONE);
            loginBinding.tvLNameError.setVisibility(View.VISIBLE);
            loginBinding.tvMobileNameError.setVisibility(View.GONE);
        } else if (loginBinding.etMobile.getText().toString().trim().isEmpty()) {
            loginBinding.tvFNameError.setVisibility(View.GONE);
            loginBinding.tvLNameError.setVisibility(View.GONE);
            loginBinding.tvMobileNameError.setVisibility(View.VISIBLE);
        } else {
            loginBinding.tvFNameError.setVisibility(View.GONE);
            loginBinding.tvLNameError.setVisibility(View.GONE);
            loginBinding.tvMobileNameError.setVisibility(View.GONE);

            // call login API method here below
            loginWS();
        }
    }

    // login user api method
    public void loginWS() {
        String wsUrl = AppConstant.LOGIN;
        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", loginBinding.etFName.getText().toString().trim());
        params.put("last_name", loginBinding.etLName.getText().toString().trim());
        params.put("mobile_no", loginBinding.etMobile.getText().toString().trim());
        // POST Parameters:
        new WS_Called(context, wsUrl, params, getOtp, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getOtp:
                if (jsonObject.getString("status").equals("true")) {
                    Toast.makeText(context, "OTP Sent On Mobile Number", Toast.LENGTH_SHORT).show();
                    userId = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("id");
                    fName = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("first_name");
                    lName = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("last_name");
                    email = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("email");
                    role = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("role");
                    apiAccessToken = jsonObject.getJSONObject("data").getJSONObject("user_data").getString("api_access_token");
                    otp = jsonObject.getJSONObject("data").getString("otp");

                    sessionData.setObjectAsString(AppConstant.USER_ID, userId);
                    sessionData.setObjectAsString(AppConstant.F_NAME, fName);
                    sessionData.setObjectAsString(AppConstant.L_NAME, lName);
                    sessionData.setObjectAsString(AppConstant.EMAIL, email);
                    sessionData.setObjectAsString(AppConstant.USER_ROLE, role);
                    sessionData.setObjectAsString(AppConstant.API_ACCESS_TOKEN, apiAccessToken);

                    startActivity(new Intent(context, OtpVerify.class)
                            .putExtra("mobile", loginBinding.etMobile.getText().toString().trim())
                            .putExtra("otp", otp));
                    finish();
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case getOtp:
                Toast.makeText(context, "Error! Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}