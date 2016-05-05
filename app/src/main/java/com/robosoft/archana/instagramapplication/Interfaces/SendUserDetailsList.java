package com.robosoft.archana.instagramapplication.Interfaces;

import com.robosoft.archana.instagramapplication.Modal.UserDetail;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archana on 3/5/16.
 */
public interface SendUserDetailsList {
    public void sendUserDetails(List<UserDetail> list, LinkedHashMap<String, UserDetail> hashMaplist);
}
