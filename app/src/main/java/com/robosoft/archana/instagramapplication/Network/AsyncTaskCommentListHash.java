package com.robosoft.archana.instagramapplication.Network;

import android.content.Context;
import android.os.AsyncTask;

import com.robosoft.archana.instagramapplication.Interfaces.SendCommentDetails;
import com.robosoft.archana.instagramapplication.Interfaces.TaskListener;
import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 2/3/16.
 */
public class AsyncTaskCommentListHash extends AsyncTask<Void, CommentDetails, LinkedHashMap<String, ArrayList<CommentDetails>>> {

    LinkedHashMap<String, ArrayList<CommentDetails>> hashMap;
    private List<MediaDetails> mediaDetailsList;
    private Context mContext;
    private List<CommentDetails> mCommnetList;
    private String mUrl[];
    SendCommentDetails sendComment;
    ArrayList<CommentDetails> arrayList;
    TaskListener taskListener;

    public AsyncTaskCommentListHash(Context mContext, String url[], List<MediaDetails> mediaDetailsList, LinkedHashMap<String, ArrayList<CommentDetails>> hashMap) {
        this.mContext = mContext;
        this.mCommnetList = mCommnetList;
        this.mUrl = url;
        this.mediaDetailsList = mediaDetailsList;
        this.hashMap = hashMap;
        sendComment = (SendCommentDetails) mContext;

    }

    @Override
    protected LinkedHashMap<String, ArrayList<CommentDetails>> doInBackground(Void... params) {

        for (int i = 0; i < mUrl.length; i++) {
            arrayList = new ArrayList<>();
            try {

                URL urlComment = new URL(mUrl[i]);
                MediaDetails mediaDetails = mediaDetailsList.get(i);
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

                    arrayList.add(commentDetails);


                }
                hashMap.put(mediaDetails.getmMediaId(), arrayList);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return hashMap;
    }

    @Override
    protected void onPostExecute(LinkedHashMap<String, ArrayList<CommentDetails>> hashMap) {
        super.onPostExecute((LinkedHashMap<String, ArrayList<CommentDetails>>) hashMap);
        sendComment.sendCommentsHashMap(hashMap);

    }
}

