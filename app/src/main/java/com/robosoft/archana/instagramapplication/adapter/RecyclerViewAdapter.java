package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constatns;
import com.robosoft.archana.instagramapplication.Modal.Followers;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskCommentListHash;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskPostComment;
import com.robosoft.archana.instagramapplication.Network.ImageDownloader;
import com.robosoft.archana.instagramapplication.R;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;
import com.robosoft.archana.instagramapplication.Util.SnackBarView;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by archana on 29/2/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CommentViewHolder> {

    private Context mContext;
    private View mOneRow;
    private List<MediaDetails> mMedeiaDetailsList;

    private LruCache<String, Bitmap> mLrucache;
    private HashMap<String, ArrayList<CommentDetails>> hashMap;
    private ArrayList<CommentDetails> mTempCommentListValueList;
    private ArrayList<String> mMedeiaKeyList = new ArrayList<>();
    private ArrayList<ArrayList<CommentDetails>> mMediaCommentValueList = new ArrayList<>();

    int noOfComments;

    public RecyclerViewAdapter(LruCache<String, Bitmap> mLrucache, Context mContext, List<MediaDetails> mMedeiaDetailsList, int noOfComments, HashMap<String, ArrayList<CommentDetails>> hashMap) {
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
        int commencount = mTempCommentListValueList.size();
        if (noOfComments > 0) {
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, noOfComments, viewType, mediaDetails.getmMediaId());
            return commentViewHolder;
        } else {
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow, commencount, viewType, mediaDetails.getmMediaId());
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
        new ImageDownloader(mLrucache, mediaDetails.getmProfilePic(), holder.mImageProfilePic).execute();
        mTempCommentListValueList = mMediaCommentValueList.get(position);
        if (noOfComments > 0 && noOfComments <= mTempCommentListValueList.size()) {
            for (int i = 0; i < noOfComments; i++) {
                holder.mTextComment = (TextView) holder.arrayList.get(i);
                CommentDetails commentDetails = mTempCommentListValueList.get(i);
                holder.mTextComment.setText(commentDetails.getmWhoCommented() + "  " + commentDetails.getmCommentText());
            }
        } else {

            for (int i = 0; i < mTempCommentListValueList.size(); i++) {
                holder.mTextComment = (TextView) holder.arrayList.get(i);
                CommentDetails commentDetails = mTempCommentListValueList.get(i);
                holder.mTextComment.setText(commentDetails.getmWhoCommented() + "  " + commentDetails.getmCommentText());
            }
        }

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
        private TextView mTextComment,mTextUserName,mTextLocation;
        String mediaId;
        LinearLayout linearLayout;
        ArrayList<TextView> arrayList = new ArrayList<>();
        int position;

        public CommentViewHolder(View itemView, int noOfCommentTextView, final int position, final String mediaId) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mTextDescription = (TextView) itemView.findViewById(R.id.textdescription);
            mCommentButton = (ImageButton) itemView.findViewById(R.id.commentbtn);
            mTextUserName = (TextView) itemView.findViewById(R.id.username);
            mTextLocation = (TextView)itemView.findViewById(R.id.location);
            mImageProfilePic = (ImageView)itemView.findViewById(R.id.profilepic);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lay);
            mEditComment = (EditText) itemView.findViewById(R.id.comment);

            this.mediaId = mediaId;
            this.noOfCommentTextView = noOfCommentTextView;
            this.position = position;
            for (int i = 0; i < noOfCommentTextView; i++) {
                mTextComment = new TextView(mContext);
                linearLayout.addView(mTextComment);
                arrayList.add(mTextComment);

            }
            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = mEditComment.getText().toString();
                    if (!comment.isEmpty()) {
                        TextView textComment = new TextView(mContext);
                        linearLayout.addView(textComment);
                        textComment.setText(Constatns.API_USERNAME + " " + comment);
                        String postCommentUrl = Constatns.APIURL + "/media/" + mediaId + "/comments";
                        new AsyncTaskPostComment(mContext, comment).execute(postCommentUrl);
                    }

                }
            });
        }
    }


}