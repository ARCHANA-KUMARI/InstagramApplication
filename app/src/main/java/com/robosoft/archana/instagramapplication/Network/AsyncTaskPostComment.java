package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
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
 * Created by archana on 1/3/16.
 */
public class AsyncTaskPostComment extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String mComments;
    private String mResponse;

    public AsyncTaskPostComment(Context mContext, String mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            if(url!=null) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("&access_token=" + Constants.ACCESSTOKEN +
                        "&text=" + mComments);
                outputStreamWriter.flush();
                mResponse = InputStreamtoString.readStream(httpsURLConnection.getInputStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

