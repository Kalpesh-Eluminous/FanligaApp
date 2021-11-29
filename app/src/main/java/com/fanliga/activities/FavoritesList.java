package com.fanliga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityClubsListBinding;
import com.fanliga.databinding.ActivityFavoritesListBinding;
import com.fanliga.databinding.ItemRowClubsBinding;
import com.fanliga.models.Clubs;
import com.fanliga.models.FavClubs;
import com.fanliga.models.FavLeagues;
import com.fanliga.models.FavModerators;
import com.fanliga.models.FavSports;
import com.fanliga.models.FavTeams;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;
import volley.WS_Called_Token;

public class FavoritesList extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityCallbackInterface activityCallbackInterface;

    ActivityFavoritesListBinding favoritesListBinding;
    private final int getFavorites = 1;
    String leagueID = "";

    ArrayList<FavSports> sportsArrayList;
    ArrayList<FavLeagues> leaguesArraylist;
    ArrayList<FavClubs> clubsArrayList;
    ArrayList<FavModerators> moderatorsArrayList;

    FavSportsAdapter favSportsAdapter;
    FavLeaguesAdapter favLeaguesAdapter;
    FavClubsAdapter favClubsAdapter;
    FavModeratorsAdapter favModeratorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritesListBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorites_list);

        context = FavoritesList.this;
        activityCallbackInterface = FavoritesList.this;
        loadLocalVariables(context);
        init();
    }

    public void init() {

        getFavoritesListWS();

        // on click listeners
        favoritesListBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });

        favoritesListBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });

        favoritesListBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        favoritesListBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        favoritesListBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        favoritesListBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ModeratorList.class));
                }
            }
        });
       // checkMenu();
      //  favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
    }

    public class FavSportsAdapter extends BaseAdapter {
        Context context;
        List<FavSports> favSportsList;

        public FavSportsAdapter(Context context, List<FavSports> items) {
            this.context = context;
            this.favSportsList = items;
        }

        @Override
        public int getCount() {
            return favSportsList.size();
        }

        @Override
        public Object getItem(int position) {
            return favSportsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return favSportsList.indexOf(getItem(position));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fav_sports, null);
                holder = new ViewHolder();

                holder.tvSports = convertView.findViewById(R.id.tvSports);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final FavSports favSports = (FavSports) getItem(position);

            holder.tvSports.setText(favSports.getSport_name());

            return convertView;
        }

        public class ViewHolder {
            TextView tvSports;
        }
    }

    public class FavLeaguesAdapter extends BaseAdapter {
        Context context;
        List<FavLeagues> favLeaguesList;

        public FavLeaguesAdapter(Context context, List<FavLeagues> items) {
            this.context = context;
            this.favLeaguesList = items;
        }

        @Override
        public int getCount() {
            return favLeaguesList.size();
        }

        @Override
        public Object getItem(int position) {
            return favLeaguesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return favLeaguesList.indexOf(getItem(position));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fav_leagues, null);
                holder = new ViewHolder();

                holder.tvLeagues = convertView.findViewById(R.id.tvLeagues);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final FavLeagues favLeagues = (FavLeagues) getItem(position);

            holder.tvLeagues.setText(favLeagues.getLeague_name());

            return convertView;
        }

        public class ViewHolder {
            TextView tvLeagues;
        }
    }

    public class FavClubsAdapter extends BaseAdapter {
        Context context;
        List<FavClubs> favClubsList;

        public FavClubsAdapter(Context context, List<FavClubs> items) {
            this.context = context;
            this.favClubsList = items;
        }

        @Override
        public int getCount() {
            return favClubsList.size();
        }

        @Override
        public Object getItem(int position) {
            return favClubsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return favClubsList.indexOf(getItem(position));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fav_clubs, null);
                holder = new ViewHolder();

                holder.tvClubs = convertView.findViewById(R.id.tvClubs);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final FavClubs favClubs = (FavClubs) getItem(position);

            holder.tvClubs.setText(favClubs.getClub_name());

            return convertView;
        }

        public class ViewHolder {
            TextView tvClubs;
        }
    }

    public class FavModeratorsAdapter extends BaseAdapter {
        Context context;
        List<FavModerators> favModeratorsList;

        public FavModeratorsAdapter(Context context, List<FavModerators> items) {
            this.context = context;
            this.favModeratorsList = items;
        }

        @Override
        public int getCount() {
            return favModeratorsList.size();
        }

        @Override
        public Object getItem(int position) {
            return favModeratorsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return favModeratorsList.indexOf(getItem(position));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fav_moderators, null);
                holder = new ViewHolder();

                holder.tvModerators = convertView.findViewById(R.id.tvModerators);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final FavModerators favModerators = (FavModerators) getItem(position);

            holder.tvModerators.setText(favModerators.getModerator_name());

            return convertView;
        }

        public class ViewHolder {
            TextView tvModerators;
        }
    }

    private void getFavoritesListWS() {
        String wsUrl = AppConstant.GET_FAVORITES_LIST;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        // params.put("league_id", leagueID);

        // POST Parameters:
        new WS_Called_Token(context, wsUrl, params, getFavorites, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getFavorites:
                if (jsonObject.getString("status").equals("true")) {

                    sportsArrayList = new ArrayList<>();
                    leaguesArraylist = new ArrayList<>();
                    clubsArrayList = new ArrayList<>();
                    moderatorsArrayList = new ArrayList<>();

                    JSONArray dataArr = jsonObject.getJSONArray("data");
                    for (int a = 0; a < dataArr.length(); a++) {
                        JSONObject dataObj = dataArr.getJSONObject(a);

                        JSONArray sportsArr = dataObj.getJSONArray("sports");
                        for (int i = 0; i < sportsArr.length(); i++) {
                            JSONObject sportsObj = sportsArr.getJSONObject(i);
                            FavSports favSports = new FavSports(sportsObj.getString("sport_name"), sportsObj.getString("sport_id"));
                            sportsArrayList.add(favSports);
                        }
                        favSportsAdapter = new FavSportsAdapter(context, sportsArrayList);
                        favoritesListBinding.lvSports.setAdapter(favSportsAdapter);

                        JSONArray leaguesArr = dataObj.getJSONArray("leagues");
                        for (int j = 0; j < leaguesArr.length(); j++) {
                            JSONObject leaguesObj = leaguesArr.getJSONObject(j);
                            FavLeagues favLeagues = new FavLeagues(leaguesObj.getString("league_name"), leaguesObj.getString("league_id"));
                            leaguesArraylist.add(favLeagues);
                        }
                        favLeaguesAdapter = new FavLeaguesAdapter(context, leaguesArraylist);
                        favoritesListBinding.lvLeagues.setAdapter(favLeaguesAdapter);

                        JSONArray clubsArr = dataObj.getJSONArray("clubs");
                        for (int k = 0; k < clubsArr.length(); k++) {
                            JSONObject clubsObj = clubsArr.getJSONObject(k);
                            FavClubs favClubs = new FavClubs(clubsObj.getString("club_name"), clubsObj.getString("club_id"));
                            clubsArrayList.add(favClubs);
                        }
                        favClubsAdapter = new FavClubsAdapter(context, clubsArrayList);
                        favoritesListBinding.lvClubs.setAdapter(favClubsAdapter);

                        JSONArray moderatorsArr = dataObj.getJSONArray("moderators");
                        for (int l = 0; l < moderatorsArr.length(); l++) {
                            JSONObject moderatorsObj = moderatorsArr.getJSONObject(l);
                            FavModerators favModerators = new FavModerators(moderatorsObj.getString("moderator_name"), moderatorsObj.getString("moderator_id"));
                            moderatorsArrayList.add(favModerators);
                        }
                        favModeratorsAdapter = new FavModeratorsAdapter(context, moderatorsArrayList);
                        favoritesListBinding.lvModerators.setAdapter(favModeratorsAdapter);
                    }

                } else {

                    String msg = "";
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
            case getFavorites:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(context, MainActivity.class));
    }
    void checkMenu(){
        if(AppConstant.MenuSeleted.equals("none")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            Log.e("Debug","Inside None");
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Favourites")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Sport")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));


        }
        else if(AppConstant.MenuSeleted.equals("Leagues")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Teams")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

        }
        else if(AppConstant.MenuSeleted.equals("Moderators")){
            favoritesListBinding.ivFavourites.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));
            favoritesListBinding.ivModerators.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }
}