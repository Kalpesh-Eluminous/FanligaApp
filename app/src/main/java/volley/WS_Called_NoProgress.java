package volley;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.UniversalCode;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class WS_Called_NoProgress {
    private Context context;
    private ActivityCallbackInterface activityCallbackInterface;
    private String strURL;
    private Map<String, String> params;
    private UniversalCode universalCode;
    private int resultCode;
    private Dialog noInternetConnectionDialog;
    private int methodUsed;


    public WS_Called_NoProgress(Context context, String strURL, Map<String, String> params, int resultCode, int methodUsed) {
        this.context = context;
        this.strURL = strURL;
        this.params = params;
        this.resultCode = resultCode;
        this.methodUsed = methodUsed;
        universalCode = new UniversalCode(context);

    }

    public void callWS(ActivityCallbackInterface activityCallbackInterface) {
        this.activityCallbackInterface = activityCallbackInterface;

        if (universalCode.checkInternet()) {
            callingWebService(params);
        } else {
            // noInternetConnectionDialog(context);
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to make JSON object request for POST
     */
    public void callingWebService(final Map<String, String> parameters) {

        Log.e("WS Called Url :- ", strURL);
        Log.e("WS Parameters :- ", parameters.toString());

        StringRequest postRequest = new StringRequest(methodUsed, strURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("WS Response :- ", response);
                //progressDialog.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        activityCallbackInterface.getResultBack(jsonObject, resultCode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // universalCode.single_button_dialog(context, "Error-Occour", "OK");
                    Toast.makeText(context, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                activityCallbackInterface.volleyErrorMessage(error, resultCode);


                if (noInternetConnectionDialog != null) {
                    noInternetConnectionDialog.dismiss();
                }
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = parameters;
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.APP_TOKEN, AppConstant.TOKEN_VALUE);
                Log.e("WS Security :- ", params.toString());
                return params;
            }
        };

        int socketTimeout = 720000;
        //RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Method for Showing Single Button Dialog Pass Button Name & Message Body
     */
    /*public void noInternetConnectionDialog(final Context context) {

        // custom dialog
        noInternetConnectionDialog = new Dialog(context, R.style.AppTheme);
        noInternetConnectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noInternetConnectionDialog.setContentView(R.layout.no_internet_custom_dialog);
        noInternetConnectionDialog.setCancelable(false);

        final Button btnRetryConnection = noInternetConnectionDialog.findViewById(R.id.btnRetryConnection);

        btnRetryConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noInternetConnectionDialog.dismiss();

                if (universalCode.checkInternet()) {

                    callingWebService(params);
                } else {

                    noInternetConnectionDialog(context);
                }
            }
        });

        noInternetConnectionDialog.show();
    }*/
}
