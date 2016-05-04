package com.robosoft.archana.instagramapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Network.ImageDownloader;
import com.robosoft.archana.instagramapplication.R;
import com.robosoft.archana.instagramapplication.adapter.UserPostedMediaAdapter;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mUserNameTxtView,mNoOfPostTxtView,mUserFollowsTxtView,mUserFollowedByTxtView;
    private ImageView mProfilePicImgView;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> mLrucCach = new LruCache<>(cacheSize);
    public static final String USER_DETAILS = "Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initUi();
        setUi();
        setUpBackButton();
        setPostedMediaAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
           onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initUi(){
        mToolbar = (Toolbar) findViewById(R.id.toolbarofuser);
        mRecyclerView = (RecyclerView) findViewById(R.id.postedmediarecycle);
        mProfilePicImgView = (ImageView) findViewById(R.id.profilepic);
        mUserNameTxtView = (TextView) findViewById(R.id.username);
        mNoOfPostTxtView = (TextView) findViewById(R.id.posttext);
        mUserFollowsTxtView = (TextView) findViewById(R.id.followingtext);
        mUserFollowedByTxtView = (TextView)findViewById(R.id.followerstext);
    }

    private void setUpBackButton(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUi(){
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            UserDetail userDetail = (UserDetail) bundle.getSerializable(USER_DETAILS);
            new ImageDownloader(mLrucCach,userDetail.getmProfile_pic(),mProfilePicImgView).execute();
            mUserNameTxtView.setText(userDetail.getmFull_name());
            mNoOfPostTxtView.setText(userDetail.getmNo_Of_Post());
            mUserFollowsTxtView.setText(userDetail.getmFollows());
            mUserFollowedByTxtView.setText(userDetail.getmFollowedBy());
        }
    }
    private void setPostedMediaAdapter(){
        UserPostedMediaAdapter postedMediaAdapter = new UserPostedMediaAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(postedMediaAdapter);

    }
}
