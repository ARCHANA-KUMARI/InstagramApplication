package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robosoft.archana.instagramapplication.Activity.UserProfileActivity;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskPostComment;
import com.robosoft.archana.instagramapplication.Network.DeleteAsyncTask;
import com.robosoft.archana.instagramapplication.Network.ImageDownloader;
import com.robosoft.archana.instagramapplication.Network.PostLikedMediaAsyncTask;
import com.robosoft.archana.instagramapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by archana on 29/2/16.
 */
public class InstagramRecyclAdapter extends RecyclerView.Adapter<InstagramRecyclAdapter.CommentViewHolder> {

    private Context mContext;
    private View mOneRow;
    private List<MediaDetails> mMedeiaDetailsList;
    private LruCache<String, Bitmap> mLrucache;
    private HashMap<String, ArrayList<CommentDetails>> hashMap;
    private ArrayList<CommentDetails> mTempCommentListValueList;
    private ArrayList<String> mMedeiaKeyList = new ArrayList<>();
    private ArrayList<ArrayList<CommentDetails>> mMediaCommentValueList = new ArrayList<>();
    private LinkedHashMap<String, UserDetail> mUserDetailsListHashMap;
    int noOfComments;
    public static final String USER_DETAILS = "Details";
    int i = 0;
    private boolean likedStatus;

    public InstagramRecyclAdapter(LruCache<String, Bitmap> mLrucache, Context mContext, List<MediaDetails> mMedeiaDetailsList, int noOfComments, HashMap<String, ArrayList<CommentDetails>> hashMap, LinkedHashMap<String, UserDetail> userDetailsListHashMap) {

        this.mContext = mContext;
        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mLrucache = mLrucache;
        this.noOfComments = noOfComments;
        this.hashMap = hashMap;
        this.mUserDetailsListHashMap = userDetailsListHashMap;
        Set keys = hashMap.entrySet();
        Iterator<CommentDetails> iterator = keys.iterator();
        while (iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) iterator.next();
            String keyname = (String) pairs.getKey();
            mMedeiaKeyList.add(keyname);
            ArrayList<CommentDetails> arrayList = (ArrayList<CommentDetails>) pairs.getValue();
            mMediaCommentValueList.add(arrayList);
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mOneRow = LayoutInflater.from(mContext).inflate(R.layout.child, parent, false);
        MediaDetails mediaDetails = mMedeiaDetailsList.get(viewType);
        mTempCommentListValueList = mMediaCommentValueList.get(viewType);
        if (noOfComments > 0 && noOfComments<=mTempCommentListValueList.size()) {
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, noOfComments, viewType, mediaDetails,mediaDetails.getmMediaId());
            return commentViewHolder;
        } else {
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, mTempCommentListValueList.size(), viewType,mediaDetails, mediaDetails.getmMediaId());
            return commentViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        final MediaDetails mediaDetails = mMedeiaDetailsList.get(position);
        Bitmap postedPicBitMap = mLrucache.get(mediaDetails.getmStandardImageResolLink());
        if(postedPicBitMap!=null){
            holder.mImage.setImageBitmap(postedPicBitMap);
        }
        else{
            holder.mImageProfilePic.setImageResource(R.drawable.download);
            new ImageDownloader(mLrucache, mediaDetails.getmStandardImageResolLink(), holder.mImage).execute();
        }

