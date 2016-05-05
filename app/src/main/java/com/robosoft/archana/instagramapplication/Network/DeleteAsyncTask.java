package com.robosoft.archana.instagramapplication.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 4/5/16.
 */
public class DeleteAsyncTask extends AsyncTask<String,Void,String> {

    private String mResponse;

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            if (url != null) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("DELETE");
              /*  httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);*/
                // No need for this
                /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("&access_token=" + Constants.ACCESSTOKEN);
                outputStreamWriter.flush();*/
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

    @Override
    protected void onPostExecute(String s) {
      Log.i("Hello","Response in Delete is"+mResponse);
    }
}
