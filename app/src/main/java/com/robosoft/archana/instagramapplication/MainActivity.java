package com.robosoft.archana.instagramapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.Interfaces.NoOfCommentInterface;
import com.robosoft.archana.instagramapplication.Interfaces.SendCommentDetails;
import com.robosoft.archana.instagramapplication.Interfaces.SendFollwersData;
import com.robosoft.archana.instagramapplication.Interfaces.SendHashMap;
import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.AccessToken;
import com.robosoft.archana.instagramapplication.Modal.AuthWebClient;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constatns;
import com.robosoft.archana.instagramapplication.Modal.Followers;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Network.AsynTaskUserInformation;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskAccessToken;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskComment;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskCommentListHash;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskGetRecentMedia;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;
import com.robosoft.archana.instagramapplication.adapter.RecyclerViewAdapter;
import com.robosoft.archana.instagramapplication.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Communicator,SendFollwersData,SendMediaDetails,SendCommentDetails,NoOfCommentInterface,SendHashMap {


    private List<UserDetail> mUserDetailList = new ArrayList<>();
    private List<Followers> mFollwersDetailsList = new ArrayList<>();
    private List<MediaDetails> mMedeiaDetailsList = new ArrayList<>();
    private List<CommentDetails> mCommentsDetailsList = new ArrayList<>();
    private LinkedHashMap<String,ArrayList<CommentDetails>> mHashMapCommentsDetails = new LinkedHashMap<>();
    private WebView mWebview;
    private RecyclerView mRecycler;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> mLrucCach = new LruCache<>(cacheSize);
    private  AsyncTaskAccessToken mAsyncTaskAccessToken;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        mWebview = (WebView) findViewById(R.id.webview);
        if(NetworkStatus.isNetworkAvailable(this)){
            mWebview.loadUrl("https://www.instagram.com/accounts/login/?force_classic_login");
            mWebview.setVerticalScrollBarEnabled(false);
            mWebview.setHorizontalScrollBarEnabled(false);
            mWebview.setWebViewClient(new AuthWebClient(MainActivity.this));
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.loadUrl(Constatns.aurthUrlString);
        }
        else{
             setSnackBar();
             FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
             fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSnackBar();
                }
            });
        }
        mRecycler = (RecyclerView)findViewById(R.id.recycler);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
          SettingFragment settingFragment = new SettingFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            settingFragment.show(fragmentManager,"Setting");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

     void setSnackBar(){
        Snackbar.make(mCoordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
     }

    @Override
    public void sendUserData(List<AccessToken> accessTokens) {
        String accessToken = null,id = null;
        if(accessTokens.size()==1){
            AccessToken access = accessTokens.get(0);
            Followers followers = new Followers();
            accessToken = access.getmAccessToken();
            id= access.getmUserId();
            mToolbar.setTitle(access.getmUserName());
            followers.setmFollowsUserId(id);
            followers.setmFollowsUserName(access.getmUserName());
            mFollwersDetailsList.add(followers);
        }

        String followersUrl  = "https://api.instagram.com/v1/users/self/follows?access_token="+accessToken;
        AsynTaskUserInformation asynTaskUserInformation = new AsynTaskUserInformation(this,mUserDetailList,mFollwersDetailsList,followersUrl);
        asynTaskUserInformation.execute();



    }

    @Override
    public void sendFdata(List<Followers> mList) {
        //mFollwersDetailsList = mList;
        String recentMediaUrl[] = new String[mList.size()];
        for(int i = 0;i<mList.size();i++){
            Followers followers = mList.get(i);
            String fId = followers.getmFollowsUserId();
            recentMediaUrl[i] =  Constatns.APIURL + "/users/"+fId +"/media/recent/?access_token=" + Constatns.ACCESSTOKEN;
        }
        AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(this,mMedeiaDetailsList,recentMediaUrl);
        asyncTaskGetRecentMedia.execute();
    }

    @Override
    public void sendMediaId(List<MediaDetails> mMediaList,int sizeOfId) {
        String commnetsUrl[] = new String[sizeOfId];
        int countMediaId = 0;
        for(int i = 0 ;i<commnetsUrl.length;i++){

            MediaDetails mediaDetails = mMediaList.get(i);
          //  String mediaId = mediaDetails.getmMediaId();
            commnetsUrl[countMediaId] =  Constatns.APIURL + "/media/"+mediaDetails.getmMediaId() +"/comments/?access_token=" + Constatns.ACCESSTOKEN;
            countMediaId++;


        }
        AsyncTaskComment asyncTaskComment = new AsyncTaskComment(this,mCommentsDetailsList,commnetsUrl);
        asyncTaskComment.execute();

        AsyncTaskCommentListHash asyncTaskCommentListHash = new AsyncTaskCommentListHash(this,mCommentsDetailsList,commnetsUrl,mMediaList, mHashMapCommentsDetails);
        asyncTaskCommentListHash.execute();

    }

    @Override
    public void sendComment(List<CommentDetails> commentDetailsList) {
        mCommentsDetailsList = commentDetailsList;
       /* RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mLrucCach,this,mFollwersDetailsList,mMedeiaDetailsList,mCommentsDetailsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(recyclerViewAdapter);*/
    }


    @Override
    public void onClick(int noOfComments) {
        Log.i("Hello","No of comment is "+noOfComments);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mLrucCach,this,mMedeiaDetailsList,mCommentsDetailsList,noOfComments,mHashMapCommentsDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void sendCommentsHashMap(LinkedHashMap<String, ArrayList<CommentDetails>> mList) {
        mHashMapCommentsDetails = mList;
     /*   Set keys = mList.entrySet();
      Log.i("Hello", "Keys are" + keys);
        Iterator<CommentDetails> iterator = keys.iterator();
        while (iterator.hasNext()){
            Map.Entry pairs = (Map.Entry) iterator.next();
            String keyname = (String) pairs.getKey();
            Log.i("Hello", "Key Name is" + keyname);
            ArrayList<CommentDetails> arrayList = (ArrayList<CommentDetails>) pairs.getValue();
            Log.i("Hello","Arrrrrrrrr size"+arrayList.size());
            for(int i = 0;i<arrayList.size();i++){
                CommentDetails commentDetails = arrayList.get(i);
                Log.i("Hello","Who COmmented ?"+commentDetails.getmWhoCommented()+"And Comment is"+commentDetails.getmCommentText());
            }
        }*/
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mLrucCach,this,mMedeiaDetailsList,mCommentsDetailsList,mHashMapCommentsDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(recyclerViewAdapter);
    }
}
