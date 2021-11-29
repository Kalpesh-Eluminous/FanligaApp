package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityVideoDetailsBinding;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.UniversalCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;
import volley.WS_Called_Token;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoDetails extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityVideoDetailsBinding videoDetailsBinding;
    ActivityCallbackInterface activityCallbackInterface;

    private final int getVideoDetails = 1, addComment = 2,likeVideo = 3;

    String video_id, video_path,ads_url;
    int likeCount,isLike;

    SimpleExoPlayer absPlayerInternal;
    PlayerView pvMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_details);

        context = VideoDetails.this;
        activityCallbackInterface = VideoDetails.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            video_id = bundle.getString("video_id", "0");
        }

        Log.e("video_id ", "" + video_id);

        // on click listeners
        videoDetailsBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
            }
        });

        videoDetailsBinding.llAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AppMenu.class));
            }
        });

        videoDetailsBinding.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoDetailsBinding.ivPlay.setVisibility(View.GONE);
                videoDetailsBinding.ivPlayVideo.setVisibility(View.GONE);

                /*
                 * setting video to videoview below
                 * */


//
//                MediaController mediaController = new MediaController(context);
//                mediaController.setAnchorView(videoDetailsBinding.videoView);
//                Uri videoPathUri = Uri.parse(video_path);
//                //Setting MediaController and URI, then starting the videoView
//                videoDetailsBinding.videoView.setMediaController(mediaController);
//                videoDetailsBinding.videoView.setVideoURI(videoPathUri);
//                videoDetailsBinding.videoView.requestFocus();
//                videoDetailsBinding.videoView.start();
            }
        });

        videoDetailsBinding.ivPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        videoDetailsBinding.llSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    // add comment API method call here
                    addCommentWS();
                }
            }
        });

        videoDetailsBinding.llViewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ViewAllComments.class).putExtra("video_id", video_id));
                }
            }
        });

        // get video details WS method call here
        videoDetailsWS();


        videoDetailsBinding.llAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewAdsWebView.class).putExtra(AppConstant.ADS_URL, ads_url));

            }
        });

        videoDetailsBinding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    likeVideoWS();
                }

            }
        });
       // loadExpoPlayer();
    }

    // get video details api method
    public void videoDetailsWS() {
        String wsUrl = AppConstant.GET_VIDEO_DETAILS;
        Map<String, String> params = new HashMap<String, String>();

        if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
            params.put("video_id", video_id);
            params.put("screen_name", "Video Details");
        } else {
            params.put("video_id", video_id);
            params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
            params.put("screen_name", "Video Details");
        }

        // POST Parameters:
        new WS_Called(context, wsUrl, params, getVideoDetails, AppConstant.POST).callWS(activityCallbackInterface);
    }

    public void loadExpoPlayer(){
        pvMain = findViewById(R.id.expo_view); // creating player view

        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        absPlayerInternal = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef); //creating a player instance

        String userAgent = Util.getUserAgent(this, "Demo");
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this,userAgent);

        Uri uriOfContentUrl = Uri.parse(video_path);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        absPlayerInternal.prepare(mediaSource);
        absPlayerInternal.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline

        pvMain.setPlayer(absPlayerInternal);
    }
    // add comments api method
    public void addCommentWS() {

        if(TextUtils.isEmpty( videoDetailsBinding.etAddComment.getText().toString().trim())){
            Toast.makeText(context, "Please add comment.", Toast.LENGTH_SHORT).show();
            return;
        }
        String wsUrl = AppConstant.ADD_COMMENT;
        Map<String, String> params = new HashMap<String, String>();

        params.put("video_id", video_id);
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("rating", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("comment", videoDetailsBinding.etAddComment.getText().toString().trim());

        // POST Parameters:
        new WS_Called_Token(context, wsUrl, params, addComment, AppConstant.POST).callWS(activityCallbackInterface);
    }


    // add comments api method
    public void likeVideoWS() {
        String wsUrl = AppConstant.LIKE_VIDEO;
        Map<String, String> params = new HashMap<String, String>();

        params.put("video_id", video_id);
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));


        // POST Parameters:
        new WS_Called_Token(context, wsUrl, params, likeVideo, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getVideoDetails:
                if (jsonObject.getString("status").equals("true")) {
                    videoDetailsBinding.ivPlayVideo.setVisibility(View.VISIBLE);
                    videoDetailsBinding.ivPlay.setVisibility(View.VISIBLE);
                   // videoDetailsBinding.si.setVisibility(View.GONE);
                    /*
                     * setting thumbnail image below
                     * */
                    Glide.with(context).load(jsonObject.getJSONObject("data").getString("thumbnail_path")).into(videoDetailsBinding.ivPlayVideo);

                    /*
                     * Settings like and comment count below
                     * */
                    likeCount = jsonObject.getJSONObject("data").getInt("total_likes");
                    videoDetailsBinding.tvLikeCount.setText(jsonObject.getJSONObject("data").getString("total_likes"));
                    videoDetailsBinding.tvCommentCount.setText(jsonObject.getJSONObject("data").getString("total_comments"));
                    isLike = jsonObject.getJSONObject("data").getInt("isLike");


                    /*
                     * setting video path in string variable and is used in init() method above
                     * */
                    //video_path = AppConstant.VIDEO_BASE_URL + "" + jsonObject.getJSONObject("data").getString("file_path");
                    video_path = jsonObject.getJSONObject("data").getString("file_path");
                    /*
                     * Setting initial 2 comments and commenter's name below
                     * */
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    /*
                     * settins add banner below
                     * */
                    Glide.with(context).load(dataObject.getJSONObject("adds").getString("image_path")).into(videoDetailsBinding.ivAddBanner);

                    ads_url = dataObject.getJSONObject("adds").getString("url");
                    /*
                     * comments array parsing below
                     * */
                    if (dataObject.has("comments")) {
                        JSONArray commentsArray = dataObject.getJSONArray("comments");
                        for (int i = 0; i < commentsArray.length(); i++) {
                            if (commentsArray.length() == 0) {
                                videoDetailsBinding.tvFirstCommenterName.setText("Not Available!");
                                videoDetailsBinding.tvSecondCommenterName.setText("Not Available!");
                                videoDetailsBinding.tvFirstComment.setText("Not Available!");
                                videoDetailsBinding.tvSecondComment.setText("Not Available!");
                            } else if (commentsArray.length() <= 1) {
                                JSONObject commentsObject = commentsArray.getJSONObject(0);
                                videoDetailsBinding.tvFirstCommenterName.setText(commentsObject.getJSONObject("given_by").getString("first_name") + " " + commentsObject.getJSONObject("given_by").getString("last_name"));
                                videoDetailsBinding.tvFirstComment.setText(commentsObject.getString("comment"));
                            } else if (commentsArray.length() <= 2) {
                                JSONObject commentsObject = commentsArray.getJSONObject(0);
                                JSONObject commentsObject1 = commentsArray.getJSONObject(1);
                                videoDetailsBinding.tvFirstCommenterName.setText(commentsObject.getJSONObject("given_by").getString("first_name") + " " + commentsObject.getJSONObject("given_by").getString("last_name"));
                                videoDetailsBinding.tvSecondCommenterName.setText(commentsObject1.getJSONObject("given_by").getString("first_name") + " " + commentsObject1.getJSONObject("given_by").getString("last_name"));
                                videoDetailsBinding.tvFirstComment.setText(commentsObject.getString("comment"));
                                videoDetailsBinding.tvSecondComment.setText(commentsObject1.getString("comment"));
                            } else {
                                JSONObject commentsObject = commentsArray.getJSONObject(0);
                                JSONObject commentsObject1 = commentsArray.getJSONObject(1);
                                videoDetailsBinding.tvFirstCommenterName.setText(commentsObject.getJSONObject("given_by").getString("first_name") + " " + commentsObject.getJSONObject("given_by").getString("last_name"));
                                videoDetailsBinding.tvSecondCommenterName.setText(commentsObject1.getJSONObject("given_by").getString("first_name") + " " + commentsObject1.getJSONObject("given_by").getString("last_name"));
                                videoDetailsBinding.tvFirstComment.setText(commentsObject.getString("comment"));
                                videoDetailsBinding.tvSecondComment.setText(commentsObject1.getString("comment"));
                            }

                        }
                    }

                    // This is to inistlize Expo Player
                    loadExpoPlayer();
                } else {
                    Toast.makeText(context, "Error in fetching data", Toast.LENGTH_SHORT).show();
                }
                break;

            case addComment:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz","Comment Data" +jsonObject.toString());
                    videoDetailsBinding.etAddComment.setText("");
                    videoDetailsWS();

                }else {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            case likeVideo:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz","Like Data" +jsonObject.toString());
                    if(isLike==0){
                        likeCount++;
                        isLike =1;
                    }else {
                        likeCount--;
                        isLike =0;

                    }

                    videoDetailsBinding.tvLikeCount.setText( String.valueOf(likeCount));

                }else {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case getVideoDetails:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;

            case addComment:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(context, MainActivity.class));
    }
}