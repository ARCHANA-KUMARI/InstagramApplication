package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskPostComment;
import com.robosoft.archana.instagramapplication.Network.ImageDownloader;
import com.robosoft.archana.instagramapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    int noOfComments;

    public InstagramRecyclAdapter(LruCache<String, Bitmap> mLrucache, Context mContext, List<MediaDetails> mMedeiaDetailsList, int noOfComments, HashMap<String, ArrayList<CommentDetails>> hashMap) {
        this.mContext = mContext;
        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mLrucache = mLrucache;
        this.noOfComments = noOfComments;
        this.hashMap = hashMap;
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
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, noOfComments, viewType, mediaDetails.getmMediaId());
            return commentViewHolder;
        } else {
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, mTempCommentListValueList.size(), viewType, mediaDetails.getmMediaId());
            return commentViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {

        MediaDetails mediaDetails = mMedeiaDetailsList.get(position);
        new ImageDownloader(mLrucache, mediaDetails.getmStandardImageResolLink(), holder.mImage).execute();
        holder.mTextDescription.setText(mediaDetails.getmCaption());
        holder.mTextUserName.setText(mediaDetails.getmUserName());
        holder.mTextLocation.setText(mediaDetails.getmLocation());
      //  Log.i("Hello","Profile Pic is"+  mediaDetails.getmProfilePic());
        new ImageDownloader(mLrucache, mediaDetails.getmProfilePic(), holder.mImageProfilePic).execute();
        mTempCommentListValueList = mMediaCommentValueList.get(position);
        if (noOfComments > 0 && noOfComments <= mTempCommentListValueList.size()&&mTempCommentListValueList.size()>0) {
            for (int i = 0; i < noOfComments; i++) {
                holder.mTextComment = (TextView) holder.CommentListTextView.get(i);
                CommentDetails commentDetails = mTempCommentListValueList.get((mTempCommentListValueList.size()-1)-i);
                holder.mTextComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
            }
        } else {
                if(mTempCommentListValueList.size()>0){
                for (int i = 0; i < mTempCommentListValueList.size(); i++) {
                    holder.mTextComment = (TextView) holder.CommentListTextView.get(i);
                    CommentDetails commentDetails = mTempCommentListValueList.get((mTempCommentListValueList.size()-1)-i);
                 //   holder.mTextComment.setText(Html.fromHtml("<b><font color =\"#6495ED\">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
                    holder.mTextComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+commentDetails.getmWhoCommented() +":"+"</b>"+ "  " + "<small>"+commentDetails.getmCommentText()+"</small>"));
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
        private ImageButton mCommentButton;
        private TextView mTextComment;
        private TextView mTextUserName;
        private TextView mTextLocation;
        String mediaId;
        LinearLayout linearLayout;
        ArrayList<TextView> CommentListTextView = new ArrayList<>();
        int position;

        public CommentViewHolder(View itemView, int noOfCommentTextView, final int position, final String mediaId) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mTextDescription = (TextView) itemView.findViewById(R.id.textdescription);
            mCommentButton = (ImageButton) itemView.findViewById(R.id.commentbtn);
            mTextUserName = (TextView) itemView.findViewById(R.id.username);
            mTextLocation = (TextView)itemView.findViewById(R.id.location);
            mImageProfilePic = (ImageView)itemView.findViewById(R.id.profilepic);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.commentlayout);
            mEditComment = (EditText) itemView.findViewById(R.id.comment);
            this.mediaId = mediaId;
            this.noOfCommentTextView = noOfCommentTextView;
            this.position = position;
            for (int i = 0; i < noOfCommentTextView; i++) {
                mTextComment = new TextView(mContext);
                linearLayout.addView(mTextComment);
                CommentListTextView.add( mTextComment);
            }
            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = mEditComment.getText().toString();
                    if (!comment.isEmpty()) {
                        TextView textComment = new TextView(mContext);
                        linearLayout.addView(textComment,0);
                        mEditComment.setText(" ");
                        textComment.setText(Html.fromHtml("<b><font color ="+R.color.username+">"+Constants.API_USERNAME+":"+"</b>"+ "  " + "<small>"+comment+"</small>"));
                        String postCommentUrl = Constants.APIURL + "/media/" + mediaId + "/comments";
                        new AsyncTaskPostComment(mContext, comment).execute(postCommentUrl);
                    }

                }
            });
        }
    }


}