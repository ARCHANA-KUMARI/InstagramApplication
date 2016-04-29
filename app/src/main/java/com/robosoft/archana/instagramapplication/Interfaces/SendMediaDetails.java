package com.robosoft.archana.instagramapplication.Interfaces;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;
import com.robosoft.archana.instagramapplication.Modal.MediaDetails;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 27/2/16.
 */
public interface SendMediaDetails {
    public void sendMediaId(List<MediaDetails> mMediaList,List<String> mPaginationList,LinkedHashMap<String, ArrayList<CommentDetails>> mList);
}
