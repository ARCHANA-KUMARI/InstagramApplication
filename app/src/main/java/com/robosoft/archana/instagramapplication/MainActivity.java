package com.robosoft.archana.instagramapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.robosoft.archana.instagramapplication.Activity.UserProfileActivity;
import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.Interfaces.NoOfCommentInterface;
import com.robosoft.archana.instagramapplication.Interfaces.SendCommentDetails;
import com.robosoft.archana.instagramapplication.Interfaces.SendFollwersData;
import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Interfaces.TaskListener;
import com.robosoft.archana.instagramapplication.Modal.AccessToken;
import com.robosoft.archana.instagramapplication.Modal.AuthWebClient;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Modal.Followers;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Network.AsynTaskUserInformation;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskCommentListHash;
import com.robosoft.archana.instagramapplication.Network.AsyncTaskGetRecentMedia;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;
import com.robosoft.archana.instagramapplication.Util.SnackBarView;
import com.robosoft.archana.instagramapplication.adapter.InstagramRecyclAdapter;
import com.robosoft.archana.instagramapplication.fragment.SettingFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Communicator,SendFollwersData,SendMediaDetails,NoOfCommentInterface,SendCommentDetails,TaskListener,SwipeRefreshLayout.OnRefreshListener{


    private List<UserDetail> mUserDetailList = new ArrayList<>();
    private List<Followers> mFollwersDetailsList = new ArrayList<>();
    private List<MediaDetails> mMedeiaDetailsList = new ArrayList<>();
    private LinkedHashMap<String,ArrayList<CommentDetails>> mHashMapCommentsDetails = new LinkedHashMap<>();

    private WebView mWebview;
    private RecyclerView mRecycler;
    private InstagramRecyclAdapter mInstagramRecyclAdapter;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cach
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> mLrucCach = new LruCache<>(cacheSize);

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private SwipeRefreshLayout mSwiper;
    private ProgressDialog progressDialog;

    private static final String MYPREFERENCES = "mypreference";
    private SharedPreferences mSharedPreference;
    private static final String NO_OF_COMMENTS ="NoOfComment";
    public static final String NO_OF_SETTING_COMMENTS = "noOfSetComments";
    private static final String LIST = "List";
    private static final String HASHMAP ="HashMap";

    private int mNoOfFollowers,mNoOfFollowing,mNoOfPost;

    String recentMediaUrl[],commnetsUrl[];
    SharedPreferences.Editor mEditor;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        setContentView(R.layout.activity_main);
        initUi();
        setSupportActionBar(mToolbar);
        mSharedPreference = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
        loadInstagramHomePage();
        mSwiper.setOnRefreshListener(this);
    }

    private void loadInstagramHomePage(){

        if(NetworkStatus.isNetworkAvailable(this)){

            if(mBundle == null){
                mWebview.loadUrl(getResources().getString(R.string.loginpageurl));
                mWebview.setVerticalScrollBarEnabled(false);
                mWebview.setHorizontalScrollBarEnabled(false);
                mWebview.setWebViewClient(new AuthWebClient(MainActivity.this));
                mWebview.getSettings().setJavaScriptEnabled(true);
                mWebview.loadUrl(Constants.aurthUrlString);

            }
        }
        else{
            SnackBarView.setSnackBar(mCoordinatorLayout);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SnackBarView.setSnackBar(view);
                }
            });
        }

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
        switch (id){
            case  R.id.action_settings:
                SettingFragment settingFragment = new SettingFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt(NO_OF_SETTING_COMMENTS,mSharedPreference.getInt(NO_OF_COMMENTS,0));
                settingFragment.setArguments(bundle);
                settingFragment.show(fragmentManager,"Setting");
                return true;
            case R.id.user_profile:
                Intent intent = new Intent(this,UserProfileActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void initUi(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        mWebview = (WebView) findViewById(R.id.webview);
        mSwiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        mRecycler = (RecyclerView)findViewById(R.id.recycler);
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

        recentMediaUrl = new String[mList.size()];
        Log.i("Hello","Size of recentCommentArray"+recentMediaUrl.length);
        for(int i = 0;i<mList.size();i++){
            Followers followers = mList.get(i);
            String fId = followers.getmFollowsUserId();
            recentMediaUrl[i] = Constants.APIURL + "/users/"+fId +"/media/recent/?access_token=" + Constants.ACCESSTOKEN+Constants.NO_OF_MEDIA_LOADED_AT_ONE_TIME;
        }
        AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(this,mMedeiaDetailsList,recentMediaUrl);
        asyncTaskGetRecentMedia.execute();
    }

    @Override
    public void sendMediaId(List<MediaDetails> mMediaList,int sizeOfId) {

        mMedeiaDetailsList = mMediaList;
        commnetsUrl = new String[sizeOfId];
        int countMediaId = 0;
        for(int i = 0 ;i<commnetsUrl.length;i++){
            MediaDetails mediaDetails = mMediaList.get(i);
            commnetsUrl[countMediaId] = Constants.APIURL + "/media/"+mediaDetails.getmMediaId() +"/comments/?access_token=" + Constants.ACCESSTOKEN;
            countMediaId++;
        }
        AsyncTaskCommentListHash asyncTaskCommentListHash = new AsyncTaskCommentListHash(this,commnetsUrl,mMedeiaDetailsList, mHashMapCommentsDetails);
        asyncTaskCommentListHash.execute();

    }

    @Override
    public void onClick(int noOfComments) {
        mEditor.putInt(NO_OF_COMMENTS,noOfComments);
        mEditor.commit();
        setInstagramRecyclAdapter();
    }

    @Override
    public void sendCommentsHashMap(LinkedHashMap<String, ArrayList<CommentDetails>> mList) {

        setInstagramRecyclAdapter();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStartTask() {

        if(mBundle==null){
            progressDialog = ProgressDialog.show(this,"Loading started.....","Please Wait for a momment");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(LIST, (Serializable) mMedeiaDetailsList);
        outState.putSerializable(HASHMAP,mHashMapCommentsDetails);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        mMedeiaDetailsList = (List<MediaDetails>) savedInstanceState.getSerializable(LIST);
        mHashMapCommentsDetails = (LinkedHashMap<String, ArrayList<CommentDetails>>) savedInstanceState.getSerializable(HASHMAP);
        setInstagramRecyclAdapter();
        super.onRestoreInstanceState(savedInstanceState);
    }

   private void setInstagramRecyclAdapter(){

        mWebview.setVisibility(View.GONE);
        mSwiper.setVisibility(View.VISIBLE);
        mInstagramRecyclAdapter = new InstagramRecyclAdapter(mLrucCach,this,mMedeiaDetailsList,mSharedPreference.getInt(NO_OF_COMMENTS,0),mHashMapCommentsDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mInstagramRecyclAdapter);
   }

    int count = 0;
    @Override
    public void onRefresh() {

        if(NetworkStatus.isNetworkAvailable(this)){
            if(mMedeiaDetailsList.size()>0){
                mMedeiaDetailsList.clear();
            }
            AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(this,mMedeiaDetailsList,recentMediaUrl);
            asyncTaskGetRecentMedia.execute();
            Log.i("Hello"," Aftrerrrrrrrr MediaListSize is"+mMedeiaDetailsList.size());
            //// TODO: 22/4/16
            Log.i("Hello","Size of HashMap is"+mHashMapCommentsDetails.size());
            AsyncTaskCommentListHash asyncTaskCommentListHash = new AsyncTaskCommentListHash(this,commnetsUrl,mMedeiaDetailsList, mHashMapCommentsDetails);
            asyncTaskCommentListHash.execute();
            mRecycler.setAdapter(mInstagramRecyclAdapter);
            mInstagramRecyclAdapter.notifyDataSetChanged();
            mSwiper.setRefreshing(false);
        }
        else{
            SnackBarView.setSnackBar(mCoordinatorLayout);
        }
    }
}
