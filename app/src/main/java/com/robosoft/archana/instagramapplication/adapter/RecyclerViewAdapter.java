package com.robosoft.archana.instagramapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.robosoft.archana.instagramapplication.Network.AsyncTaskPostComment;
import com.robosoft.archana.instagramapplication.Network.ImageDownloader;
import com.robosoft.archana.instagramapplication.R;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;

import java.util.ArrayList;
import java.util.List;

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
    int noOfComments;
    int count = 0;
    public RecyclerViewAdapter(LruCache<String,Bitmap> mLrucache,Context mContext,List<Followers> mFollwersDetailsList, List<MediaDetails> mMedeiaDetailsList, List<CommentDetails> mCommentsDetailsList) {
        this.mContext = mContext;

        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mCommentsDetailsList = mCommentsDetailsList;
        this.mLrucache = mLrucache;
    }
    public RecyclerViewAdapter(LruCache<String,Bitmap> mLrucache,Context mContext,List<Followers> mFollwersDetailsList, List<MediaDetails> mMedeiaDetailsList, List<CommentDetails> mCommentsDetailsList,int noOfComments) {
        this.mContext = mContext;

        this.mMedeiaDetailsList = mMedeiaDetailsList;
        this.mCommentsDetailsList = mCommentsDetailsList;
        this.mLrucache = mLrucache;
        this.noOfComments = noOfComments;
        Toast.makeText(mContext,"No of comment is"+noOfComments,Toast.LENGTH_LONG).show();
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
                if(count<=mCommentsDetailsList.size()-1){

                    for(int i = 0; i<noOfComments;i++){
                        holder.mTextComment = (TextView)holder.arrayList.get(i);
                        Log.i("Hello","DynemicTextView is"+holder.mTextComment);
                        if(count<=mCommentsDetailsList.size()-1){
                            CommentDetails commentDetails = mCommentsDetailsList.get(count);
                            holder.mTextComment.setText(commentDetails.getmWhoCommented() + " " + commentDetails.getmCommentText());

                         }
                         count++;
                    }

                }
            }else{
                if(count<=mCommentsDetailsList.size()-1){

                    for(int i = 0; i<commentcount;i++){


                        holder.mTextComment = (TextView)holder.arrayList.get(i);
                        Log.i("Hello","DynemicTextView is"+holder.mTextComment);
                        if(count<=mCommentsDetailsList.size()-1){
                            CommentDetails commentDetails = mCommentsDetailsList.get(count);
                            holder.mTextComment.setText(commentDetails.getmWhoCommented() + " " + commentDetails.getmCommentText());

                        }

                        count++;
                    }

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
                   Toast.makeText(mContext,"Button is Clicked"+position+"Comment is"+comment+"And Media Id is"+mediaId,Toast.LENGTH_LONG).show();
                   TextView textComment = new TextView(mContext);
                   linearLayout.addView(textComment);
                   textComment.setText(whoCommented + " "+comment);
                   String postCommentUrl = Constatns.APIURL+"/media/"+mediaId+"/comments";
                  new AsyncTaskPostComment(mContext,comment).execute(postCommentUrl);
              }
          });
        }
    }



}
