package com.robosoft.archana.instagramapplication.Network;

import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 28/4/16.
 */
public class DownloadManager {
    private String mStringResponse;
    public String download(URL url){
        try {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            mStringResponse = InputStreamtoString.readStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mStringResponse;
    }
}
