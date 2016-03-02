package com.robosoft.archana.instagramapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.Interfaces.NoOfCommentInterface;
import com.robosoft.archana.instagramapplication.Interfaces.SendCommentDetails;
import com.robosoft.archana.instagramapplication.Interfaces.SendFollwersData;
import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.AccessToken;
import com.robosoft.archana.instagramapplication.Modal.AuthWebClient;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constatns;
import com.robosoft.archana.instagramapplication.Modal.Followers;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Modal.RequestToken;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Network.AsynTaskUserInformation;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskAccessToken;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskComment;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskGetRecentMedia;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;
import com.robosoft.archana.instagramapplication.adapter.RecyclerViewAdapter;
import com.robosoft.archana.instagramapplication.fragment.SettingFragment;
import com.robosoft.archana.instagramapplication.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Communicator,SendFollwersData,SendMediaDetails,SendCommentDetails,NoOfCommentInterface {

  //  private List<AccessToken> mUserPersonalList = new ArrayList<>();
    private List<UserDetail> mUserDetailList = new ArrayList<>();
    private List<Followers> mFollwersDetailsList = new ArrayList<>();
    private List<MediaDetails> mMedeiaDetailsList = new ArrayList<>();
    private List<CommentDetails> mCommentsDetailsList = new ArrayList<>();
    private WebView mWebview;
    private RecyclerView mRecycler;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> mLrucCach = new LruCache<>(cacheSize);
    private  AsyncTaskAccessToken mAsyncTaskAccessToken;
  //  private AuthWebClient mAuth = new AuthWebClient();
    String request_token;
    private String mUserDetailsUrl;
    private Toolbar mToolbar;
    RequestToken requestToken = new RequestToken();
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
        String userRecentLikeUrl =  Constatns.APIURL + "/users/"+ id +"/media/recent/?access_token=" + accessToken;
        String followersUrl  = "https://api.instagram.com/v1/users/self/follows?access_token="+accessToken;

        String recentMediaLike = "https://api.instagram.com/v1/users/2972956137/media/recent/?access_token=2972956137.289b08f.4518b8e436fd444195fdf1d47745a3c5";
            //String urlString = Constatns.APIURL + "/users/"+ id +"/media/recent/?access_token=" + accessToken;
    //     https://api.instagram.com/v1/users/self/media/recent/?access_token=ACCESS-TOKEN
        //    String urlString = new String("https://api.instagram.com/v1/users/self/media/recent/?access_token=ACCESS-TOKEN");
         //   String urlString = Constatns.APIURL + "/users/self/media/recent/?access_token="+accessToken;


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

    }

    @Override
    public void sendComment(List<CommentDetails> commentDetailsList) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mLrucCach,this,mFollwersDetailsList,mMedeiaDetailsList,mCommentsDetailsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(recyclerViewAdapter);
    }


    @Override
    public void onClick(int noOfComments) {
        Log.i("Hello","No of comment is "+noOfComments);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mLrucCach,this,mFollwersDetailsList,mMedeiaDetailsList,mCommentsDetailsList,noOfComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(recyclerViewAdapter);
    }
}
// https://api.instagram.com/v1/users/self/media/recent/?access_token=2972956137.289b08f.4518b8e436fd444195fdf1d47745a3c5