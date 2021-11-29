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
import com.fanliga.databinding.ActivityCategoryListBinding;
import com.fanliga.databinding.ItemRowBookmarkCategoriesBinding;
import com.fanliga.databinding.ItemRowCategoriesBinding;
import com.fanliga.models.Categories;
import com.fanliga.utils.AppBaseActivity;
import com.fanliga.utils.AppConstant;
import com.fanliga.utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Call;
import volley.WS_Called;
import volley.WS_Called_Token;

public class CategoryList extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityCategoryListBinding activityCategoryListBinding;
    private final int getCategories = 1, addFavCategory = 2;
    ActivityCallbackInterface activityCallbackInterface;
    CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCategoryListBinding = DataBindingUtil.setContentView(this, R.layout.activity_category_list);
        context = CategoryList.this;
        loadLocalVariables(context);
        activityCallbackInterface = CategoryList.this;
        init();
    }

    private void init() {

        getCategoriesListWS();

        // on click listeners
        activityCategoryListBinding.ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, FavoritesList.class));
                }
            }
        });


        activityCategoryListBinding.ivSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, SportsList.class));
                }
            }
        });

        activityCategoryListBinding.ivLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, LeaguesList.class));
                }
            }
        });

        activityCategoryListBinding.ivTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionData.getObjectAsString(AppConstant.USER_ID).isEmpty()) {
                    universalCode.double_button_login_dialog(context, "Login to view all contents in app", "Login now", "Cancel");
                } else {
                    startActivity(new Intent(context, ClubsList.class));
                }
            }
        });

        activityCategoryListBinding.ivModerators.setOnClickListener(new View.OnClickListener() {
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

    public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
        private ArrayList<Categories> categoriesArrayList;
        private Context context;
        ItemRowCategoriesBinding itemRowCategoriesBinding;

        public CategoriesAdapter(Context context, ArrayList<Categories> categoriesArrayList) {
            this.categoriesArrayList = categoriesArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemRowCategoriesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_row_categories, parent, false);

            return new ViewHolder(itemRowCategoriesBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Categories categories = categoriesArrayList.get(position);

            itemRowCategoriesBinding.tvCategories.setText(categories.getName());


            if (categories.getIs_bookmark().equals("1")) {
                holder.itemRowCategoriesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
            } else {
                holder.itemRowCategoriesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
            }

            holder.itemRowCategoriesBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categories.getIs_bookmark().equals("0")) {
                        categories.setIs_bookmark("1");
                        Log.e("Shahbaz", "Bookmarked");
                        holder.itemRowCategoriesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill_blue);
                        addFavouriteCategoriesWS(categories.getId());
                    } else {
                        categories.setIs_bookmark("0");
                        Log.e("Shahbaz", "Cleared");
                        holder.itemRowCategoriesBinding.ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_border_blue);
                        addFavouriteCategoriesWS(categories.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoriesArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemRowCategoriesBinding itemRowCategoriesBinding;

            public ViewHolder(ItemRowCategoriesBinding itemRowCategoriesBinding) {
                super(itemRowCategoriesBinding.getRoot());
                this.itemRowCategoriesBinding = itemRowCategoriesBinding;
            }
        }
    }


    private void getCategoriesListWS() {

        String wsUrl = AppConstant.GET_CATEGORIES;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));

        new WS_Called(context, wsUrl, params, getCategories, AppConstant.POST).callWS(activityCallbackInterface);
    }


    private void addFavouriteCategoriesWS(String categoryId) {
        String wsUrl = AppConstant.ADD_FAVOURITE_CATEGORY;

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("category_id", categoryId);

        new WS_Called_Token(context, wsUrl, params, addFavCategory, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getCategories:
                if (jsonObject.getString("status").equals("true")) {

                    JSONArray categoryArray = jsonObject.getJSONArray("data");
                    ArrayList<Categories> categoriesArrayList = new ArrayList<>();
                    for (int i = 0; i < categoryArray.length(); i++) {
                        JSONObject categoryObject = categoryArray.getJSONObject(i);
                        Categories categories = new Categories();

                        categories.setId(categoryObject.getString("id"));
                        categories.setName(categoryObject.getString("category_name"));
                        categories.setIs_bookmark(categoryObject.getString("is_bookmark"));

                        categoriesArrayList.add(categories);

                    }

                    if (categoriesArrayList.size() > 0) {
                        activityCategoryListBinding.rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        activityCategoryListBinding.rvCategory.setItemAnimator(new DefaultItemAnimator());
                        categoriesAdapter = new CategoriesAdapter(context, categoriesArrayList);
                        activityCategoryListBinding.rvCategory.setAdapter(categoriesAdapter);
                    }

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

            case addFavCategory:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz", "Bookmark Success");
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getCategoriesListWS();
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
            case getCategories:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;

            case addFavCategory:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}