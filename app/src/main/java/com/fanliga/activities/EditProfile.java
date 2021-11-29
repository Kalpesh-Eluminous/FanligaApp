package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityEditProfileBinding;
import com.fanliga.models.ProfileData;
import com.fanliga.models.Sports;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called_Token;
import volley.WS_Upload_File;

public class EditProfile extends AppBaseActivity implements ActivityCallbackInterface {

    ActivityEditProfileBinding activityEditProfileBinding;
    Context context;
    ActivityCallbackInterface activityCallbackInterface;
    String imageFilePath = null;
    String imageFileName = null;
    ProfileData profileData;
    private final int updateProfile = 1;
    String image_changed = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        context = EditProfile.this;
        activityCallbackInterface = EditProfile.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profileData = (ProfileData) getIntent().getSerializableExtra(AppConstant.PROFILE_DATA);
        }

        activityEditProfileBinding.etName.setText(profileData.getFirst_name() +" "+ profileData.getLast_name());
        if(profileData.getAge() != null && !profileData.getAge().isEmpty() && !profileData.getAge().equals("null")) {
            activityEditProfileBinding.etAge.setText(profileData.getAge());
        }else{
            activityEditProfileBinding.etAge.setText("");
        }

        if(profileData.getLocation() != null && !profileData.getLocation().isEmpty() && !profileData.getLocation().equals("null")) {
            activityEditProfileBinding.etLocation.setText(profileData.getLocation());
        }else{
            activityEditProfileBinding.etLocation.setText("");
        }

        if(profileData.getActive_sports() != null && !profileData.getActive_sports().isEmpty() ) {
            activityEditProfileBinding.etActiveSport.setText(toCSV(profileData.getActive_sports()));
        }else{
            activityEditProfileBinding.etActiveSport.setText("");
        }

        if(profileData.getHomeClub() != null ) {
            activityEditProfileBinding.etHomeClub.setText(profileData.getHomeClub().getClub_name());
        }else{
            activityEditProfileBinding.etHomeClub.setText("");
        }

        if(profileData.getProfile_image_path() != null && !profileData.getProfile_image_path().isEmpty() && !profileData.getProfile_image_path().equals("null")) {
            Glide.with(this).load(profileData.getProfile_image_path()).into(activityEditProfileBinding.ivProfile);
        }

        activityEditProfileBinding.flProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // runtime permission by using dexter library
                Dexter.withActivity(EditProfile.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    openGallery();
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {

                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();

            }
        });

        activityEditProfileBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(profileData.getProfile_image_path()!=null && !profileData.getProfile_image_path().isEmpty() && !profileData.getProfile_image_path().equals("null")) {
                    if (imageFilePath != null && !imageFilePath.isEmpty()) {
                        updateProfileMediaWS();
                    } else {
                        updateProfileWS();
                    }
//                }else{
//                    Toast.makeText(context, "Please select profile image.", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        activityEditProfileBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });


        activityEditProfileBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        activityEditProfileBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        activityEditProfileBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        activityEditProfileBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ModeratorList.class));
                }
            }
        });
    }

    public void openGallery() {

        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        startActivityForResult(pickIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {


                Uri selectedMediaUri = data.getData();
                // your codefor single image selection
                //handle image
                imageFilePath = getPath(selectedMediaUri);
                imageFileName = "default_file_name";
                Cursor returnCursor =
                        getContentResolver().query(data.getData(), null, null, null, null);
                try {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    imageFileName = returnCursor.getString(nameIndex);
                    Log.e("TAG", "file name : " + imageFileName);
                    Log.e("TAG", "file path : " + imageFilePath);

                } catch (Exception e) {
                    Log.e("TAG", "error: ", e);
                    //handle the failure cases here
                } finally {
                    returnCursor.close();
                }

             image_changed = "1";
            Glide.with(this).load(selectedMediaUri).into(activityEditProfileBinding.ivProfile);

        }


    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            int currentApiVersion = Build.VERSION.SDK_INT;
            //TODO changes to solve gallery video issue
            if (currentApiVersion > Build.VERSION_CODES.M && uri.toString().contains("com.puregyngeneric.provider")) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (cursor.getString(column_index) != null) {
                        String state = Environment.getExternalStorageState();
                        File file;
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", cursor.getString(column_index));
                        } else {
                            file = new File(context.getFilesDir(), cursor.getString(column_index));
                        }
                        return file.getAbsolutePath();
                    }
                    return "";
                }
            } else {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    if (cursor.getString(column_index) != null) {
                        return cursor.getString(column_index);
                    }
                    return "";
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void updateProfileMediaWS(){

        if(TextUtils.isEmpty(activityEditProfileBinding.etAge.getText().toString().trim())){
            Toast.makeText(context, "Please enter age.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(activityEditProfileBinding.etLocation.getText().toString().trim())){
            Toast.makeText(context, "Please enter location.", Toast.LENGTH_SHORT).show();
            return;
        }
        String wsUrl = AppConstant.UPDATE_PROFILE;
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("email", profileData.getUser_email());
        params.put("image_changed", image_changed);
        params.put("location", activityEditProfileBinding.etLocation.getText().toString().trim());
        params.put("age", activityEditProfileBinding.etAge.getText().toString().trim());
        //params.put("active_sport_id", toCSV_ID(profileData.getActive_sports()));
        if(profileData.getHomeClub()!= null  && !profileData.getHomeClub().equals("null")) {
            params.put("home_club_id", profileData.getHomeClub().getClub_id());
        }else{
            params.put("home_club_id","");
        }

        // POST Parameters:

        new WS_Upload_File(context, wsUrl, new File(imageFilePath), updateProfile, AppConstant.POST, params).callUploadFileWS(activityCallbackInterface);


    }

    private void updateProfileWS(){
        if(TextUtils.isEmpty(activityEditProfileBinding.etAge.getText().toString().trim())){
            Toast.makeText(context, "Please enter age.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(activityEditProfileBinding.etLocation.getText().toString().trim())){
            Toast.makeText(context, "Please enter location.", Toast.LENGTH_SHORT).show();
            return;
        }
        String wsUrl = AppConstant.UPDATE_PROFILE;
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("email", profileData.getUser_email());
        params.put("profile_image", profileData.getProfile_image_path());
        params.put("image_changed", image_changed);
        params.put("location", activityEditProfileBinding.etLocation.getText().toString().trim());
        params.put("age", activityEditProfileBinding.etAge.getText().toString().trim());
//        params.put("active_sport_id", toCSV_ID(profileData.getActive_sports()));
        if(profileData.getHomeClub()!= null  && !profileData.getHomeClub().equals("null")) {
            params.put("home_club_id", profileData.getHomeClub().getClub_id());
        }else{
            params.put("home_club_id","");
        }

        // POST Parameters:

        new WS_Called_Token(context, wsUrl, params, updateProfile, AppConstant.POST).callWS(activityCallbackInterface);


    }


    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case updateProfile:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz", "Profile Data" + jsonObject.toString());
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    String msg="";
                    JSONArray jsonArray = jsonObject.getJSONArray("errors");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        try {
                           JSONObject errorObject = jsonArray.getJSONObject(i);
                            msg = msg + errorObject.getString("error") + System.getProperty("line.separator");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case updateProfile:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static String toCSV(ArrayList<Sports> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Sports s : array) {
                sb.append(s.getSport_name()).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public static String toCSV_ID(ArrayList<Sports> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Sports s : array) {
                sb.append(s.getId()).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }


}