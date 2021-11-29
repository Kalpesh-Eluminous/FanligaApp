package com.fanliga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityMainBinding;
import com.fanliga.databinding.LatestVideoRowItemBinding;
import com.fanliga.databinding.MostCommentedRowItemBinding;
import com.fanliga.databinding.MostLikedRowItemBinding;
import com.fanliga.models.Clubs;
import com.fanliga.models.Home;
import com.fanliga.models.LatestAdded;
import com.fanliga.models.MostCommented;
import com.fanliga.models.MostLiked;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.EqualSpacingItemDecoration;
import com.fanliga.utils.GridRowView;
import com.fanliga.utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityMainBinding mainBinding;
    ActivityCallbackInterface activityCallbackInterface;

    private final int getHomeScreenData = 1;

    MostCommentedVideosAdapter mostCommentedVideosAdapter;
    ArrayList<MostCommented> mostCommentedArrayList;

    MostLikedVideosAdapter mostLikedVideosAdapter;
    ArrayList<MostLiked> likedVideoArrayList;

    LatestVideosAdapter latestVideosAdapter;
    ArrayList<LatestAdded> latestVideoArrayList;

    String adsUrl1, adsUrl2;

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        context = MainActivity.this;
        activityCallbackInterface = MainActivity.this;
        loadLocalVariables(context);

        init();
    }

    private void init() {

        // login access token printed on console log
        Log.e("ACCESS_TOKEN ", "" + sessionData.getObjectAsString(AppConstant.API_ACCESS_TOKEN));

        if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
            mainBinding.llLoginStatus.setVisibility(View.VISIBLE);
            mainBinding.llLoggedinUser.setVisibility(View.GONE);
        } else {
            mainBinding.llLoginStatus.setVisibility(View.GONE);
            mainBinding.llLoggedinUser.setVisibility(View.VISIBLE);
            mainBinding.tvUserFullname.setText(sessionData.getObjectAsString(AppConstant.F_NAME) + " " + sessionData.getObjectAsString(AppConstant.L_NAME));
        }

        // on click listeners
        mainBinding.llAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AppMenu.class));
            }
        });

        mainBinding.llLoginStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
            }
        });

        mainBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mainBinding.ivFavourites.setColorFilter(R.color.colorPrimaryDark);
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {

//                            None
//                            Favourites
//                            Sport
//                            Leagues
//                            Teams
//                            Moderators
//                            AppConstant.MenuSeleted="Favourites";

                              startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });

        mainBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        mainBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        mainBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        mainBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ModeratorList.class));
                }
            }
        });

        mainBinding.llAddBannerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewAdsWebView.class).putExtra(AppConstant.ADS_URL, adsUrl1));
            }
        });

        mainBinding.llAddBannerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewAdsWebView.class).putExtra(AppConstant.ADS_URL, adsUrl2));
            }
        });


        // call get home screen data API here
        homeScreenDataWS();
       // checkMenu();
       // Log.e("Debug",AppConstant.MenuSeleted);
    }

    public class MostCommentedVideosAdapter extends RecyclerView.Adapter<MostCommentedVideosAdapter.ViewHolder> {
        private ArrayList<MostCommented> mostCommenteds;
        private Context context;
        MostCommentedRowItemBinding mostCommentedRowItemBinding;

        public MostCommentedVideosAdapter(Context context, ArrayList<MostCommented> homeList) {
            this.mostCommenteds = homeList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mostCommentedRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.most_commented_row_item, parent, false);

            return new ViewHolder(mostCommentedRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final MostCommented mostCommented = mostCommenteds.get(position);
            // AppUtils.loadCircularImage(context,holder.admirerListBinding.ivProfile,admirer.getProfileImage());

            Glide.with(context).load(mostCommented.getMostCommentedVideoThumbnail()).into(mostCommentedRowItemBinding.ivMostCommentedVideoThumbnail);

            holder.mostCommentedRowItemBinding.ivMostCommentedVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, mostCommented.getMostCommentedVideoId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VideoDetails.class).putExtra("video_id", mostCommented.getMostCommentedVideoId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mostCommenteds.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public MostCommentedRowItemBinding mostCommentedRowItemBinding;

            public ViewHolder(MostCommentedRowItemBinding mostCommentedRowItemBinding) {
                super(mostCommentedRowItemBinding.getRoot());
                this.mostCommentedRowItemBinding = mostCommentedRowItemBinding;
            }
        }
    }

    public class MostLikedVideosAdapter extends RecyclerView.Adapter<MostLikedVideosAdapter.ViewHolder> {
        private ArrayList<MostLiked> homeList;
        private Context context;
        MostLikedRowItemBinding mostLikedRowItemBinding;

        public MostLikedVideosAdapter(Context context, ArrayList<MostLiked> homeList) {
            this.homeList = homeList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mostLikedRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.most_liked_row_item, parent, false);

            return new ViewHolder(mostLikedRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final MostLiked mostLiked = homeList.get(position);
            // AppUtils.loadCircularImage(context,holder.admirerListBinding.ivProfile,admirer.getProfileImage());

            Glide.with(context).load(mostLiked.getMostLikedVideoThumbnail()).into(mostLikedRowItemBinding.ivMostLikedVideoThumbnail);

            holder.mostLikedRowItemBinding.ivMostLikedVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, mostLiked.getMostLikedVideoId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VideoDetails.class).putExtra("video_id", mostLiked.getMostLikedVideoId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return homeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public MostLikedRowItemBinding mostLikedRowItemBinding;

            public ViewHolder(MostLikedRowItemBinding mostLikedRowItemBinding) {
                super(mostLikedRowItemBinding.getRoot());
                this.mostLikedRowItemBinding = mostLikedRowItemBinding;
            }
        }
    }

    public class LatestVideosAdapter extends RecyclerView.Adapter<LatestVideosAdapter.ViewHolder> {
        private ArrayList<LatestAdded> homeList;
        private Context context;
        LatestVideoRowItemBinding latestVideoRowItemBinding;

        public LatestVideosAdapter(Context context, ArrayList<LatestAdded> homeList) {
            this.homeList = homeList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            latestVideoRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.latest_video_row_item, parent, false);

            return new ViewHolder(latestVideoRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final LatestAdded latestAdded = homeList.get(position);
            // AppUtils.loadCircularImage(context,holder.admirerListBinding.ivProfile,admirer.getProfileImage());

            Glide.with(context).load(latestAdded.getLatestVideoThumbnail()).into(latestVideoRowItemBinding.ivLatestVideoThumbnail);

            holder.latestVideoRowItemBinding.ivLatestVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, latestAdded.getLatestVideoId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VideoDetails.class).putExtra("video_id", latestAdded.getLatestVideoId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return homeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public LatestVideoRowItemBinding latestVideoRowItemBinding;

            public ViewHolder(LatestVideoRowItemBinding latestVideoRowItemBinding) {
                super(latestVideoRowItemBinding.getRoot());
                this.latestVideoRowItemBinding = latestVideoRowItemBinding;
            }
        }
    }

    // get home screen api method
    public void homeScreenDataWS() {
        String wsUrl = AppConstant.GET_HOME_DATA;
        Map<String, String> params = new HashMap<String, String>();
        params.put("screen_name", "home");
        // POST Parameters:
        new WS_Called(context, wsUrl, params, getHomeScreenData, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getHomeScreenData:
                if (jsonObject.getString("status").equals("true")) {
                    /*
                     * Most commented video array parsing done below
                     * */
                    mostCommentedArrayList = new ArrayList<>();
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray mostCommentedVideoArray = dataObject.getJSONArray("most_commented_video");
                    for (int i = 0; i < mostCommentedVideoArray.length(); i++) {
                        JSONObject mostCommentedVideoObject = mostCommentedVideoArray.getJSONObject(i);
                        MostCommented mostCommented = new MostCommented(mostCommentedVideoObject.getString("id"),
                                mostCommentedVideoObject.getString("file_path"),
                                mostCommentedVideoObject.getString("thumbnail_path"),
                                mostCommentedVideoObject.getString("tags"));
                        mostCommentedArrayList.add(mostCommented);
                    }
                    int numberOfColumns = 2;
                    mainBinding.rvMostCommented.setLayoutManager(new GridLayoutManager(context, numberOfColumns, LinearLayoutManager.HORIZONTAL, false));
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    mainBinding.rvMostCommented.addItemDecoration(itemDecoration);
                    mostCommentedVideosAdapter = new MostCommentedVideosAdapter(context, mostCommentedArrayList);
                    mainBinding.rvMostCommented.setAdapter(mostCommentedVideosAdapter);

                    JSONArray addsArray = dataObject.getJSONArray("adds");
                    for (int l = 0; l < addsArray.length(); l++) {
                        JSONObject addsObject = addsArray.getJSONObject(0);
                        Glide.with(context).load(addsObject.getString("image_path")).into(mainBinding.ivAddBannerOne);
                        adsUrl1 = addsObject.getString("url");
                    }

                    for (int k = 0; k < addsArray.length(); k++) {
                        JSONObject addsObject = addsArray.getJSONObject(1);
                        Glide.with(context).load(addsObject.getString("image_path")).into(mainBinding.ivAddBannerTwo);
                        adsUrl2 = addsObject.getString("url");
                    }

                    /*
                     * Most liked video array parsing done below
                     * */
                    likedVideoArrayList = new ArrayList<>();
                    JSONObject dataObject1 = jsonObject.getJSONObject("data");
                    JSONArray likedVideoArray = dataObject1.getJSONArray("liked_video");
                    for (int j = 0; j < likedVideoArray.length(); j++) {
                        JSONObject likedVideoObject = likedVideoArray.getJSONObject(j);
                        MostLiked mostLiked = new MostLiked(likedVideoObject.getString("id"),
                                likedVideoObject.getString("file_path"),
                                likedVideoObject.getString("thumbnail_path"),
                                likedVideoObject.getString("tags"));
                        likedVideoArrayList.add(mostLiked);
                    }
                    int numberOfColumns1 = 2;
                    mainBinding.rvMostLiked.setLayoutManager(new GridLayoutManager(context, numberOfColumns1, LinearLayoutManager.HORIZONTAL, false));
                    ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    mainBinding.rvMostLiked.addItemDecoration(itemDecoration1);
                    mostLikedVideosAdapter = new MostLikedVideosAdapter(context, likedVideoArrayList);
                    mainBinding.rvMostLiked.setAdapter(mostLikedVideosAdapter);

                    /*
                     * Latest video array parsing done below
                     * */
                    latestVideoArrayList = new ArrayList<>();
                    JSONArray latestVideoArray = dataObject.getJSONArray("latest_video");
                    for (int l = 0; l < latestVideoArray.length(); l++) {
                        JSONObject latestVideoObj = latestVideoArray.getJSONObject(l);
                        LatestAdded latestAdded = new LatestAdded(latestVideoObj.getString("id"),
                                latestVideoObj.getString("file_path"),
                                latestVideoObj.getString("thumbnail_path"),
                                latestVideoObj.getString("tags"));
                        latestVideoArrayList.add(latestAdded);
                    }
                    int numberOfColumns2 = 2;
                    mainBinding.rvLatestVideo.setLayoutManager(new GridLayoutManager(context, numberOfColumns2, LinearLayoutManager.HORIZONTAL, false));
                    ItemOffsetDecoration itemDecoration2 = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    mainBinding.rvLatestVideo.addItemDecoration(itemDecoration2);
                    latestVideosAdapter = new LatestVideosAdapter(context, latestVideoArrayList);
                    mainBinding.rvLatestVideo.setAdapter(latestVideosAdapter);
                }
                break;
        }
    }


    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case getHomeScreenData:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void checkMenu(){
        if(AppConstant.MenuSeleted.equals("none")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            Log.e("Debug","Inside None");
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Favourites")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Sport")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));


        }
        else if(AppConstant.MenuSeleted.equals("Leagues")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Teams")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Moderators")){
            mainBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            mainBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
           }
         }
}