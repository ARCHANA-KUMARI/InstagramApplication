package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendFollwersData;
import com.robosoft.archana.instagramapplication.Modal.Followers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by archana on 24/2/16.
 */
public class AsynTaskUserInformation extends AsyncTask<Void, Void, List<Followers>> {

    private Context mContext;
    private List<Followers> mFollowersList;

    private String mUrl;
    SendFollwersData sendFollwersData;

    public AsynTaskUserInformation(Context mContext,List<Followers> mFollowersList, String mUrl) {

        this.mContext = mContext;
        this.mFollowersList = mFollowersList;
        this.mUrl = mUrl;
        sendFollwersData = (SendFollwersData) mContext;
    }

    @Override
    protected List<Followers> doInBackground(Void... params) {
        try {
            //Getting Follewers`S ID AND Name
             URL url = new URL(mUrl);
             if(url!=null) {
                 DownloadManager downloadManager = new DownloadManager();
                 String response = downloadManager.downloadWithGet(url);
                 if(response!=null) {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonSubObject = jsonArray.getJSONObject(i);
                        Followers followers = new Followers();
                        followers.setmFollowsUserName(jsonSubObject.getString("username"));
                        followers.setmFollowsUserId(jsonSubObject.getString("id"));
                        mFollowersList.add(followers);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mFollowersList;
    }

        @Override
    protected void onPostExecute(List<Followers> followerses) {
            if(sendFollwersData!=null){
            sendFollwersData.sendFollowersId(followerses);
        }
    }
}
