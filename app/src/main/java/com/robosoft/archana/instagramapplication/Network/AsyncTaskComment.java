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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archana on 27/2/16.
 */
public class AsyncTaskComment extends AsyncTask<Void,Void,List<CommentDetails>> {

    private Context mContext;
    private List<CommentDetails> mCommnetList;
    private String mUrl[];
    SendCommentDetails sendComment;

    public AsyncTaskComment(Context mContext, List<CommentDetails> mCommnetList,String url[]) {
        this.mContext = mContext;
        this.mCommnetList = mCommnetList;
        this.mUrl = url;
        sendComment = (SendCommentDetails) mContext;
    }

    @Override
    protected List<CommentDetails> doInBackground(Void... params) {
      //  Log.i("Hello","Length of Idddddddddddd is"+mUrl.length);

        for(int i = 0;i<mUrl.length;i++){
            try {
             //   Log.i("Hello","&&&&&&&&&&&&&&&&&&&&****************************Outer Loop is****************"+i);
                URL urlComment = new URL(mUrl[i]);
        //  Log.i("Hello","Url is"+i+urlComment);
                HttpsURLConnection httpurlConnection = (HttpsURLConnection) urlComment.openConnection();
                InputStream inputStream = httpurlConnection.getInputStream();
               /* httpurlConnection.setRequestMethod("POST");
                httpurlConnection.setDoInput(true);
                httpurlConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpurlConnection.getOutputStream());

                outputStreamWriter.write("client_id=" + Constatns.CLIENT_ID +
                        "&client_secret=" + Constatns.CLIENT_SECRET +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + Constatns.CALLBACK_URL +
                        "&code=" + mRequestToken);
                outputStreamWriter.flush();*/
                String response = InputStreamtoString.readStream(inputStream);
              //  Log.i("Hello","Response is"+response);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                //  if(!jsonObject.isNull("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int j = 0;j<jsonArray.length();j++){
                    CommentDetails commentDetails = new CommentDetails();
                    //  Log.i("Hello","&&&&&&&&&&&&&&&****************Inner Loop is************************"+j);
                      JSONObject subObject = jsonArray.getJSONObject(j);
                      if(!subObject.isNull("text")) {

                          String commentText = subObject.getString("text");
                          commentDetails.setmCommentText(commentText);
                        //  Log.i("Hello", "Comment Text is" + commentText);
                      }

                      if(!subObject.isNull("from")){
                          if(subObject.has("from")){
                              JSONObject fromObject = subObject.getJSONObject("from");
                              String whocommented = fromObject.getString("username");
                            //  Log.i("Hello","Who commented is "+whocommented);
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
