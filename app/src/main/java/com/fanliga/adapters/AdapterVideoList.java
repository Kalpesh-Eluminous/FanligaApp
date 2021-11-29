package com.fanliga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fanliga.R;

class AdapterVideoList extends RecyclerView.Adapter<AdapterVideoList.AdapterVideoListHolder> {

    @NonNull
    @Override
    public AdapterVideoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_video_list, parent, false);
        return new AdapterVideoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVideoListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AdapterVideoListHolder extends RecyclerView.ViewHolder {

        public AdapterVideoListHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
