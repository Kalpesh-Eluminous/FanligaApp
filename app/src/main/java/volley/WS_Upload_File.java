package volley;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanliga.R;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.InternetConnection;
import com.fanliga.utils.SessionData;
import com.fanliga.utils.UniversalCode;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WS_Upload_File {

    private Context context;
    private ActivityCallbackInterface activityCallbackInterface;
    private String strURL;
    private File uploadFile;
    private int resultCode;
    private Dialog noInternetConnectionDialog;
    private ProgressDialog progressDialog;
    private int methodUsed;
    private boolean showProgressBar;
    private SessionData sessionData;
    private Activity activity;
    private BroadcastReceiver mNetworkReceiver;
    private boolean isWSRunning = false;
    private ArrayList<File> uploadFilesLists;
    private boolean uploadSingleFile = true;
    Map<String, String> userParams;
    String customeId,firstName,lastName,email,mobilenumber,categoryId;

    public WS_Upload_File(Context context, String strURL, File uploadFile, int resultCode, int methodUsed, Map<String,String> userParams) {
        this.context = context;
        this.strURL = strURL;
        this.uploadFile = uploadFile;
        this.resultCode = resultCode;
        this.methodUsed = methodUsed;
        showProgressBar = true;
        showProgressDialog(context);
        sessionData = new SessionData(context);
        activity = (Activity) context;
        uploadSingleFile = true;
        this.userParams = userParams;
    }

    public WS_Upload_File(Context context, String strURL, ArrayList<File> uploadFilesLists, int resultCode, int methodUsed,  Map<String,String> userParams) {
        this.context = context;
        this.strURL = strURL;
        this.uploadFilesLists = uploadFilesLists;
        this.resultCode = resultCode;
        this.methodUsed = methodUsed;
        showProgressBar = true;
        showProgressDialog(context);
        sessionData = new SessionData(context);
        activity = (Activity) context;
        uploadSingleFile = false;
        this.userParams = userParams;
    }

    public void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if (showProgressBar) {
            progressDialog.show();
        }
    }

    public void callUploadFileWS(ActivityCallbackInterface activityCallbackInterface) {
        this.activityCallbackInterface = activityCallbackInterface;

        if (InternetConnection.checkConnection(context)) {
            new uploadAccredeationsWS().execute();
        } else {
            progressDialog.dismiss();
        }
    }


    public class uploadAccredeationsWS extends AsyncTask<Void, Void, Void> {
        private String response_str;
        private HttpEntity resEntity;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isWSRunning = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String BOUNDARY = "*****";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(strURL);
                post.addHeader(AppConstant.AUTHORIZATION, sessionData.getObjectAsString(AppConstant.API_ACCESS_TOKEN));
                post.setHeader("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDARY, Charset.defaultCharset());

                // For Multiple Uploads
                if (!uploadSingleFile) {
                    for (int i = 0; i < uploadFilesLists.size(); i++) {
                        FileBody bin1 = new FileBody(uploadFilesLists.get(i));
                        reqEntity.addPart("profile_image" + (i + 1), bin1);
                    }
                } else { // For Single Upload
                    FileBody bin1 = new FileBody(uploadFile);
                    reqEntity.addPart("profile_image", bin1);
                }

                reqEntity.addPart("id", new StringBody(userParams.get("id")));
                reqEntity.addPart("email", new StringBody(userParams.get("email")));
                reqEntity.addPart("image_changed", new StringBody(userParams.get("image_changed")));
                reqEntity.addPart("location", new StringBody(userParams.get("location")));
                reqEntity.addPart("age", new StringBody(userParams.get("age")));
                //reqEntity.addPart("active_sport_id", new StringBody(userParams.get("active_sport_id")));
                reqEntity.addPart("home_club_id", new StringBody(userParams.get("home_club_id")));



                Log.e("WS Called File :- ", uploadFile.getAbsolutePath());
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();
                response_str = EntityUtils.toString(resEntity);
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (mNetworkReceiver != null) {
                        context.unregisterReceiver(mNetworkReceiver);
                    }

                    if (resEntity != null) {
                        Log.e("File Upload URL", response_str);
                        if (response_str.contains("Invalid Token Signature") || response_str.contains("Token Expired")) {

                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response_str);
                                Log.e("JSON_OBJ ", new Gson().toJson(jsonObject));
                                activityCallbackInterface.getResultBack(jsonObject, resultCode);
                                progressDialog.dismiss();

                               /* if (jsonObject.getString("status").equals("Success")) {



                                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                    alertDialog.setTitle("Shwing");
                                    alertDialog.setMessage(jsonObject.getString("message"));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            });
                                    alertDialog.show();
                                    alertDialog.setCancelable(false);



                                } else {

                                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                    alertDialog.setTitle("Shwing");
                                    alertDialog.setMessage(jsonObject.getString("message"));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            });
                                    alertDialog.show();
                                    alertDialog.setCancelable(false);
                                }*/

                                if (noInternetConnectionDialog != null) {
                                    noInternetConnectionDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        }
    }

    public class CheckInternetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (InternetConnection.checkConnection(context) && !isWSRunning) {
                    // Call Benefits Webservice
                    showProgressDialog(context);
                    new uploadAccredeationsWS().execute();
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
}
