package com.fanliga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityProfileBinding;
import com.fanliga.databinding.AllCommentsRowItemBinding;
import com.fanliga.databinding.ItemRowBookmarkCategoriesBinding;
import com.fanliga.databinding.ItemRowBookmarkClubsBinding;
import com.fanliga.databinding.ItemRowBookmarkLeaguesBinding;
import com.fanliga.databinding.ItemRowBookmarkModeratorsBinding;
import com.fanliga.models.AllComments;
import com.fanliga.models.Categories;
import com.fanliga.models.Clubs;
import com.fanliga.models.HomeClub;
import com.fanliga.models.Leagues;
import com.fanliga.models.Moderators;
import com.fanliga.models.ProfileData;
import com.fanliga.models.Sports;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called_Token;

public class ProfileActivity extends AppBaseActivity implements ActivityCallbackInterface {
    Context context;
    ActivityCallbackInterface activityCallbackInterface;
    ActivityProfileBinding activityProfileBinding;

    private final int getProfileDetails = 1;
    ProfileData profileData;
    BookmarkLeaguesAdapter bookmarkLeaguesAdapter;
    BookmarkCategoriesAdapter bookmarkCategoriesAdapter;
    BookmarkClubsAdapter bookmarkClubsAdapter;
    BookmarkModeratorAdapter bookmarkModeratorAdapter;
    String ads_url,ads_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        context = ProfileActivity.this;
        loadLocalVariables(context);
        activityCallbackInterface = ProfileActivity.this;
        init();
    }

    private void init(){

        activityProfileBinding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class).putExtra(AppConstant.PROFILE_DATA,profileData));
            }
        });

        activityProfileBinding.llPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class).putExtra(AppConstant.PROFILE_DATA,profileData));
            }
        });

        activityProfileBinding.llAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class).putExtra(AppConstant.PROFILE_DATA,profileData));
            }
        });


        activityProfileBinding.llAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewAdsWebView.class).putExtra(AppConstant.ADS_URL, ads_url));

            }
        });
        // get video details WS method call here
        profileDetailsWS();
    }


    public class BookmarkCategoriesAdapter extends RecyclerView.Adapter<BookmarkCategoriesAdapter.ViewHolder> {
        private ArrayList<Categories> categoriesArrayList;
        private Context context;
        ItemRowBookmarkCategoriesBinding itemRowBookmarkCategoriesBinding;

        public BookmarkCategoriesAdapter(Context context, ArrayList<Categories> categoriesArrayList) {
            this.categoriesArrayList = categoriesArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowBookmarkCategoriesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_bookmark_categories, parent, false);

            return new ViewHolder(itemRowBookmarkCategoriesBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Categories categories = categoriesArrayList.get(position);

            itemRowBookmarkCategoriesBinding.tvCategories.setText(categories.getName());
        }

        @Override
        public int getItemCount() {
            return categoriesArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowBookmarkCategoriesBinding itemRowBookmarkCategoriesBinding;

            public ViewHolder(ItemRowBookmarkCategoriesBinding itemRowBookmarkCategoriesBinding) {
                super(itemRowBookmarkCategoriesBinding.getRoot());
                this.itemRowBookmarkCategoriesBinding = itemRowBookmarkCategoriesBinding;
            }
        }
    }

    public class BookmarkLeaguesAdapter extends RecyclerView.Adapter<BookmarkLeaguesAdapter.ViewHolder> {
        private ArrayList<Leagues> leaguesArrayList;
        private Context context;
        ItemRowBookmarkLeaguesBinding itemRowBookmarkLeaguesBinding;

        public BookmarkLeaguesAdapter(Context context, ArrayList<Leagues> leaguesArrayList) {
            this.leaguesArrayList = leaguesArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowBookmarkLeaguesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_bookmark_leagues, parent, false);

            return new ViewHolder(itemRowBookmarkLeaguesBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Leagues leagues = leaguesArrayList.get(position);

            itemRowBookmarkLeaguesBinding.tvLeagues.setText(leagues.getLeague_name());
        }

        @Override
        public int getItemCount() {
            return leaguesArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowBookmarkLeaguesBinding itemRowBookmarkLeaguesBinding;

            public ViewHolder(ItemRowBookmarkLeaguesBinding itemRowBookmarkLeaguesBinding) {
                super(itemRowBookmarkLeaguesBinding.getRoot());
                this.itemRowBookmarkLeaguesBinding = itemRowBookmarkLeaguesBinding;
            }
        }
    }


    public class BookmarkClubsAdapter extends RecyclerView.Adapter<BookmarkClubsAdapter.ViewHolder> {
        private ArrayList<Clubs> clubsArrayList;
        private Context context;
        ItemRowBookmarkClubsBinding itemRowBookmarkClubsBinding;

        public BookmarkClubsAdapter(Context context, ArrayList<Clubs> clubsArrayList) {
            this.clubsArrayList = clubsArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowBookmarkClubsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_bookmark_clubs, parent, false);

            return new ViewHolder(itemRowBookmarkClubsBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Clubs clubs = clubsArrayList.get(position);

            itemRowBookmarkClubsBinding.tvClubs.setText(clubs.getName());
        }

        @Override
        public int getItemCount() {
            return clubsArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowBookmarkClubsBinding itemRowBookmarkClubsBinding;

            public ViewHolder(ItemRowBookmarkClubsBinding itemRowBookmarkClubsBinding) {
                super(itemRowBookmarkClubsBinding.getRoot());
                this.itemRowBookmarkClubsBinding = itemRowBookmarkClubsBinding;
            }
        }
    }


    public class BookmarkModeratorAdapter extends RecyclerView.Adapter<BookmarkModeratorAdapter.ViewHolder> {
        private ArrayList<Moderators> moderatorsArrayList;
        private Context context;
        ItemRowBookmarkModeratorsBinding itemRowBookmarkModeratorsBinding;

        public BookmarkModeratorAdapter(Context context, ArrayList<Moderators> moderatorsArrayList) {
            this.moderatorsArrayList = moderatorsArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowBookmarkModeratorsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_bookmark_moderators, parent, false);

            return new ViewHolder(itemRowBookmarkModeratorsBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Moderators moderators = moderatorsArrayList.get(position);

            itemRowBookmarkModeratorsBinding.tvModerators.setText(moderators.getFirst_name() +" " +moderators.getLast_name());
        }

        @Override
        public int getItemCount() {
            return moderatorsArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowBookmarkModeratorsBinding itemRowBookmarkModeratorsBinding;

            public ViewHolder(ItemRowBookmarkModeratorsBinding itemRowBookmarkModeratorsBinding) {
                super(itemRowBookmarkModeratorsBinding.getRoot());
                this.itemRowBookmarkModeratorsBinding = itemRowBookmarkModeratorsBinding;
            }
        }
    }
    private void profileDetailsWS(){

        String wsUrl = AppConstant.GET_PROFILE_DATA;
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("screen_name", "profile");

        // POST Parameters:
        new WS_Called_Token(context, wsUrl, params, getProfileDetails, AppConstant.POST).callWS(activityCallbackInterface);

    }
    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getProfileDetails:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz", "Profile Data" + jsonObject.toString());
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONObject profileObject = dataObject.getJSONObject("profile");
                    profileData = new ProfileData();
                    profileData.setFirst_name(profileObject.getString("first_name"));
                    profileData.setLast_name(profileObject.getString("last_name"));
                    profileData.setUser_email(profileObject.getString("email"));
                    profileData.setMobile_no(profileObject.getString("mobile_no"));
                    profileData.setLocation(profileObject.getString("location"));
                    profileData.setAge(profileObject.getString("age"));
                    profileData.setHome_club_id(profileObject.getString("home_club_id"));
                    profileData.setProfile_image(profileObject.getString("profile_image"));
                    profileData.setProfile_image_path(profileObject.getString("profile_image_path"));

                    if (profileObject.has("home_club_detail")) {

                        JSONObject homeClubObject = profileObject.getJSONObject("home_club_detail");

                        HomeClub homeClub = new HomeClub();
                        homeClub.setClub_id(homeClubObject.getString("club_id"));
                        homeClub.setClub_name(homeClubObject.getString("club_name"));
                        profileData.setHomeClub(homeClub);
                    }

                    JSONArray activeSportsArray = profileObject.getJSONArray("active_sports");
                    JSONArray bookmarkLeaguesArray = profileObject.getJSONArray("bookmark_leagues");
                    JSONArray bookmarkModeratorsArray = profileObject.getJSONArray("bookmark_moderators");
                    JSONArray bookmarkCategoriesArray = profileObject.getJSONArray("bookmark_categories");
                    JSONArray bookmarkClubsArray = profileObject.getJSONArray("bookmark_clubs");

                    ArrayList<Sports> sporstArrayList = new ArrayList<>();
                    ArrayList<Leagues> bookmarkLeaguesList = new ArrayList<>();
                    ArrayList<Moderators> bookmarkModeratorsList = new ArrayList<>();
                    ArrayList<Categories> bookmarkCategoriesList = new ArrayList<>();
                    ArrayList<Clubs> bookmarkCubsList = new ArrayList<>();

                    // Populate Active Sports
                    if(activeSportsArray.length()>0){
                        for (int i = 0; i <activeSportsArray.length() ; i++) {
                            JSONObject activeSportsObject = activeSportsArray.getJSONObject(i);
                            Sports sports = new Sports();
                            sports.setId(activeSportsObject.getString("id"));
                            sports.setSport_name(activeSportsObject.getString("sport_name"));
                            sporstArrayList.add(sports);

                        }
                        activityProfileBinding.tvActiveSportClub.setText(toCSV(sporstArrayList));
                        profileData.setActive_sports(sporstArrayList);
                    }else {
                        activityProfileBinding.tvActiveSportClub.setText("No data");
                    }

                    // Populate bookmark leagues
                    if(bookmarkLeaguesArray.length()>0){
                        for (int i = 0; i <bookmarkLeaguesArray.length() ; i++) {
                            JSONObject bookmarkLeagueObject = bookmarkLeaguesArray.getJSONObject(i);
                            Leagues leagues = new Leagues();
                            leagues.setId(bookmarkLeagueObject.getString("id"));
                            leagues.setLeague_name(bookmarkLeagueObject.getString("league_name"));
                            bookmarkLeaguesList.add(leagues);

                        }

                        activityProfileBinding.rvLeagues.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                        activityProfileBinding.rvLeagues.addItemDecoration(itemDecoration);
                        bookmarkLeaguesAdapter = new BookmarkLeaguesAdapter(context, bookmarkLeaguesList);
                        activityProfileBinding.rvLeagues.setAdapter(bookmarkLeaguesAdapter);

                    }

                    // Populate bookmark_moderators
                    if(bookmarkModeratorsArray.length()>0){
                        for (int i = 0; i <bookmarkModeratorsArray.length() ; i++) {
                            JSONObject bookmarkModeratorsJSONObject = bookmarkModeratorsArray.getJSONObject(i);
                           Moderators moderators = new Moderators();
                            moderators.setId(bookmarkModeratorsJSONObject.getString("id"));
                            moderators.setFirst_name(bookmarkModeratorsJSONObject.getString("first_name"));
                            moderators.setLast_name(bookmarkModeratorsJSONObject.getString("last_name"));
                            bookmarkModeratorsList.add(moderators);

                        }

                        activityProfileBinding.rvModeration.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                        activityProfileBinding.rvModeration.addItemDecoration(itemDecoration);
                        bookmarkModeratorAdapter = new BookmarkModeratorAdapter(context, bookmarkModeratorsList);
                        activityProfileBinding.rvModeration.setAdapter(bookmarkModeratorAdapter);


                    }



                    // Populate bookmark_categories
                    if(bookmarkCategoriesArray.length()>0){
                        for (int i = 0; i <bookmarkCategoriesArray.length() ; i++) {
                            JSONObject bookmarkCategoriesJSONObject = bookmarkCategoriesArray.getJSONObject(i);
                            Categories categories = new Categories();
                            categories.setId(bookmarkCategoriesJSONObject.getString("id"));
                            categories.setName(bookmarkCategoriesJSONObject.getString("name"));

                            bookmarkCategoriesList.add(categories);

                        }

                        activityProfileBinding.rvSportCatagory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                        activityProfileBinding.rvSportCatagory.addItemDecoration(itemDecoration);
                        bookmarkCategoriesAdapter = new BookmarkCategoriesAdapter(context, bookmarkCategoriesList);
                        activityProfileBinding.rvSportCatagory.setAdapter(bookmarkCategoriesAdapter);

                    }


                    // Populate bookmark_clubs
                    if(bookmarkClubsArray.length()>0){
                        for (int i = 0; i <bookmarkClubsArray.length() ; i++) {
                            JSONObject bookmarkClubsJSONObject = bookmarkClubsArray.getJSONObject(i);
                            Clubs clubs = new Clubs();
                            clubs.setId(bookmarkClubsJSONObject.getString("id"));
                            clubs.setName(bookmarkClubsJSONObject.getString("name"));

                            bookmarkCubsList.add(clubs);

                        }

                        activityProfileBinding.rvVerien.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                        activityProfileBinding.rvVerien.addItemDecoration(itemDecoration);
                        bookmarkClubsAdapter = new BookmarkClubsAdapter(context, bookmarkCubsList);
                        activityProfileBinding.rvVerien.setAdapter(bookmarkClubsAdapter);

                    }



                    /*
                     * settins add banner below
                     * */
                    Glide.with(context).load(dataObject.getJSONObject("adds").getString("image_path")).into(activityProfileBinding.ivAddBanner);

                    ads_img = dataObject.getJSONObject("adds").getString("image_path");
                    ads_url = dataObject.getJSONObject("adds").getString("url");

                    activityProfileBinding.tvProfile.setText(profileData.getFirst_name() +" "+ profileData.getLast_name());
                    if(profileData.getAge() != null && !profileData.getAge().isEmpty() && !profileData.getAge().equals("null")) {
                        activityProfileBinding.tvAge.setText(profileData.getAge());
                    }else{
                        activityProfileBinding.tvAge.setText("no data");
                    }

                    if(profileData.getLocation() != null && !profileData.getLocation().isEmpty() && !profileData.getLocation().equals("null")) {
                        activityProfileBinding.tvPlace.setText(profileData.getLocation());
                    }else{
                        activityProfileBinding.tvPlace.setText("no data");
                    }

                    if(profileData.getProfile_image_path() != null && !profileData.getProfile_image_path().isEmpty() && !profileData.getProfile_image_path().equals("null")) {
                        Glide.with(this).load(profileData.getProfile_image_path()).into(activityProfileBinding.ivProfile);
                    }
                    if(profileData.getHomeClub()!= null  && !profileData.getHomeClub().equals("null")) {
                        activityProfileBinding.tvHomeTownClub.setText(profileData.getHomeClub().getClub_name());
                    }


                }
                break;
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case getProfileDetails:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static String toCSV(ArrayList<Sports> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Sports s : array) {
                sb.append(s.getSport_name()).append("\n");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileDetailsWS();
    }
}