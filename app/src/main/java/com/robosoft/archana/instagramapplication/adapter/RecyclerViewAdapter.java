package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.CommentViewHolder>{

    private Context mContext;
    private View mOneRow;

    private List<MediaDetails> mMedeiaDetailsList;
    private List<CommentDetails> mCommentsDetailsList;
    private LruCache<String, Bitmap> mLrucache;
    private HashMap<String,ArrayList<CommentDetails>> hashMap;
    private ArrayList<CommentDetails> mTempCommentListValueList;
    private ArrayList<String> mMedeiaKeyList = new ArrayList<>();
    private ArrayList<ArrayList<CommentDetails>> mMediaCommentValueList = new ArrayList<>();

    int noOfComments;
    int count = 0;
    public RecyclerViewAdapter(LruCache<String, Bitmap> mLrucache, Context mContext, List<MediaDetails> mMedeiaDetailsList, List<CommentDetails> mCommentsDetailsList, HashMap<String, ArrayList<CommentDetails>> mHashMapCommentsDetails) {
        this.mContext = mContext;

        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mCommentsDetailsList = mCommentsDetailsList;
        this.mLrucache = mLrucache;
    }
    public RecyclerViewAdapter(LruCache<String,Bitmap> mLrucache,Context mContext, List<MediaDetails> mMedeiaDetailsList, List<CommentDetails> mCommentsDetailsList,int noOfComments,HashMap<String,ArrayList<CommentDetails>> hashMap) {
        this.mContext = mContext;
        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mCommentsDetailsList = mCommentsDetailsList;
        this.mLrucache = mLrucache;
        this.noOfComments = noOfComments;
        this.hashMap = hashMap;
        Set keys = hashMap.entrySet();
        Log.i("Hello", "Keys are" + keys);
        Iterator<CommentDetails> iterator = keys.iterator();
        while (iterator.hasNext()){
            Map.Entry pairs = (Map.Entry) iterator.next();
            String keyname = (String) pairs.getKey();
            mMedeiaKeyList.add(keyname);
            ArrayList<CommentDetails> arrayList = (ArrayList<CommentDetails>) pairs.getValue();
            mMediaCommentValueList.add(arrayList);
        }

    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mOneRow = LayoutInflater.from(mContext).inflate(R.layout.child,parent,false);
        MediaDetails mediaDetails = mMedeiaDetailsList.get(viewType);
        CommentDetails commentDetails = mCommentsDetailsList.get(viewType);
        if(noOfComments>0){
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow,noOfComments,parent,viewType,mediaDetails.getmMediaId(),commentDetails.getmWhoCommented());
            return commentViewHolder;
        }
        else {

            int commentcount = Integer.parseInt(mediaDetails.getmCommentsCount());
            CommentViewHolder commentViewHolder = new CommentViewHolder(mOneRow,commentcount,parent,viewType,mediaDetails.getmMediaId(),commentDetails.getmWhoCommented());
            return commentViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {


        MediaDetails mediaDetails = mMedeiaDetailsList.get(position);
        if(NetworkStatus.isNetworkAvailable(mContext)){

            new ImageDownloader(mLrucache,mediaDetails.getmStandardImageResolLink(),holder.mImage).execute();

        }
        else
        {
            holder.mImage.setImageResource(R.drawable.download);
        }
        holder.mTextDescription.setText(mediaDetails.getmCaption());
        int commentcount = Integer.parseInt(mediaDetails.getmCommentsCount());
        if(noOfComments>0){
            mTempCommentListValueList = mMediaCommentValueList.get(position);
            if(noOfComments<=mTempCommentListValueList.size()){
                for(int i = 0;i <noOfComments;i++){
                    holder.mTextComment = (TextView)holder.arrayList.get(i);
                    CommentDetails commentDetails =  mTempCommentListValueList.get(i);
                    holder.mTextComment.setText(commentDetails.getmWhoCommented()+"  "+commentDetails.getmCommentText());
                }
            }
            else {
               Toast.makeText(mContext,"No of comment is less in Instagram Post then setting comment Number",Toast.LENGTH_LONG).show();
            }


        }else{
            Log.i("Hello","I AM IN ELSE BLOCK OF IF RECYCLER");
           if(count<=mCommentsDetailsList.size()-1){

                for(int i = 0; i<commentcount;i++){


                    holder.mTextComment = (TextView)holder.arrayList.get(i);
                   // Log.i("Hello","DynemicTextView is"+holder.mTextComment);
                    if(count<=mCommentsDetailsList.size()-1){
                        CommentDetails commentDetails = mCommentsDetailsList.get(count);
                        holder.mTextComment.setText(commentDetails.getmWhoCommented() + " " + commentDetails.getmCommentText());

                    }

                    count++;
                }


            }
          /*mTempCommentListValueList = mMediaCommentValueList.get(position);

                for(int i = 0;i <mTempCommentListValueList.size();i++){
                    holder.mTextComment = (TextView)holder.arrayList.get(i);
                    CommentDetails commentDetails =  mTempCommentListValueList.get(i);
                    holder.mTextComment.setText(commentDetails.getmWhoCommented()+"  "+commentDetails.getmCommentText());
                }*/

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

    class CommentViewHolder extends RecyclerView.ViewHolder{
        int size;
        //  View itemView;
        private ImageView mImage;
        private TextView mTextDescription;
        private EditText mEditComment;
        private ImageButton mCommentButton;
        ViewGroup viewGroup;
        private TextView mTextComment;
        String mediaId,whoCommented;
        LinearLayout linearLayout;
        ArrayList<TextView> arrayList = new ArrayList<>();
        int position;
        public CommentViewHolder(View itemView,int size,ViewGroup viewGroup, final int position, final String mediaId, final String whoCommented) {
            super(itemView);
            //  this.itemView = itemView;
            mImage = (ImageView)itemView.findViewById(R.id.image);
            mTextDescription = (TextView) itemView.findViewById(R.id.textdescription);
            mCommentButton = (ImageButton)itemView.findViewById(R.id.commentbtn);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lay);
            mEditComment = (EditText) itemView.findViewById(R.id.comment);
            this.viewGroup = viewGroup;
            this.mediaId = mediaId;
            this.whoCommented = whoCommented;
            this.size = size;
            this.position = position;
            for(int i =0 ;i<size;i++){
                mTextComment = new TextView(mContext);
                linearLayout.addView(mTextComment);
                arrayList.add(mTextComment);

            }
            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = mEditComment.getText().toString();
                    TextView textComment = new TextView(mContext);
                    linearLayout.addView(textComment);
                    if(comment.length()!=0){
                        textComment.setText(whoCommented + " "+comment);
                        String postCommentUrl = Constatns.APIURL+"/media/"+mediaId+"/comments";
                        new AsyncTaskPostComment(mContext,comment).execute(postCommentUrl);
                    }

                }
            });
        }
    }



}