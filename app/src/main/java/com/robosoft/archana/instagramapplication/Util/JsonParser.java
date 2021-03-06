package com.robosoft.archana.instagramapplication.Util;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.Constants;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;
import com.robosoft.archana.instagramapplication.Network.DownloadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 28/4/16.
 */
public class JsonParser {

    public List<MediaDetails> medeiaUrlParsed(String response, List<String> mPaginationList, DownloadManager downloadManager,LinkedHashMap<String, ArrayList<CommentDetails>> hashMap,List<MediaDetails> mMediaDetailsList) throws JSONException, MalformedURLException {

        ArrayList<CommentDetails> arrayList;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
        // For pagenation
        JSONObject jsonPagObj = jsonObject.getJSONObject("pagination");
        if (!jsonPagObj.isNull("next_url")) {
            String next_Url = jsonPagObj.getString("next_url");
            mPaginationList.add(next_Url);
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonSubObject = jsonArray.getJSONObject(j);
            MediaDetails mediaDetails = new MediaDetails();
            if (!jsonSubObject.isNull("id")) {
                String mediaId = jsonSubObject.getString("id");
                mediaDetails.setmMediaId(mediaId);
                //https://api.instagram.com/v1/media/1241287195349863257_2972956137/comments/?access_token=2972956137.289b08f.4518b8e436fd444195fdf1d47745a3c5
                String commentUrl = Constants.APIURL + "/media/"+mediaDetails.getmMediaId() +"/comments/?access_token=" + Constants.ACCESSTOKEN;
                URL commentUrlAddress = new URL(commentUrl);
                if(commentUrlAddress!=null){
                    String commentResponse = downloadManager.downloadWithGet(commentUrlAddress);
                    arrayList = new ArrayList<>();
                    hashMap = commentUrlParsed(commentResponse,mediaDetails,arrayList,hashMap);
                }
            }
            if (!jsonSubObject.isNull("comments")) {
                JSONObject commentObject = jsonSubObject.getJSONObject("comments");
                String commentCount = commentObject.getString("count");
                mediaDetails.setmCommentsCount(commentCount);
            }
            if (!jsonSubObject.isNull("created_time")) {
                String created_time = jsonSubObject.getString("created_time");
                created_time = TimeUtil.convertMilliSecToYMD(created_time);
                mediaDetails.setmCreatedTime(created_time);
            }

            if (!jsonSubObject.isNull("likes")) {
                JSONObject likeObject = jsonSubObject.getJSONObject("likes");
                String likesCount = likeObject.getString("count");
                mediaDetails.setmLikeCounts(likesCount);
            }

            if (!jsonSubObject.isNull("images")) {
                JSONObject imageObject = jsonSubObject.getJSONObject("images");
                JSONObject standardResObject = imageObject.getJSONObject("standard_resolution");
                String standardResUrl = standardResObject.getString("url");
                mediaDetails.setmStandardImageResolLink(standardResUrl);
            }

            if (!jsonSubObject.isNull("caption")) {
                JSONObject captionObject = jsonSubObject.getJSONObject("caption");
                if (captionObject.has("text")) {
                    String text = captionObject.getString("text");
                    mediaDetails.setmCaption(text);

                }
            }

            if (!jsonSubObject.isNull("user")) {
                JSONObject userObject = jsonSubObject.getJSONObject("user");
                mediaDetails.setmUserName(userObject.getString("username"));
                mediaDetails.setmProfilePic(userObject.getString("profile_picture"));
                mediaDetails.setmUserId(userObject.getString("id"));

            }

            if (!jsonSubObject.isNull("location")) {
                JSONObject locationObject = jsonSubObject.getJSONObject("location");
                mediaDetails.setmLocation(locationObject.getString("name"));

            }
            if(jsonSubObject.has("user_has_liked")){
                if(!jsonSubObject.isNull("user_has_liked")){
                    mediaDetails.setmUser_Has_Liked_Status(jsonSubObject.getBoolean("user_has_liked"));
                }
            }
            mMediaDetailsList.add(mediaDetails);
        }
        return  mMediaDetailsList;
    }


    public LinkedHashMap<String, ArrayList<CommentDetails>> commentUrlParsed(String response, MediaDetails mediaDetails,ArrayList<CommentDetails> arrayList, LinkedHashMap<String, ArrayList<CommentDetails>> hashMap) {
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int j = 0; j < jsonArray.length(); j++) {
                CommentDetails commentDetails = new CommentDetails();
                JSONObject subObject = jsonArray.getJSONObject(j);
                if (subObject.has("text")) {
                    if (!subObject.isNull("text")) {
                        String commentText = subObject.getString("text");
                        commentDetails.setmCommentText(commentText);
                    }
                }
                if (subObject.has("from")) {
                    if (!subObject.isNull("from")) {
                        JSONObject fromObject = subObject.getJSONObject("from");
                        String whocommented = fromObject.getString("username");
                        commentDetails.setmWhoCommented(whocommented);
                        String whocommentedUserId = fromObject.getString("id");
                        commentDetails.setmCommetedUserId(whocommentedUserId);
                    }
                }
                if (subObject.has("id")) {
                    if (!subObject.isNull("id")) {
                        String commentId = subObject.getString("id");
                        commentDetails.setmCommentId(commentId);
                    }
                }
                arrayList.add(commentDetails);

            }
            hashMap.put(mediaDetails.getmMediaId(), arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
}
