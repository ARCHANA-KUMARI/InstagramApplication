package com.robosoft.archana.instagramapplication.Network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.robosoft.archana.instagramapplication.Interfaces.SendMediaDetails;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Util.JsonParser;
import com.robosoft.archana.instagramapplication.Util.OrientationHandler;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 26/2/16.
 */
public class AsyncTaskGetRecentMedia extends AsyncTask<Void, Void, List<MediaDetails>> {

    private Context mContext;
    private List<MediaDetails> mMediaDetailsList;
    private List<String>mPaginationList = new ArrayList<>();
    private List<String> mUrl;
    SendMediaDetails sendMediaDetails;
    LinkedHashMap<String, ArrayList<CommentDetails>> hashMap;

    public AsyncTaskGetRecentMedia(Context mContext, List<MediaDetails> mediaDetailsList, List<String> mUrl,LinkedHashMap<String, ArrayList<CommentDetails>> hashMap) {
        this.mContext = mContext;
        this.mMediaDetailsList = mediaDetailsList;
        this.mUrl = mUrl;
        sendMediaDetails = (SendMediaDetails) mContext;
        this.hashMap = hashMap;
    }

    @Override
    protected List<MediaDetails> doInBackground(Void... params) {

        JsonParser jsonParser = new JsonParser();
        if(mPaginationList.size()>0){
            mPaginationList.clear();
        }
        DownloadManager downloadManager = new DownloadManager();
        if(mUrl.size()>0) {
            for (int i = 0; i < mUrl.size(); i++) {
                URL url = null;
                try {
                     url = new URL(mUrl.get(i));
                     if (url != null) {
                        String response = downloadManager.downloadWithGet(url);
                        mMediaDetailsList = jsonParser.medeiaUrlParsed(response,mPaginationList,downloadManager,hashMap,mMediaDetailsList);
                     }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return mMediaDetailsList;

    }

    @Override
    protected void onPostExecute(List<MediaDetails> mediaDetailses) {
        if(sendMediaDetails!=null){
            sendMediaDetails.sendMediaId(mediaDetailses,mPaginationList,hashMap);
        }
        OrientationHandler.unLockOrientation((Activity) mContext);
    }
}
