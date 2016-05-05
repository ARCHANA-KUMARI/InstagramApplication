package com.robosoft.archana.instagramapplication.Network;

import android.os.AsyncTask;

import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 4/5/16.
 */
public class PostLikedMediaAsyncTask extends AsyncTask<String,Void,String> {

    private String mResponse;
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            if(url!=null) {
                DownloadManager downloadManager = new DownloadManager();
                HttpsURLConnection httpsURLConnection = downloadManager.downloadWithPost(url);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("&access_token=" + Constants.ACCESSTOKEN);
                outputStreamWriter.flush();
                mResponse = InputStreamtoString.readStream(httpsURLConnection.getInputStream());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mResponse;
    }
    //Todo for onPostExecute
    @Override
    protected void onPostExecute(String s) {

    }
}
