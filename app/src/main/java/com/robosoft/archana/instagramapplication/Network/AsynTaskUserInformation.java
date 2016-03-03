package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendFollwersData;
import com.robosoft.archana.instagramapplication.Modal.Followers;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 24/2/16.
 */
public class AsynTaskUserInformation extends AsyncTask<Void, Void, List<Followers>> {

    private Context mContext;
    private List<Followers> mFollowersList;
    private List<UserDetail> mUserDetailsList;
    private String mUrl;
    SendFollwersData sendFollwersData;

    public AsynTaskUserInformation(Context mContext, List<UserDetail> mUserDetailsList, List<Followers> mFollowersList, String mUrl) {

        this.mContext = mContext;
        this.mUserDetailsList = mUserDetailsList;
        this.mFollowersList = mFollowersList;
        this.mUrl = mUrl;
        sendFollwersData = (SendFollwersData) mContext;
    }

    @Override
    protected List<Followers> doInBackground(Void... params) {
        try {
            //Getting Follewers`S ID AND Name
            URL url = new URL(mUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            String response = InputStreamtoString.readStream(inputStream);

            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonSubObject = jsonArray.getJSONObject(i);
                Followers followers = new Followers();
                followers.setmFollowsUserName(jsonSubObject.getString("username"));

                followers.setmFollowsUserId(jsonSubObject.getString("id"));

                mFollowersList.add(followers);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mFollowersList;
    }

    @Override
    protected void onPostExecute(List<Followers> followerses) {
        super.onPostExecute(followerses);
        sendFollwersData.sendFdata(followerses);
    }
}
