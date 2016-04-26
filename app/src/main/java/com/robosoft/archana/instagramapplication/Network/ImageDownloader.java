package com.robosoft.archana.instagramapplication.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by archana on 26/2/16.
 */
public class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {

    private LruCache<String, Bitmap> mMemoryCache;
    private String urladdress;
    private Bitmap mPic = null;
    private ImageView mImage;

    public ImageDownloader(LruCache<String, Bitmap> lruCache, String urladdress, ImageView mImage) {
        this.urladdress = urladdress;
        this.mImage = mImage;
        this.mMemoryCache = lruCache;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL url = new URL(urladdress);
            if(url!=null) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                mPic = BitmapFactory.decodeStream(inputStream);
                mMemoryCache.put(urladdress, mPic);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPic;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImage.setImageBitmap(bitmap);

    }

}