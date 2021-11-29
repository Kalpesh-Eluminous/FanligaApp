package com.fanliga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.fanliga.databinding.ActivityViewAllCommentsBinding;
import com.fanliga.databinding.AllCommentsRowItemBinding;
import com.fanliga.databinding.MostCommentedRowItemBinding;
import com.fanliga.listeners.SwipeToDeleteCallback;
import com.fanliga.models.AllComments;
import com.fanliga.models.MostCommented;
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

public class ViewAllComments extends AppBaseActivity implements ActivityCallbackInterface {

    Context context;
    ActivityViewAllCommentsBinding viewAllCommentsBinding;
    ActivityCallbackInterface activityCallbackInterface;

    String video_id;

    private final int getVideoDetails = 1,deleteComment=2;

    AllCommentsAdapter allCommentsAdapter;
    ArrayList<AllComments> allCommentsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewAllCommentsBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_comments);

        context = ViewAllComments.this;
        loadLocalVariables(context);
        activityCallbackInterface = ViewAllComments.this;

        init();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            video_id = bundle.getString("video_id", "0");
        }

        Log.e("video_id ", "" + video_id);

        // call get video details API to get all comments
        videoDetailsWS();

        // on click listeners
        viewAllCommentsBinding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalCode.openMainScreen();
                finish();
            }
        });
    }

    public class AllCommentsAdapter extends RecyclerView.Adapter<AllCommentsAdapter.ViewHolder> {
        private ArrayList<AllComments> allComments;
        private Context context;
        AllCommentsRowItemBinding allCommentsRowItemBinding;

        public AllCommentsAdapter(Context context, ArrayList<AllComments> allCommentsList) {
            this.allComments = allCommentsList;
            this.context = context;
        }

        @NonNull
        @Override
        public AllCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            allCommentsRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.all_comments_row_item, parent, false);

            return new AllCommentsAdapter.ViewHolder(allCommentsRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull final AllCommentsAdapter.ViewHolder holder, int position) {
            final AllComments allComments1 = allComments.get(position);

            if (position % 2 == 1) {
                allCommentsRowItemBinding.llMainCommentsRow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                allCommentsRowItemBinding.llMainCommentsRow.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            allCommentsRowItemBinding.tvFirstCommenterName.setText(allComments1.getFirst_name() + " " + allComments1.getLast_name());
            allCommentsRowItemBinding.tvFirstComment.setText(allComments1.getComment());
        }

        @Override
        public int getItemCount() {
            return allComments.size();
        }

        public ArrayList<AllComments> getData() {
            return allComments;
        }

        public void removeItem(int position) {
            allComments.remove(position);
            notifyItemRemoved(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public AllCommentsRowItemBinding allCommentsRowItemBinding;

            public ViewHolder(AllCommentsRowItemBinding allCommentsRowItemBinding) {
                super(allCommentsRowItemBinding.getRoot());
                this.allCommentsRowItemBinding = allCommentsRowItemBinding;
            }
        }
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

    private void deleteCommentWS(AllComments commentModel){
        String wsUrl = AppConstant.DELETE_COMMENT;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sessionData.getObjectAsString(AppConstant.USER_ID));
        params.put("comment_id", commentModel.getId());

        // POST Parameters:
        new WS_Called_Token(context, wsUrl, params, deleteComment, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case getVideoDetails:
                if (jsonObject.getString("status").equals("true")) {
                    allCommentsArrayList = new ArrayList<>();
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray commentsArray = dataObject.getJSONArray("comments");
                    for (int i = 0; i < commentsArray.length(); i++) {
                        JSONObject commentsObject = commentsArray.getJSONObject(i);
                        JSONObject givenByObject = commentsObject.getJSONObject("given_by");
                        AllComments allComments = new AllComments(commentsObject.getString("comment"),
                                givenByObject.getString("first_name"),
                                givenByObject.getString("last_name"),
                                givenByObject.getString("profile_image"),
                                commentsObject.getString("id"),
                                commentsObject.getBoolean("isMyComment"));
                        allCommentsArrayList.add(allComments);
                    }
                    viewAllCommentsBinding.rvComments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    viewAllCommentsBinding.rvComments.addItemDecoration(itemDecoration);
                    allCommentsAdapter = new AllCommentsAdapter(context, allCommentsArrayList);
                    viewAllCommentsBinding.rvComments.setAdapter(allCommentsAdapter);
                    enableSwipeToDeleteAndUndo();
                }
                break;

            case deleteComment:
                if (jsonObject.getString("status").equals("true")) {
                    Log.e("Shahbaz", "Success ");
                    videoDetailsWS();
                }else{
                    Log.e("Shahbaz", "Failed ");
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
            case deleteComment:
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context,allCommentsAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final AllComments item = allCommentsAdapter.getData().get(position);

                allCommentsAdapter.removeItem(position);

                deleteCommentWS(item);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(viewAllCommentsBinding.rvComments);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}