        holder.mTextDescription.setText(mediaDetails.getmCaption());
        holder.mTextUserName.setText(mediaDetails.getmUserName());
        if(mediaDetails.ismUser_Has_Liked_Status()==true){
            holder.mLikeBtn.setImageResource(R.drawable.like);
        }
        else {
            holder.mLikeBtn.setImageResource(R.drawable.unlike);
        }
        holder.mTextUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set keys =mUserDetailsListHashMap.entrySet();
                Iterator<UserDetail> iterator = keys.iterator();
                while (iterator.hasNext()){
                    Map.Entry pairs = (Map.Entry) iterator.next();
                    String keyname = (String) pairs.getKey();
                    if(mediaDetails.getmUserId().equals(keyname)){
                        UserDetail userDetail =  mUserDetailsListHashMap.get(keyname);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(USER_DETAILS, (Serializable) userDetail);
                        Intent intent = new Intent(mContext, UserProfileActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
                }


        });
        holder.mTextLocation.setText(mediaDetails.getmLocation());
        holder.mTextCreatedTime.setText(mediaDetails.getmCreatedTime());
        // Memory cache handling
        final Bitmap profilpicBitmap = mLrucache.get(mediaDetails.getmProfilePic());
        if (profilpicBitmap != null) {
            holder.mImageProfilePic.setImageBitmap(profilpicBitmap);
        } else {
            holder.mImageProfilePic.setImageResource(R.drawable.download);
            new ImageDownloader(mLrucache, mediaDetails.getmProfilePic(), holder.mImageProfilePic).execute();
        }
        mTempCommentListValueList = mMediaCommentValueList.get(position);
        if (noOfComments > 0 && noOfComments <= mTempCommentListValueList.size()&&mTempCommentListValueList.size()>0) {
            for (int i = 0; i < noOfComments; i++) {
                holder.mTextComment = (TextView) holder.CommentListTextView.get(i);
                CommentDetails commentDetails = mTempCommentListValueList.get((mTempCommentListValueList.size()-1)-i);
                holder.mTextComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
                holder.mDeleteCommentBtn = (ImageButton)holder.deleteCommentImgBtnList.get(i);
                holder.mDeleteCommentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Hello","I am in DeleteCommentBtn");
                    }
                });
            }
        } else {
                if(mTempCommentListValueList.size()>0){
                for (int i = 0; i < mTempCommentListValueList.size(); i++) {
                    holder.mTextComment = (TextView) holder.CommentListTextView.get(i);
                    CommentDetails commentDetails = mTempCommentListValueList.get((mTempCommentListValueList.size()-1)-i);
                    //holder.mTextComment.setText(Html.fromHtml("<b><font color =\"#6495ED\">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
                    holder.mTextComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
                    holder.mDeleteCommentBtn = (ImageButton)holder.deleteCommentImgBtnList.get(i);
                    holder.mDeleteCommentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Hello","I am in DeleteCommentBtn Else Part");
                            Log.i("Hello","I am in DeleteCommentBtn");
                        }
                    });
                }
                }
        }
        holder.mEditComment.setHint(R.string.addcommentedit);
        holder.mCommentButton.setImageResource(R.drawable.comment);
    }

    @Override
    public int getItemCount() {
        return mMedeiaDetailsList.size();
    }

   @Override
    public int getItemViewType(int position) {
        return position;
   }

    class CommentViewHolder extends RecyclerView.ViewHolder {


        int noOfCommentTextView;
        private ImageView mImage,mImageProfilePic;
        private TextView mTextDescription;
        private EditText mEditComment;
        private ImageButton mCommentButton,mLikeBtn,mDeleteCommentBtn;
        private TextView mTextComment;
        private TextView mTextUserName;
        private TextView mTextLocation,mTextCreatedTime;
        String mediaId;
        LinearLayout linearLayout,deleteLinearLayout;
        ArrayList<TextView> CommentListTextView = new ArrayList<>();
        ArrayList<ImageButton> deleteCommentImgBtnList = new ArrayList<>();
        int position;

        public CommentViewHolder(View itemView, int noOfCommentTextView, final int position, final MediaDetails mediaDetails, final String mediaId) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mTextDescription = (TextView) itemView.findViewById(R.id.textdescription);
            mCommentButton = (ImageButton) itemView.findViewById(R.id.commentbtn);
            mLikeBtn = (ImageButton)itemView.findViewById(R.id.likebtn);
            mTextUserName = (TextView) itemView.findViewById(R.id.username);
            mTextLocation = (TextView)itemView.findViewById(R.id.location);
            mImageProfilePic = (ImageView)itemView.findViewById(R.id.profilepic);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.commentlayout);
            deleteLinearLayout = (LinearLayout)itemView.findViewById(R.id.deletebtn);
            mEditComment = (EditText) itemView.findViewById(R.id.comment);
            mTextCreatedTime = (TextView) itemView.findViewById(R.id.createdtime);

            this.mediaId = mediaId;
            this.noOfCommentTextView = noOfCommentTextView;
            this.position = position;
            for (int i = 0; i < noOfCommentTextView; i++) {
                mTextComment = new TextView(mContext);
                linearLayout.addView(mTextComment);
                CommentListTextView.add(mTextComment);
                mDeleteCommentBtn = new ImageButton(mContext);
                mDeleteCommentBtn.setImageResource(R.drawable.delete);
                mDeleteCommentBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                mDeleteCommentBtn.setPadding(0, 18, 0, 0);
//                mDeleteCommentBtn.setMaxHeight(3);
//                mDeleteCommentBtn.setMaxWidth(5);
                deleteLinearLayout.addView(mDeleteCommentBtn);
                deleteCommentImgBtnList.add(mDeleteCommentBtn);

            }
            mLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mediaDetails.ismUser_Has_Liked_Status()==true){
                        Log.i("Hello","I am in If Method of Like");
                        String unLikeUrl = Constants.APIURL+"/media/"+mediaId+"/likes"+"?access_token="+Constants.ACCESSTOKEN;
                        new DeleteAsyncTask().execute(unLikeUrl);
                        // likedStatus = false;
                        mediaDetails.setmUser_Has_Liked_Status(false);


                    }
                    else{
                       // mLikeBtn.setImageResource(R.drawable.unlike);
                        // mLikeBtn.setImageResource(R.drawable.like);
                        Log.i("Hello","I am in Else Method of Like");
                        String postLikeUrl = Constants.APIURL+"/media/"+mediaId+"/likes";
                        new PostLikedMediaAsyncTask().execute(postLikeUrl);
                        likedStatus = true;
                        mediaDetails.setmUser_Has_Liked_Status(true);
                    }
                    notifyDataSetChanged();
                }
            });

            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = mEditComment.getText().toString();
                    if (!comment.isEmpty()) {
                        TextView textComment = new TextView(mContext);
                        linearLayout.addView(textComment,0);
                        mEditComment.setText(" ");
                        //TODO FOR COMMENT VALIDATION BASED ON SERVER REQUIREMENT
                        textComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+Constants.API_USERNAME+":"+"</b>"+ "  " + "<small>"+comment+"</small>"));
                        String postCommentUrl = Constants.APIURL + "/media/" + mediaId + "/comments";
                        new AsyncTaskPostComment(mContext, comment).execute(postCommentUrl);
                    }
                }
            });



        }
    }


}