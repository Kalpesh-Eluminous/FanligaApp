package com.fanliga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.fanliga.R;
import com.fanliga.databinding.ActivityClubsListBinding;
import com.fanliga.databinding.ItemRowClubsBinding;
import com.fanliga.databinding.ItemRowLeaguesBinding;
import com.fanliga.models.Clubs;
import com.fanliga.models.Leagues;
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
import volley.WS_Called;
import volley.WS_Called_Token;

public class ClubsList extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityClubsListBinding activityClubsListBinding;
    ActivityCallbackInterface activityCallbackInterface;
    private final int getClubs = 1,addFavClub = 2;
    String leagueID = "";
    ClubsAdapter clubsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityClubsListBinding = DataBindingUtil.setContentView(this,R.layout.activity_clubs_list);

        context = ClubsList.this;
        activityCallbackInterface = ClubsList.this;
        loadLocalVariables(context);
        init();
    }

    private void init(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            leagueID = bundle.getString(AppConstant.LEAGUE_ID, "0");
        }

        Log.e("league id ", "" + leagueID);
        getClubsListWS();

        // on click listeners
        activityClubsListBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });

        activityClubsListBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });


        activityClubsListBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        activityClubsListBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });


      //  activityClubsListBinding.ivTeams.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));

        activityClubsListBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        activityClubsListBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
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


    public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.ViewHolder> {
        private ArrayList<Clubs> clubsArrayList;
        private Context context;
        ItemRowClubsBinding itemRowClubsBinding;

        public ClubsAdapter(Context context, ArrayList<Clubs> clubsArrayList) {
            this.clubsArrayList = clubsArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowClubsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_clubs, parent, false);

            return new ViewHolder(itemRowClubsBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Clubs clubs = clubsArrayList.get(position);

            itemRowClubsBinding.tvClubs.setText(clubs.getName());


            if(clubs.getIs_bookmark().equals("1")){
                holder.itemRowClubsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
            }else {
                holder.itemRowClubsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
            }

            holder.itemRowClubsBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clubs.getIs_bookmark().equals("0")){
                        clubs.setIs_bookmark("1");
                        Log.e("Shahbaz","Bookmarked");
                        holder.itemRowClubsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
                        addFavouriteClubsWS(clubs.getId());
                    }else{
                        clubs.setIs_bookmark("0");
                        Log.e("Shahbaz","Cleared");
                        holder.itemRowClubsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
                        addFavouriteClubsWS(clubs.getId());
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return clubsArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowClubsBinding itemRowClubsBinding;

            public ViewHolder(ItemRowClubsBinding itemRowClubsBinding) {
                super(itemRowClubsBinding.getRoot());
                this.itemRowClubsBinding = itemRowClubsBinding;
            }
        }
    }


    private void getClubsListWS(){
        String wsUrl = AppConstant.GET_CLUBS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        // params.put("league_id", leagueID);

        // POST Parameters:
        new WS_Called(context, wsUrl, params, getClubs, AppConstant.POST).callWS(activityCallbackInterface);
    }

    private void addFavouriteClubsWS(String teamId){
        String wsUrl = AppConstant.ADD_FAVOURITE_CLUB;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("club_id", teamId);

        new WS_Called_Token(context, wsUrl,params, addFavClub, AppConstant.POST).callWS(activityCallbackInterface);
    }
    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getClubs:
                if (jsonObject.getString("status").equals("true")) {

                    JSONArray clubsArray = jsonObject.getJSONArray("data");
                    ArrayList<Clubs> clubsArrayList = new ArrayList<>();
                    for (int i = 0; i <clubsArray.length() ; i++) {
                        JSONObject clubsObject = clubsArray.getJSONObject(i);
                        Clubs clubs = new Clubs();

                        clubs.setId(clubsObject.getString("id"));
                        clubs.setName(clubsObject.getString("club_name"));
                        clubs.setIs_bookmark(clubsObject.getString("is_bookmark"));

                        clubsArrayList.add(clubs);

                    }

                    if(clubsArrayList.size()>0) {
                        activityClubsListBinding.rvClubs.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        activityClubsListBinding.rvClubs.setItemAnimator(new DefaultItemAnimator());
                        clubsAdapter = new ClubsAdapter(context, clubsArrayList);
                        activityClubsListBinding.rvClubs.setAdapter(clubsAdapter);
                    }

                } else {
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

            case addFavClub:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz","Bookmark Success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getClubsListWS();
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
            case getClubs:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
            case addFavClub:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}