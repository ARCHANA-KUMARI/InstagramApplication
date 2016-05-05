package com.robosoft.archana.instagramapplication.Modal;

import java.io.Serializable;

/**
 * Created by archana on 24/2/16.
 */
public class UserDetail implements Serializable {
    private String mFull_name,mProfile_pic,mFollowedBy,mFollows,mNo_Of_Post;

    public String getmNo_Of_Post() {
        return mNo_Of_Post;
    }

    public void setmNo_Of_Post(String mNo_Of_Post) {
        this.mNo_Of_Post = mNo_Of_Post;
    }

    public String getmFull_name() {
        return mFull_name;
    }

    public void setmFull_name(String mFull_name) {
        this.mFull_name = mFull_name;
    }

    public String getmProfile_pic() {
        return mProfile_pic;
    }

    public void setmProfile_pic(String mProfile_pic) {
        this.mProfile_pic = mProfile_pic;
    }

    public String getmFollowedBy() {
        return mFollowedBy;
    }

    public void setmFollowedBy(String mFollowedBy) {
        this.mFollowedBy = mFollowedBy;
    }

    public String getmFollows() {
        return mFollows;
    }

    public void setmFollows(String mFollows) {
        this.mFollows = mFollows;
    }
}
