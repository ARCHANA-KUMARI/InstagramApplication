package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robosoft.archana.instagramapplication.R;

/**
 * Created by archana on 22/4/16.
 */
public class UserPostedMediaAdapter extends RecyclerView.Adapter<UserPostedMediaAdapter.PostedMediaHolder> {

    private Context mContext;

    public UserPostedMediaAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private View mOneMedeiaRow;

    @Override
    public PostedMediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mOneMedeiaRow = LayoutInflater.from(mContext).inflate(R.layout.postedmediarow,parent,false);
        PostedMediaHolder postedMediaHolder = new PostedMediaHolder(mOneMedeiaRow);
        return postedMediaHolder;
    }

    @Override
    public void onBindViewHolder(PostedMediaHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class PostedMediaHolder extends RecyclerView.ViewHolder{
        private ImageView mPostedImage;
        public PostedMediaHolder(View itemView) {
            super(itemView);
            mPostedImage = (ImageView) itemView.findViewById(R.id.postedmediaimage);
        }
    }
}
