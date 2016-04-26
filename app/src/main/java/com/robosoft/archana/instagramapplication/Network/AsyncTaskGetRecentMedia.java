package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;
import com.robosoft.archana.instagramapplication.Util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 26/2/16.
 */
public class AsyncTaskGetRecentMedia extends AsyncTask<Void, Void, List<MediaDetails>> {

    private Context mContext;
    private int mSizeOfId = 0;
    private List<MediaDetails> mediaDetailsList;
    String mUrl[];
    SendMediaDetails sendMediaDetails;

    public AsyncTaskGetRecentMedia(Context mContext, List<MediaDetails> mediaDetailsList, String mUrl[]) {
        this.mContext = mContext;
        this.mediaDetailsList = mediaDetailsList;
        this.mUrl = mUrl;
        sendMediaDetails = (SendMediaDetails) mContext;
    }

    @Override
    protected List<MediaDetails> doInBackground(Void... params) {


        for (int i = 0; i < mUrl.length; i++) {
            URL url = null;
            try {
                Log.i("Hello", "I AM IN AsyncTask Class");
                url = new URL(mUrl[i]);
                if (url != null) {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = httpsURLConnection.getInputStream();
                    String response = InputStreamtoString.readStream(inputStream);
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    // For pagenation
                    JSONObject jsonPagObj = jsonObject.getJSONObject("pagination");
                    if (!jsonPagObj.isNull("next_url")) {
                         String next_Url = jsonPagObj.getString("next_url");
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonSubObject = jsonArray.getJSONObject(j);
                        MediaDetails mediaDetails = new MediaDetails();
                        if (!jsonSubObject.isNull("comments")) {
                        JSONObject commentObject = jsonSubObject.getJSONObject("comments");
                        String commentCount = commentObject.getString("count");
                        mediaDetails.setmCommentsCount(commentCount);
                    }
                    if(!jsonSubObject.isNull("created_time")){
                        String created_time = jsonSubObject.getString("created_time");
                        created_time = TimeUtil.convertMilliSecToYMD(created_time);
                        mediaDetails.setmCreatedTime(created_time);
                    }

                    if (!jsonSubObject.isNull("likes")) {
                        JSONObject likeObject = jsonSubObject.getJSONObject("likes");
                        String likesCount = likeObject.getString("count");
                        mediaDetails.setmLikeCounts(likesCount);
                    }

                    if (!jsonSubObject.isNull("images")) {
                        JSONObject imageObject = jsonSubObject.getJSONObject("images");
                        JSONObject standardResObject = imageObject.getJSONObject("standard_resolution");
                        String standardResUrl = standardResObject.getString("url");
                        mediaDetails.setmStandardImageResolLink(standardResUrl);
                    }

                    if (!jsonSubObject.isNull("caption")) {
                        JSONObject captionObject = jsonSubObject.getJSONObject("caption");
                        if (captionObject.has("text")) {
                            String text = captionObject.getString("text");
                            mediaDetails.setmCaption(text);

                        }
                    }

                    if (!jsonSubObject.isNull("id")) {
                        String mediaId = jsonSubObject.getString("id");
                        mediaDetails.setmMediaId(mediaId);
                        mSizeOfId++;
                    }

                    if (!jsonSubObject.isNull("user")) {
                        JSONObject userObject = jsonSubObject.getJSONObject("user");
                        mediaDetails.setmUserName(userObject.getString("username"));
                        mediaDetails.setmProfilePic(userObject.getString("profile_picture"));
                    }

                    if (!jsonSubObject.isNull("location")) {
                        JSONObject locationObject = jsonSubObject.getJSONObject("location");
                        mediaDetails.setmLocation(locationObject.getString("name"));

                    }
                    mediaDetailsList.add(mediaDetails);
                }
            }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mediaDetailsList;
    }

    @Override
    protected void onPostExecute(List<MediaDetails> mediaDetailses) {
        super.onPostExecute(mediaDetailses);
        sendMediaDetails.sendMediaId(mediaDetailses, mSizeOfId);
    }
}
