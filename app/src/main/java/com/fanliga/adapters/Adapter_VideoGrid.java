package com.fanliga.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fanliga.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Adapter_VideoGrid extends RecyclerView.Adapter<Adapter_VideoGrid.Adapter_VideoGrid_Holder>{
    JSONObject jsonMain;


    public Adapter_VideoGrid(JSONObject Data){
        jsonMain=Data;
        Log.e("VideoResponse 1",""+Data);
    }
    @NonNull
    @Override
    public Adapter_VideoGrid_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_video_list, parent, false);
        return new Adapter_VideoGrid_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_VideoGrid_Holder holder, int position) {
        try {
                Log.e("Inside Adapter",""+jsonMain);

                JSONArray jsonArray=new JSONArray(jsonMain.getString("data"));
                Log.e("Inside Adapter Array",""+jsonArray);

                    JSONObject jsonObject= jsonArray.getJSONObject(position);
                     Log.i("Inside Thumbnail",""+jsonObject.getString("thumbnail_path"));
            Picasso.get().
                    load(jsonObject.getString("thumbnail_path")).
                    placeholder(R.drawable.login_logo).
                    into(holder.gridImageView);
//
//                    Glide.with(holder.gridImageView.getContext()).load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fthecommonwealth.org%2Four-member-countries%2Findia&psig=AOvVaw3uzEUD-qPeoiHVsLh7T7vG&ust=1637745164354000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCJDsy6uSrvQCFQAAAAAdAAAAABAD").
//                            into(holder.gridImageView);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return jsonMain.getJSONArray("data").length();
        } catch (JSONException e) {
            return 0;
           // e.printStackTrace();
        }
    }

    class Adapter_VideoGrid_Holder extends RecyclerView.ViewHolder {
        ImageView gridImageView;
        public Adapter_VideoGrid_Holder(@NonNull View itemView) {
            super(itemView);
            gridImageView=(ImageView)itemView.findViewById(R.id.gridImageView);
        }
    }
}
