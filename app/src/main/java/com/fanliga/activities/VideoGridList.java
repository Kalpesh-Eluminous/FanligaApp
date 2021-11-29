package com.fanliga.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fanliga.R;
import com.fanliga.adapters.Adapter_VideoGrid;
import com.fanliga.utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.ActivityCallbackInterface;
import volley.WS_Called;
import volley.WS_Called_Token;

public class VideoGridList extends AppCompatActivity implements ActivityCallbackInterface {
    Context context;
    RecyclerView rv_grid_list;
    Adapter_VideoGrid adapterVideoList;
    ActivityCallbackInterface activityCallbackInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_grid_list);
        context=VideoGridList.this;
        activityCallbackInterface = VideoGridList.this;
        rv_grid_list=(RecyclerView) findViewById(R.id.rv_grid_video_list);
      //  rv_grid_list.setLayoutManager(new GridLayoutManager(this, 2));

        Log.e("KB Log","Inside Class");
        apiLoadData();
    }
    public void apiLoadData(){
        Log.e("KB Log","Inside API");
        String postUrl = "https://backend.fanliga.app/api/v1/user/get-videos";

        Map<String, String> params = new HashMap<String, String>();
                params.put("sport_id", "9");
                params.put("league_id", "14");
                params.put("user_id", "1");

        // POST Parameters:
        new WS_Called_Token(context, postUrl, params, 1, AppConstant.POST).callWS(activityCallbackInterface);
    }

    @Override
    public void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException {
        switch (resultCode) {
            case 1:Log.e("VideoResponse",""+jsonObject);
                adapterVideoList=new Adapter_VideoGrid(jsonObject);
                rv_grid_list.setLayoutManager(new GridLayoutManager(this, 2));
                rv_grid_list.setAdapter(adapterVideoList);
        }
    }

    @Override
    public void volleyErrorMessage(VolleyError error, int resultCode) {
       Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
       Log.e("Error",""+error);
    }
}