package com.robosoft.archana.instagramapplication.Modal;

import java.io.Serializable;

/**
 * Created by archana on 27/2/16.
 */
public class CommentDetails implements Serializable {
    private String mCommentText,mWhoCommented;

    public String getmCommentText() {
        return mCommentText;
    }

    public void setmCommentText(String mCommentText) {
        this.mCommentText = mCommentText;
    }

    public String getmWhoCommented() {
        return mWhoCommented;
    }

    public void setmWhoCommented(String mWhoCommented) {
        this.mWhoCommented = mWhoCommented;
    }
}
