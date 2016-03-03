package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.robosoft.archana.instagramapplication.Interfaces.SendCommentDetails;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constatns;
import com.robosoft.archana.instagramapplication.Util.InputStreamtoString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 27/2/16.
 */
public class AsyncTaskComment extends AsyncTask<Void, Void, List<CommentDetails>> {

    private Context mContext;
    private List<CommentDetails> mCommnetList;
    private String mUrl[];

    SendCommentDetails sendComment;

    public AsyncTaskComment(Context mContext, List<CommentDetails> mCommnetList, String url[]) {
        this.mContext = mContext;
        this.mCommnetList = mCommnetList;
        this.mUrl = url;
        sendComment = (SendCommentDetails) mContext;
    }

    @Override
    protected List<CommentDetails> doInBackground(Void... params) {

        for (int i = 0; i < mUrl.length; i++) {
            try {

                URL urlComment = new URL(mUrl[i]);

                HttpsURLConnection httpurlConnection = (HttpsURLConnection) urlComment.openConnection();
                InputStream inputStream = httpurlConnection.getInputStream();
                String response = InputStreamtoString.readStream(inputStream);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    CommentDetails commentDetails = new CommentDetails();
                    JSONObject subObject = jsonArray.getJSONObject(j);
                    if (!subObject.isNull("text")) {

                        String commentText = subObject.getString("text");
                        commentDetails.setmCommentText(commentText);

                    }

                    if (!subObject.isNull("from")) {
                        if (subObject.has("from")) {
                            JSONObject fromObject = subObject.getJSONObject("from");
                            String whocommented = fromObject.getString("username");
                            commentDetails.setmWhoCommented(whocommented);
                        }
                    }
                    mCommnetList.add(commentDetails);


                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mCommnetList;
    }

    @Override
    protected void onPostExecute(List<CommentDetails> commentDetailses) {
        super.onPostExecute(commentDetailses);
        sendComment.sendComment(commentDetailses);
    }
}
