package com.robosoft.archana.instagramapplication.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by archana on 2/3/16.
 */
public class NetworkStatus {

    private Context mContext;
    public  static boolean isNetworkAvailable(Context mContext) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //  Log.i("Hello","Network status is"+(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
