package com.robosoft.archana.instagramapplication.Util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * Created by archana on 26/4/16.
 */
public class OrientationHandler {

    public static void lockOrientation(Activity mActivity) {
        int currentOrienttion = mActivity.getResources().getConfiguration().orientation;
        if (currentOrienttion == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public static void unLockOrientation(Activity mActivity) {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
