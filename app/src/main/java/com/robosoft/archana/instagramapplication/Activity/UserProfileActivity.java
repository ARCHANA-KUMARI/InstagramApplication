package com.robosoft.archana.instagramapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.robosoft.archana.instagramapplication.R;
import com.robosoft.archana.instagramapplication.adapter.UserPostedMediaAdapter;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initUi();
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
    }

    private void setUpBackButton(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setPostedMediaAdapter(){
        UserPostedMediaAdapter postedMediaAdapter = new UserPostedMediaAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(postedMediaAdapter);

    }
}
