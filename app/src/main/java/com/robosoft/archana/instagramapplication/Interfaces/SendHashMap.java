package com.robosoft.archana.instagramapplication.Interfaces;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 2/3/16.
 */
public interface SendHashMap {
    public void sendCommentsHashMap(LinkedHashMap<String, ArrayList<CommentDetails>> mList);
}
