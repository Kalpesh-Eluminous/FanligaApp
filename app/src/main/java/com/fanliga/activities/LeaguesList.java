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
import com.fanliga.databinding.ActivityLeaguesListBinding;
import com.fanliga.databinding.ItemRowLeaguesBinding;
import com.fanliga.databinding.ItemRowSportsBinding;
import com.fanliga.models.Leagues;
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
import volley.WS_Call;
import volley.WS_Called;
import volley.WS_Called_Token;

public class LeaguesList extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityLeaguesListBinding activityLeaguesListBinding;
    ActivityCallbackInterface activityCallbackInterface;
    private final int getLeagues = 1,addFavLeague = 2;
    String sportsID = "";
    LeaguesAdapter leaguesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLeaguesListBinding = DataBindingUtil.setContentView(this,R.layout.activity_leagues_list);
        context = LeaguesList.this;
        activityCallbackInterface = LeaguesList.this;

        loadLocalVariables(context);
        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sportsID = bundle.getString(AppConstant.SPORT_ID, "0");
        }

        Log.e("sportsID ", "" + sportsID);
        getLeaguesListWS();

        // on click listeners
        activityLeaguesListBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });

        activityLeaguesListBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });


        activityLeaguesListBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });
      //  activityLeaguesListBinding.ivLeagues.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));

        activityLeaguesListBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        activityLeaguesListBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        activityLeaguesListBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
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


    public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.ViewHolder> {
        private ArrayList<Leagues> leaguesArrayList;
        private Context context;
        ItemRowLeaguesBinding itemRowLeaguesBinding;

        public LeaguesAdapter(Context context, ArrayList<Leagues> leaguesArrayList) {
            this.leaguesArrayList = leaguesArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowLeaguesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_leagues, parent, false);

            return new ViewHolder(itemRowLeaguesBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Leagues leagues = leaguesArrayList.get(position);

            itemRowLeaguesBinding.tvLeagues.setText(leagues.getLeague_name());

            itemRowLeaguesBinding.tvLeagues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    sport_id:9
//                    league_id:14
//                    user_id:1
                    Intent intent=new Intent(LeaguesList.this,VideoGridList.class);
                    intent.putExtra(AppConstant.LEAGUE_ID,leagues.getId());
                    intent.putExtra(AppConstant.USER_ID,sessionData.getObjectAsString(AppConstant.USER_ID));
                    startActivity(intent);

                }
            });

            if(leagues.getIs_bookmark().equals("1")){
                holder.itemRowLeaguesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
            }else {
                holder.itemRowLeaguesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
            }

            itemRowLeaguesBinding.rlLeaguessRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, ClubsList.class).putExtra(AppConstant.LEAGUE_ID,leagues.getId()));
                }
            });


            holder.itemRowLeaguesBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(leagues.getIs_bookmark().equals("0")){
                        leagues.setIs_bookmark("1");
                        Log.e("Shahbaz","Bookmarked");
                        holder.itemRowLeaguesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
                        addFavouriteLeaguesWS(leagues.getId());
                    }else{
                        leagues.setIs_bookmark("0");
                        Log.e("Shahbaz","Cleared");
                        holder.itemRowLeaguesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
                        addFavouriteLeaguesWS(leagues.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return leaguesArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowLeaguesBinding itemRowLeaguesBinding;

            public ViewHolder(ItemRowLeaguesBinding itemRowLeaguesBinding) {
                super(itemRowLeaguesBinding.getRoot());
                this.itemRowLeaguesBinding = itemRowLeaguesBinding;
            }
        }
    }


    private void getLeaguesListWS(){
        String wsUrl = AppConstant.GET_LEAGUES;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        // params.put("sport_id", sportsID);

        // POST Parameters:
        new WS_Called(context, wsUrl, params, getLeagues, AppConstant.POST).callWS(activityCallbackInterface);
    }

    private void addFavouriteLeaguesWS(String leagueId) {
        String wsUrl = AppConstant.ADD_FAVOURITE_LEAGUE;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("league_id", leagueId);

        new WS_Called_Token(context, wsUrl,params, addFavLeague, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getLeagues:
                if (jsonObject.getString("status").equals("true")) {

                    JSONArray leaguesArray = jsonObject.getJSONArray("data");
                    ArrayList<Leagues> leaguesArrayList = new ArrayList<>();
                    for (int i = 0; i <leaguesArray.length() ; i++) {
                        JSONObject leaguesObject = leaguesArray.getJSONObject(i);
                        Leagues leagues = new Leagues();

                        leagues.setId(leaguesObject.getString("id"));
                        leagues.setLeague_name(leaguesObject.getString("league_name"));
                        leagues.setIs_bookmark(leaguesObject.getString("is_bookmark"));

                        leaguesArrayList.add(leagues);

                    }

                    if(leaguesArrayList.size()>0) {
                        activityLeaguesListBinding.rvLeagues.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        activityLeaguesListBinding.rvLeagues.setItemAnimator(new DefaultItemAnimator());
                        leaguesAdapter = new LeaguesAdapter(context, leaguesArrayList);
                        activityLeaguesListBinding.rvLeagues.setAdapter(leaguesAdapter);
                    }

                }
                else {
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

            case addFavLeague:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz","Bookmark Success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getLeaguesListWS();
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
            case getLeagues:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
            case addFavLeague:
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