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
import com.fanliga.databinding.ActivitySportsListBinding;
import com.fanliga.databinding.ItemRowCategoriesBinding;
import com.fanliga.databinding.ItemRowSportsBinding;
import com.fanliga.models.Categories;
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

public class SportsList extends AppBaseActivity implements ActivityCallbackInterface {

    ActivitySportsListBinding activitySportsListBinding;
    Context context;
    private final int getSports = 1,addFavSport = 2;
    ActivityCallbackInterface activityCallbackInterface;
    SportsAdapter sportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySportsListBinding = DataBindingUtil.setContentView(this,R.layout.activity_sports_list);
        context = SportsList.this;
        activityCallbackInterface = SportsList.this;

        loadLocalVariables(context);
        init();

    }

    private void init() {

        getSportsListWS();

        // on click listeners
        activitySportsListBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });

        activitySportsListBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });

       // activitySportsListBinding.ivSports.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));

        activitySportsListBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        activitySportsListBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        activitySportsListBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        activitySportsListBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
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

    public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {
        private ArrayList<Sports> sportsArrayList;
        private Context context;
        ItemRowSportsBinding itemRowSportsBinding;

        public SportsAdapter(Context context, ArrayList<Sports> sportsArrayList) {
            this.sportsArrayList = sportsArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowSportsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_sports, parent, false);

            return new ViewHolder(itemRowSportsBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Sports sports = sportsArrayList.get(position);

            itemRowSportsBinding.tvSports.setText(sports.getSport_name());

            if(sports.getIs_bookmark().equals("1")){
                holder.itemRowSportsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_start_fill_blue);
            }else {
                holder.itemRowSportsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_star_border_blue);
            }
            holder.itemRowSportsBinding.rlSportsRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, LeaguesList.class).putExtra(AppConstant.SPORT_ID,sports.getId()));
                }
            });

            holder.itemRowSportsBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sports.getIs_bookmark().equals("0")){
                        sports.setIs_bookmark("1");
                        Log.e("Shahbaz","Bookmarked");
                        holder.itemRowSportsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_start_fill_blue);
                        addFavouriteSportWS(sports.getId());
                    }else{
                        sports.setIs_bookmark("0");
                        Log.e("Shahbaz","Cleared");
                        holder.itemRowSportsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_star_border_blue);
                        addFavouriteSportWS(sports.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return sportsArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowSportsBinding itemRowSportsBinding;

            public ViewHolder(ItemRowSportsBinding itemRowSportsBinding) {
                super(itemRowSportsBinding.getRoot());
                this.itemRowSportsBinding = itemRowSportsBinding;
            }
        }
    }


    private void getSportsListWS(){
        String wsUrl = AppConstant.GET_SPORTS;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));

        new WS_Called(context, wsUrl,params, getSports, AppConstant.POST).callWS(activityCallbackInterface);
    }


    private void addFavouriteSportWS(String sportId){
        String wsUrl = AppConstant.ADD_FAVOURITE_SPORT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("sport_id", sportId);

        new WS_Called_Token(context, wsUrl,params, addFavSport, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getSports:
                if (jsonObject.getString("status").equals("true")) {

                    JSONArray sportsArray = jsonObject.getJSONArray("data");
                    ArrayList<Sports> sportsArrayList = new ArrayList<>();
                    for (int i = 0; i <sportsArray.length() ; i++) {
                        JSONObject sportsObject = sportsArray.getJSONObject(i);
                        Sports sports = new Sports();

                        sports.setId(sportsObject.getString("id"));
                        sports.setSport_name(sportsObject.getString("sport_name"));
                        sports.setIs_bookmark(sportsObject.getString("is_bookmark"));

                        sportsArrayList.add(sports);

                    }

                    if(sportsArrayList.size()>0) {
                        activitySportsListBinding.rvSports.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        activitySportsListBinding.rvSports.setItemAnimator(new DefaultItemAnimator());
                        sportsAdapter = new SportsAdapter(context, sportsArrayList);
                        activitySportsListBinding.rvSports.setAdapter(sportsAdapter);
                    }

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

            case addFavSport:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz","Bookmark Success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getSportsListWS();
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
            case getSports:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
            case addFavSport:
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