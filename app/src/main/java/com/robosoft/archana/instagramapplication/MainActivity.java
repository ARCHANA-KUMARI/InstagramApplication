package com.robosoft.archana.instagramapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.robosoft.archana.instagramapplication.Activity.UserProfileActivity;
import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.Interfaces.NoOfCommentInterface;
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
import com.robosoft.archana.instagramapplication.Network.AsyncTaskGetRecentMedia;
import com.robosoft.archana.instagramapplication.Util.NetworkStatus;
import com.robosoft.archana.instagramapplication.Util.OrientationHandler;
import com.robosoft.archana.instagramapplication.adapter.InstagramRecyclAdapter;
import com.robosoft.archana.instagramapplication.fragment.SettingFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Communicator,SendFollwersData,SendMediaDetails,NoOfCommentInterface,TaskListener,SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private List<UserDetail> mUserDetailList = new ArrayList<>();
    private List<Followers> mFollwersDetailsList = new ArrayList<>();
    private List<MediaDetails> mMedeiaDetailsList = new ArrayList<>();
    private List<String> mPaginationUrlList = new ArrayList<>();
    private List<String> mRecentMediaUrlList = new ArrayList<>();
    private LinkedHashMap<String,ArrayList<CommentDetails>> mHashMapCommentsDetails = new LinkedHashMap<>();

    private WebView mWebview;
    private RecyclerView mRecycler;
    private InstagramRecyclAdapter mInstagramRecyclAdapter;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cach
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> mLrucCach = new LruCache<>(cacheSize);
    private LinearLayoutManager mLinearLayoutManager;

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private SwipeRefreshLayout mSwiper;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton mFloatBtn;

    private static final String MYPREFERENCES = "mypreference";
    private SharedPreferences mSharedPreference;
    private static final String NO_OF_COMMENTS ="NoOfComment";
    public static final String NO_OF_SETTING_COMMENTS = "noOfSetComments";
    private static final String LIST = "List";
    private static final String HASHMAP ="HashMap";
    private static final String RECENT_MEDIA_URL_LIST = "RecentMediaUrlList";
    private static final String PAGINATION_LIST = "PaginationList";

    private SharedPreferences.Editor mEditor;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        setContentView(R.layout.activity_main);
        initUi();
        setSupportActionBar(mToolbar);
        loadWebView();
        mSwiper.setOnRefreshListener(this);
        setOnScrollListenerWithRecyclerView();

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
        mFloatBtn = (FloatingActionButton)findViewById(R.id.fab);
        mWebview = (WebView) findViewById(R.id.webview);
        mSwiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        mRecycler = (RecyclerView)findViewById(R.id.recycler);
        mSharedPreference = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
        mEditor.apply();
    }
   private void getRequestToken(){
       OrientationHandler.lockOrientation(this);
       mWebview.setWebViewClient(new AuthWebClient(MainActivity.this));
       mWebview.getSettings().setJavaScriptEnabled(true);
       mWebview.loadUrl(Constants.aurthUrlString);
   }

   private void loadWebView(){
       if(NetworkStatus.isNetworkAvailable(this)){
           if(mBundle==null){
               loadInstagramHomePage();
               getRequestToken();
           }
       }
       else{
           setSnackBar();
           mFloatBtn.setOnClickListener(this);
           mFloatBtn.setVisibility(View.VISIBLE);
       }
   }
    @Override
    public void sendUserData(List<AccessToken> accessTokens) {
       // mWebview.setVisibility(View.GONE);
        String accessToken = null,id ;
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
    public void sendFollowersId(List<Followers> mList) {

        for(int i = 0;i<mList.size();i++){
            Followers followers = mList.get(i);
            String fId = followers.getmFollowsUserId();
            mRecentMediaUrlList.add(Constants.APIURL + "/users/"+fId +"/media/recent/?access_token=" + Constants.ACCESSTOKEN+Constants.NO_OF_MEDIA_LOADED_AT_ONE_TIME);
        }
        AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(this,mMedeiaDetailsList,mRecentMediaUrlList,mHashMapCommentsDetails);
        asyncTaskGetRecentMedia.execute();
    }

    @Override
    public void sendMediaId(List<MediaDetails> mMediaList,List<String> paginationList,LinkedHashMap<String, ArrayList<CommentDetails>> mList) {
        mMedeiaDetailsList = mMediaList;
        mHashMapCommentsDetails = mList;
        mPaginationUrlList = paginationList;
        setInstagramRecyclerAdapter();
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(int noOfComments) {
        mEditor.putInt(NO_OF_COMMENTS,noOfComments);
        mEditor.commit();
        setInstagramRecyclerAdapter();
    }

    @Override
    public void onStartTask() {
        mProgressDialog = ProgressDialog.show(this,"Loading started.....","Please Wait for a momment");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(LIST, (Serializable) mMedeiaDetailsList);
        outState.putSerializable(PAGINATION_LIST, (Serializable) mPaginationUrlList);
        outState.putSerializable(HASHMAP,mHashMapCommentsDetails);
        outState.putSerializable(RECENT_MEDIA_URL_LIST, (Serializable) mRecentMediaUrlList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        mRecentMediaUrlList = (List<String>) savedInstanceState.getSerializable(RECENT_MEDIA_URL_LIST);
        mPaginationUrlList = savedInstanceState.getStringArrayList(PAGINATION_LIST);
        mMedeiaDetailsList = (List<MediaDetails>) savedInstanceState.getSerializable(LIST);
        mHashMapCommentsDetails = (LinkedHashMap<String, ArrayList<CommentDetails>>) savedInstanceState.getSerializable(HASHMAP);
        setInstagramRecyclerAdapter();

    }

   private void setInstagramRecyclerAdapter(){
        mWebview.setVisibility(View.GONE);
        mSwiper.setVisibility(View.VISIBLE);
        mInstagramRecyclAdapter = new InstagramRecyclAdapter(mLrucCach,this,mMedeiaDetailsList,mSharedPreference.getInt(NO_OF_COMMENTS,0),mHashMapCommentsDetails);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setAdapter(mInstagramRecyclAdapter);
        mFloatBtn.setVisibility(View.GONE);
   }

    @Override
    public void onRefresh() {

        OrientationHandler.lockOrientation(this);
        if (NetworkStatus.isNetworkAvailable(this)) {

            if (mMedeiaDetailsList.size() > 0) {
                mMedeiaDetailsList.clear();
            }
            if (mHashMapCommentsDetails.size() > 0) {
                mHashMapCommentsDetails.clear();
            }
            mProgressDialog = ProgressDialog.show(this, "Loading started.....", "Please Wait for a momment");
            AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(this, mMedeiaDetailsList,mRecentMediaUrlList,mHashMapCommentsDetails);
            asyncTaskGetRecentMedia.execute();
            mRecycler.setAdapter(mInstagramRecyclAdapter);
            mInstagramRecyclAdapter.notifyDataSetChanged();
            mSwiper.setRefreshing(false);


       }else{
           setSnackBar();
           mSwiper.setRefreshing(false);
       }
    }


    @Override
    public void onClick(View v) {
        setSnackBar();
    }
    public void loadInstagramHomePage(){
        mWebview.loadUrl(getResources().getString(R.string.loginpageurl));
        mWebview.setVerticalScrollBarEnabled(false);
        mWebview.setHorizontalScrollBarEnabled(false);
    }
    private void setSnackBar(){

        Snackbar.make(mCoordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetworkStatus.isNetworkAvailable(getApplicationContext())){
                            loadInstagramHomePage();
                            getRequestToken();
                        }

                    }
                }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    // For pagination implementation
    boolean loading = true;
    int firstVisiblesItems, visibleItemCount, totalItemCount;

    private void setOnScrollListenerWithRecyclerView() {
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                loading = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    firstVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (mPaginationUrlList.size() > 0) {
                                if(NetworkStatus.isNetworkAvailable(MainActivity.this)){

                                    mProgressDialog = ProgressDialog.show(MainActivity.this, "Loading started.....", "Please Wait for a momment");
                                    OrientationHandler.lockOrientation(MainActivity.this);
                                    AsyncTaskGetRecentMedia asyncTaskGetRecentMedia = new AsyncTaskGetRecentMedia(MainActivity.this, mMedeiaDetailsList, mPaginationUrlList, mHashMapCommentsDetails);
                                    asyncTaskGetRecentMedia.execute();
                                    mRecycler.setAdapter(mInstagramRecyclAdapter);
                                    mInstagramRecyclAdapter.notifyDataSetChanged();
                                }
                                else {
                                    setSnackBar();
                                }

                            }
                        }
                    }


                }
            }

        });
    }

}
