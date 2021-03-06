package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.Communicator;
import com.robosoft.archana.instagramapplication.Interfaces.TaskListener;
import com.robosoft.archana.instagramapplication.Modal.AccessToken;
import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Modal.RequestToken;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 24/2/16.
 */

public class AsyncTaskAccessToken extends AsyncTask<Void, Void, List<AccessToken>> {
    RequestToken requestToken;
    private Context mContext;
    private List<AccessToken> mList;
    Communicator communicator;
    private String mRequestToken;
    TaskListener taskListener;
    public AsyncTaskAccessToken(Context mContext, List<AccessToken> mList, String requesttoken) {
        this.mContext = mContext;
        taskListener = (TaskListener) mContext;
        requestToken = new RequestToken();
        this.mList = mList;
        this.mRequestToken = requesttoken;
        this.communicator = (Communicator) mContext;

    }
    @Override
    protected void onPreExecute() {
       // OrientationHandler.lockOrientation((Activity) mContext);
        taskListener.onStartTask();

    }
    @Override
    protected List<AccessToken> doInBackground(Void... params) {
        try {
            URL url = new URL(Constants.tokenURLString);
            if(url!=null) {
                DownloadManager downloadManager = new DownloadManager();
                HttpsURLConnection httpsURLConnection = downloadManager.downloadWithPost(url);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + Constants.CLIENT_ID +
                        "&client_secret=" + Constants.CLIENT_SECRET +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + Constants.CALLBACK_URL +
                        "&code=" + mRequestToken);
                outputStreamWriter.flush();
                String response = InputStreamtoString.readStream(httpsURLConnection.getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                String accessTokenString = jsonObject.getString("access_token"); //Here is your ACCESS TOKEN
                Log.i("Hello","Access Token is"+accessTokenString);
                //Create object of AccessToken class
                AccessToken accessTokenClassObject = new AccessToken();
                accessTokenClassObject.setmAccessToken(accessTokenString);
                accessTokenClassObject.setmUserFull_Name(jsonObject.getJSONObject("user").getString("full_name"));
                accessTokenClassObject.setmUserId(jsonObject.getJSONObject("user").getString("id"));
                accessTokenClassObject.setmUserName(jsonObject.getJSONObject("user").getString("username"));
                accessTokenClassObject.setmProfilePicUrl(jsonObject.getJSONObject("user").getString("profile_picture"));
                mList.add(accessTokenClassObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mList;
    }


    @Override
    protected void onPostExecute(List<AccessToken> accessTokens) {
        if(communicator!=null){
            communicator.sendUserData(accessTokens);
        }

    }


}
