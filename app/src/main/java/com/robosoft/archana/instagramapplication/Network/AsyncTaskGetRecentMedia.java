package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.Followers;
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
public class AsyncTaskGetRecentMedia extends AsyncTask<Void,Void,List<MediaDetails>>{

    private Context mContext;
    private int mSizeOfId = 0;
    private List<MediaDetails> mediaDetailsList;
    String mUrl[];
    SendMediaDetails sendMediaDetails;

    public AsyncTaskGetRecentMedia(Context mContext, List<MediaDetails> mediaDetailsList,String mUrl[]) {
        this.mContext = mContext;
        this.mediaDetailsList = mediaDetailsList;
        this.mUrl = mUrl;
        sendMediaDetails = (SendMediaDetails)mContext;
    }

    @Override
    protected List<MediaDetails> doInBackground(Void... params) {

      //  Log.i("Hello","I am in AsyncGetRecentMedia****************");
      //  Log.i("Hello","Length of url is"+mUrl.length);
        for(int i = 0;i<mUrl.length;i++){
       //   Log.i("Hello","********************Outer  Loop is ***"+i+"******************");
      //   Log.i("Hello","Length of url is"+mUrl.length);
          URL url = null;
          try {
              url = new URL(mUrl[i]);
            //  Log.i("Hello","Url is"+i+url);
              HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
              InputStream inputStream = httpsURLConnection.getInputStream();
              String response = InputStreamtoString.readStream(inputStream);
            //  Log.i("Hello","Response is"+response);
              JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            //  if(!jsonObject.isNull("data")) {
                   JSONArray jsonArray = jsonObject.getJSONArray("data");
              //     Log.i("Hello", "Length of jSOn array is" + jsonArray.length());
                   for (int j = 0; j < jsonArray.length(); j++) {
                   //   Log.i("Hello", "***********************" + j + "******************");
                      JSONObject jsonSubObject = jsonArray.getJSONObject(j);
                      MediaDetails mediaDetails = new MediaDetails();
                      if(!jsonSubObject.isNull("comments"))
                      {
                          JSONObject commentObject = jsonSubObject.getJSONObject("comments");
                    // Log.i("Hello","Cooment Object is"+commentObject);
                         String commentCount = commentObject.getString("count");
                       //   Log.i("Hello","Comment count is"+commentCount);
                          mediaDetails.setmCommentsCount(commentCount);
                      }


                      if(!jsonSubObject.isNull("likes")) {
                          JSONObject likeObject = jsonSubObject.getJSONObject("likes");
                          String likesCount = likeObject.getString("count");
                          mediaDetails.setmLikeCounts(likesCount);

                          //Log.i("Hello", "Like Count is" + likesCount);

                      }   if(!jsonSubObject.isNull("images")){
                           JSONObject imageObject = jsonSubObject.getJSONObject("images");
                           JSONObject standardResObject = imageObject.getJSONObject("standard_resolution");
                           String standardResUrl = standardResObject.getString("url");
                           mediaDetails.setmStandardImageResolLink(standardResUrl);    //Log.i("Hello", "Standard Image url is" + standardResUrl);
                       }


                      if (!jsonSubObject.isNull("caption")) {
                          JSONObject captionObject = jsonSubObject.getJSONObject("caption");
                          if (captionObject.has("text")) {
                              String text = captionObject.getString("text");
                             // Log.i("Hello", "Text is" + text);
                              mediaDetails.setmCaption(text);

                          }

                      }
                      if(!jsonSubObject.isNull("id")){

                          String mediaId = jsonSubObject.getString("id");
                     //     Log.i("Hello", "Media id is" + mediaId);
                       //   Log.i("Hello","Midida Id size is"+mSizeOfId);
                          mediaDetails.setmMediaId(mediaId);
                          mSizeOfId++;
                      }
                      mediaDetailsList.add(mediaDetails);
                  }
            //  }
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
        sendMediaDetails.sendMediaId(mediaDetailses,mSizeOfId);
    }
}

/*
 String mCommentsCount,mLikeCounts,mStandardImageResolLink,mCaption,mMediaId;
 */