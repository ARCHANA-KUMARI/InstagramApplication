package com.robosoft.archana.instagramapplication.Modal;

import java.io.Serializable;

/**
 * Created by archana on 27/2/16.
 */
public class CommentDetails implements Serializable {
    public String getmCommetedUserId() {
        return mWhoCommetedUserId;
    }

    public void setmCommetedUserId(String mCommetedUserId) {
        this.mWhoCommetedUserId = mCommetedUserId;
    }

    private String mCommentText,mWhoCommented,mCommentId,mWhoCommetedUserId;

    public String getmCommentId() {
        return mCommentId;
    }

    public void setmCommentId(String mCommentId) {
        this.mCommentId = mCommentId;
    }

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
