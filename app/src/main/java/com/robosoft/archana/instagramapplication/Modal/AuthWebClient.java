package com.robosoft.archana.instagramapplication.Modal;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.robosoft.archana.instagramapplication.Network.AsyncTaskAccessToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archana on 23/2/16.
 */
public class AuthWebClient extends WebViewClient {

    String request_token;
    private Context mContext;
    private List<AccessToken> mList = new ArrayList<>();
    private AsyncTaskAccessToken mAsyncTaskAccessToken;

    public AuthWebClient(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
       Log.i("Hello","I am in shouldOverrideUrlLoading Method");
        if (url.startsWith(Constants.CALLBACK_URL)) {
            System.out.println(url);
            String parts[] = url.split("=");
            request_token = parts[1];  //This is your request token.
            RequestToken requestToken = new RequestToken();
            requestToken.setRequestToken(request_token);
            Log.i("Hello", "Request_Token is" + request_token);
            mAsyncTaskAccessToken = new AsyncTaskAccessToken(mContext, mList, request_token);
            mAsyncTaskAccessToken.execute();
            return true;
        }
        return false;

    }

}
