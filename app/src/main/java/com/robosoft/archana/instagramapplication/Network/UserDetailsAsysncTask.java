package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;

import com.robosoft.archana.instagramapplication.Interfaces.SendUserDetailsList;
import com.robosoft.archana.instagramapplication.Modal.UserDetail;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 3/5/16.
 */
public class UserDetailsAsysncTask extends AsyncTask<Void,Void,List<UserDetail>> {

    private List<UserDetail> mUserDetailList = new ArrayList<>();
    private LinkedHashMap<String,UserDetail> userDetailHashMap = new LinkedHashMap<>();
    private List<String> mUserDetailsUrlList;
    private Context mContext;

    SendUserDetailsList sendUserDetailsList;
    private String mId;

    public UserDetailsAsysncTask(List<String> mUserDetailsUrlList, Context mContext) {
        this.mUserDetailsUrlList = mUserDetailsUrlList;
        this.mContext = mContext;
        sendUserDetailsList = (SendUserDetailsList) mContext;
    }

    @Override
    protected List<UserDetail> doInBackground(Void... params) {

        DownloadManager downloadManager = new DownloadManager();
        for (int i = 0; i < mUserDetailsUrlList.size(); i++) {
            URL url = null;
            try {
                url = new URL(mUserDetailsUrlList.get(i));
                if (url != null) {
                    String response = downloadManager.downloadWithGet(url);
                    UserDetail userDetail = new UserDetail();
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject jsonDataObj = jsonObject.getJSONObject("data");
                    if (jsonDataObj.has("id")) {
                        if (!jsonDataObj.isNull("id")) {
                            mId = jsonDataObj.getString("id");
                        }
                    }
                    if (jsonDataObj.has("full_name")) {
                        if (!jsonDataObj.isNull("full_name")) {
                            userDetail.setmFull_name(jsonDataObj.getString("full_name"));
                        }
                    }
                    if (jsonDataObj.has("profile_picture")) {
                        if (!jsonDataObj.isNull("profile_picture")) {
                            userDetail.setmProfile_pic(jsonDataObj.getString("profile_picture"));
                        }
                    }
                    if (jsonDataObj.has("counts")) {
                        if (!jsonDataObj.isNull("counts")) {
                            JSONObject jsonCountObj = jsonDataObj.getJSONObject("counts");
                            if (jsonCountObj.has("followed_by")) {
                                if (!jsonCountObj.isNull("followed_by")) {
                                    userDetail.setmFollowedBy(jsonCountObj.getString("followed_by"));
                                }
                            }
                            if (jsonCountObj.has("follows")) {
                                if (!jsonCountObj.isNull("follows")) {
                                    userDetail.setmFollows(jsonCountObj.getString("follows"));
                                }
                            }
                            if (jsonCountObj.has("media")) {
                                if (!jsonCountObj.isNull("media")) {
                                    userDetail.setmNo_Of_Post(jsonCountObj.getString("media"));
                                }
                            }
                        }
                    }
                    mUserDetailList.add(userDetail);
                    userDetailHashMap.put(mId,userDetail);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mUserDetailList;
    }

    @Override
    protected void onPostExecute(List<UserDetail> userDetails) {
        if(sendUserDetailsList!=null){
          sendUserDetailsList.sendUserDetails(userDetails,userDetailHashMap);
      }
      }

}
