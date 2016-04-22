package com.robosoft.archana.instagramapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.robosoft.archana.instagramapplication.R;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initUi();
        setUpBackButton();

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
    }
    private void setUpBackButton(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
