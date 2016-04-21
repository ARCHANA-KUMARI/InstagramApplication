package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

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
                url = new URL(mUrl[i]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                String response = InputStreamtoString.readStream(inputStream);
               // Log.i("Hello","Page"+response);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jsonSubObject = jsonArray.getJSONObject(j);
                    MediaDetails mediaDetails = new MediaDetails();
                    if (!jsonSubObject.isNull("comments")) {
                        JSONObject commentObject = jsonSubObject.getJSONObject("comments");

                        String commentCount = commentObject.getString("count");

                        mediaDetails.setmCommentsCount(commentCount);
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

                    if(!jsonSubObject.isNull("user")){
                        JSONObject userObject = jsonSubObject.getJSONObject("user");
                        mediaDetails.setmUserName(userObject.getString("username"));
                        mediaDetails.setmProfilePic(userObject.getString("profile_picture"));
                    }

                    if(!jsonSubObject.isNull("location")){
                        Log.i("Hello","I am in if location block");
                        JSONObject locationObject = jsonSubObject.getJSONObject("location");
                         mediaDetails.setmLocation(locationObject.getString("name"));

                    }
                    mediaDetailsList.add(mediaDetails);
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
