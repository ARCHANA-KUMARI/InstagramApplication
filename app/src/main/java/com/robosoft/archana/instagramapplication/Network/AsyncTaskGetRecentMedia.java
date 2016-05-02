package com.robosoft.archana.instagramapplication.Network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    static int check = 0;

    public AsyncTaskGetRecentMedia(Context mContext, List<MediaDetails> mediaDetailsList, List<String> mUrl,LinkedHashMap<String, ArrayList<CommentDetails>> hashMap) {
        this.mContext = mContext;
        this.mMediaDetailsList = mediaDetailsList;
        this.mUrl = mUrl;
        sendMediaDetails = (SendMediaDetails) mContext;
        this.mPaginationList = mPaginationList;
        this.hashMap = hashMap;
    }
    @Override
    protected List<MediaDetails> doInBackground(Void... params) {
        check++;
        Log.i("Hello","No of Check"+check);
        JsonParser jsonParser = new JsonParser();
        Log.i("Hello","Pagination Size is before clear"+mPaginationList.size());
        Log.i("Hello","MediaList size is"+mMediaDetailsList.size());
        Log.i("Hello","HashMap Size is"+hashMap.size());
        if(mPaginationList.size()>0){
            mPaginationList.clear();
        }
        Log.i("Hello","Pagination Size is After clear"+mPaginationList.size());
        DownloadManager downloadManager = new DownloadManager();
        Log.i("Hello","Size of Url is"+mUrl.size());
        if(mUrl.size()>0) {
            Log.i("Hello","Size of Url is"+mUrl.size());
            for (int i = 0; i < mUrl.size(); i++) {
                URL url = null;
                try {
                     url = new URL(mUrl.get(i));
                     Log.i("Hello","Url is....................."+url);
                      if (url != null) {
                        String response = downloadManager.download(url);
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
        Log.i("Hello","Size of pagination in OnPostOFMedia"+mPaginationList.size());
        Log.i("Hello","Size of Media in OnPostOFMedia"+mMediaDetailsList.size());
        Log.i("Hello","Size of HaShMap in OnPostOFMedia"+hashMap.size());
        if(sendMediaDetails!=null){
            sendMediaDetails.sendMediaId(mediaDetailses,mPaginationList,hashMap);
        }
        OrientationHandler.unLockOrientation((Activity) mContext);
    }
}
