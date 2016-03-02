package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.MainActivity;
import com.robosoft.archana.instagramapplication.Modal.AccessToken;
import com.robosoft.archana.instagramapplication.Modal.Constatns;
import com.robosoft.archana.instagramapplication.Modal.RequestToken;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 24/2/16.
 */
public class AsyncTaskAccessToken extends AsyncTask<Void,Void,List<AccessToken>> {
    RequestToken requestToken;
    private Context mContext;
    private List<AccessToken> mList;
    Communicator communicator;
    private String mRequestToken;

    public AsyncTaskAccessToken(MainActivity mContext ,List<AccessToken> mList,String requesttoken) {
        this.mContext = mContext;
        requestToken = new RequestToken();
        this.mList = mList;
        this. mRequestToken = requesttoken;
        this.communicator = (Communicator) mContext;

    }

    @Override
    protected List<AccessToken> doInBackground(Void... params) {
       Log.i("Hello","I am in AsynTaskAccessToken");
        Log.i("Hello","I am doInBackground");
       try
        {   URL url = new URL(Constatns.tokenURLString);
            Log.i("Hello","Token Url is"+url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            outputStreamWriter.write("client_id=" + Constatns.CLIENT_ID +
                    "&client_secret=" + Constatns.CLIENT_SECRET +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + Constatns.CALLBACK_URL +
                    "&code=" + mRequestToken);
            outputStreamWriter.flush();
            String response = InputStreamtoString.readStream(httpsURLConnection.getInputStream());
           // Log.i("Hello","Response of getting access Token is"+response);
            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            String accessTokenString = jsonObject.getString("access_token"); //Here is your ACCESS TOKEN
            //Create object of AccessToken class
            AccessToken accessTokenClassObject = new AccessToken();
            accessTokenClassObject.setmAccessToken(accessTokenString);
            accessTokenClassObject.setmUserFull_Name(jsonObject.getJSONObject("user").getString("full_name"));
            accessTokenClassObject.setmUserId(jsonObject.getJSONObject("user").getString("id"));
            accessTokenClassObject.setmUserName(jsonObject.getJSONObject("user").getString("username"));
            accessTokenClassObject.setmProfilePicUrl(jsonObject.getJSONObject("user").getString("profile_picture"));
            // Log.i("Hello","Id is"+id);
            mList.add(accessTokenClassObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mList;
    }

/*
   private String mAccessToken;
    private String mUserFull_Name;
    private String mUserId;
    private String mUserName;
    private String mProfilePicUrl;
 */


    @Override
    protected void onPostExecute(List<AccessToken> accessTokens) {
        super.onPostExecute(accessTokens);
       // Log.i("Hello", "Request token is in onPost" + mRequestToken);
        communicator.sendUserData(accessTokens);
       // Log.i("Hello","List size is"+accessTokens.size());
     //   Log.i("Hello","AsyncTaskAccessToken is finished");

    }




}
