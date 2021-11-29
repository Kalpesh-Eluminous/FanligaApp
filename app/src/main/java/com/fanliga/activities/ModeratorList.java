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
import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.fanliga.databinding.ActivityModeratorListBinding;
import com.fanliga.databinding.ItemRowModeratorsBinding;
import com.fanliga.databinding.ItemRowSportsBinding;
import com.fanliga.models.Categories;
import com.fanliga.models.ModeratorPojo;
import com.fanliga.models.Moderators;
import com.fanliga.models.Sports;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;
import volley.WS_Called_Token;

public class ModeratorList extends AppBaseActivity implements ActivityCallbackInterface {
    ActivityModeratorListBinding activityModeratorListBinding;
    Context context;
    private final int getModerators = 1, addFavModerators = 2;
    ActivityCallbackInterface activityCallbackInterface;
    SportsList.SportsAdapter sportsAdapter;
    ModeratorsAdapter moderatorsAdapter;
    String adsUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityModeratorListBinding = DataBindingUtil.setContentView(this, R.layout.activity_moderator_list);
        context = ModeratorList.this;
        activityCallbackInterface = ModeratorList.this;
        loadLocalVariables(context);
        init();
    }

    private void init() {

        activityModeratorListBinding.ivAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewAdsWebView.class).putExtra(AppConstant.ADS_URL, adsUrl));
            }
        });

        activityModeratorListBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });

        getModeratorsListWS();
    }


    public class ModeratorsAdapter extends RecyclerView.Adapter<ModeratorsAdapter.ViewHolder> {
        private ArrayList<ModeratorPojo> moderatorPojoArrayList;
        private Context context;
        ItemRowModeratorsBinding itemRowModeratorsBinding;

        public ModeratorsAdapter(Context context, ArrayList<ModeratorPojo> moderatorPojoArrayList) {
            this.moderatorPojoArrayList = moderatorPojoArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowModeratorsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_moderators, parent, false);

            return new ViewHolder(itemRowModeratorsBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final ModeratorPojo moderatorPojo = moderatorPojoArrayList.get(position);

            itemRowModeratorsBinding.tvName.setText(moderatorPojo.getFirst_name() + " " + moderatorPojo.getLast_name());
            if (moderatorPojo.getProfile_image() != null && !moderatorPojo.getProfile_image().isEmpty() && !moderatorPojo.getProfile_image().equals("null")) {
                Glide.with(context).load(moderatorPojo.getProfile_image()).into(itemRowModeratorsBinding.ivProfile);
            }


            if (moderatorPojo.getIs_bookmark().equals("1")) {
                holder.itemRowModeratorsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
            } else {
                holder.itemRowModeratorsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
            }

            itemRowModeratorsBinding.tvEmail.setText(moderatorPojo.getEmail());

            holder.itemRowModeratorsBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moderatorPojo.getIs_bookmark().equals("0")) {
                        moderatorPojo.setIs_bookmark("1");
                        Log.e("Shahbaz", "Bookmarked");
                        holder.itemRowModeratorsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
                        addFavouriteModeratorWS(moderatorPojo.getId());
                    } else {
                        moderatorPojo.setIs_bookmark("0");
                        Log.e("Shahbaz", "Cleared");
                        holder.itemRowModeratorsBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
                        addFavouriteModeratorWS(moderatorPojo.getId());
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return moderatorPojoArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowModeratorsBinding itemRowModeratorsBinding;

            public ViewHolder(ItemRowModeratorsBinding itemRowModeratorsBinding) {
                super(itemRowModeratorsBinding.getRoot());
                this.itemRowModeratorsBinding = itemRowModeratorsBinding;
            }
        }
    }

    private void getModeratorsListWS() {
        String wsUrl = AppConstant.GET_MODERATORS;

        Map<String, String> params = new HashMap<String, String>();

        if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
            params.put("screen_name", "Moderator List");
        } else {

            params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
            params.put("screen_name", "Moderator List");
        }

        new WS_Called(context, wsUrl, params, getModerators, AppConstant.POST).callWS(activityCallbackInterface);
    }

    private void addFavouriteModeratorWS(String moderatorId) {
        String wsUrl = AppConstant.ADD_FAVOURITE_MODERATOR;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("moderator_id", moderatorId);

        new WS_Called_Token(context, wsUrl, params, addFavModerators, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getModerators:
                if (jsonObject.getString("status").equals("true")) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray moderatorArray = dataObject.getJSONArray("moderators");
                    ArrayList<ModeratorPojo> moderatorPojoArrayList = new ArrayList<>();
                    for (int i = 0; i < moderatorArray.length(); i++) {
                        JSONObject moderatorObject = moderatorArray.getJSONObject(i);
                        ModeratorPojo moderatorPojo = new ModeratorPojo();

                        moderatorPojo.setId(moderatorObject.getString("id"));
                        moderatorPojo.setFirst_name(moderatorObject.getString("first_name"));
                        moderatorPojo.setLast_name(moderatorObject.getString("last_name"));
                        moderatorPojo.setEmail(moderatorObject.getString("email"));
                        moderatorPojo.setProfile_image(moderatorObject.getString("profile_image"));
                        // moderatorPojo.setRole(moderatorObject.getString("role"));
                        moderatorPojo.setIs_bookmark(moderatorObject.getString("is_bookmark"));


                        moderatorPojoArrayList.add(moderatorPojo);

                    }

                    if (moderatorPojoArrayList.size() > 0) {
                        activityModeratorListBinding.rvModerators.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        activityModeratorListBinding.rvModerators.setItemAnimator(new DefaultItemAnimator());
                        moderatorsAdapter = new ModeratorsAdapter(context, moderatorPojoArrayList);
                        activityModeratorListBinding.rvModerators.setAdapter(moderatorsAdapter);
                    } else {
                        Toast.makeText(context, "No data available.", Toast.LENGTH_SHORT).show();
                    }


                    JSONObject addsObject = dataObject.getJSONObject("adds");
                    Glide.with(context).load(addsObject.getString("image_path")).into(activityModeratorListBinding.ivAddBanner);
                    adsUrl = addsObject.getString("url");
                   /* JSONArray addsArray = dataObject.getJSONArray("adds");
                    for (int l = 0; l < addsArray.length(); l++) {
                        JSONObject addsObject = addsArray.getJSONObject(l);
                        Glide.with(context).load(addsObject.getString("image_path")).into(activityModeratorListBinding.ivAddBanner);
                        adsUrl = addsObject.getString("url");
                    }*/


                } else {
                   /* String msg="";
                    JSONArray jsonArray = jsonObject.getJSONArray("errors");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        try {
                            JSONObject errorObject = jsonArray.getJSONObject(i);
                            msg = msg + errorObject.getString("error") + System.getProperty("line.separator");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }*/

                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                break;

            case addFavModerators:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz", "Bookmark Success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getModeratorsListWS();
                } else {
                   /* String msg = "";
                    JSONArray jsonArray = jsonObject.getJSONArray("errors");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject errorObject = jsonArray.getJSONObject(i);
                            msg = msg + errorObject.getString("error") + System.getProperty("line.separator");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }*/

                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
        switch (resultCode) {
            case getModerators:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
            case addFavModerators:
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