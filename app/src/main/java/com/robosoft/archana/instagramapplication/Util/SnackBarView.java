package com.robosoft.archana.instagramapplication.Util;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by archana on 4/3/16.
 */
public class SnackBarView {
  public static  void setSnackBar(View mCoordinatorLayout){
        Snackbar.make(mCoordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
