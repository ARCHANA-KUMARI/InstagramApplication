package com.robosoft.archana.instagramapplication.Modal;

import java.io.Serializable;

/**
 * Created by archana on 26/2/16.
 */
public class MediaDetails implements Serializable{

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
    private boolean mUser_Has_Liked_Status;

    public boolean ismUser_Has_Liked_Status() {
        return mUser_Has_Liked_Status;
    }

    public void setmUser_Has_Liked_Status(boolean mUser_Has_Liked_Status) {
        this.mUser_Has_Liked_Status = mUser_Has_Liked_Status;
    }

    String mCommentsCount,mLikeCounts,mStandardImageResolLink,mCaption,mMediaId,mUserName,mProfilePic,mLocation,mPagenation_nextUrl,mCreatedTime,mUserId;

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmCreatedTime() {
        return mCreatedTime;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmCreatedTime(String mCreatedTime) {
        this.mCreatedTime = mCreatedTime;
    }

    public String getmCommentsCount() {
        return mCommentsCount;
    }

    public String getmPagenation_nextUrl() {
        return mPagenation_nextUrl;
    }

    public void setmPagenation_nextUrl(String mPagenation_nextUrl) {
        this.mPagenation_nextUrl = mPagenation_nextUrl;
    }

    public void setmCommentsCount(String mCommentsCount) {
        this.mCommentsCount = mCommentsCount;
    }

    public String getmStandardImageResolLink() {
        return mStandardImageResolLink;
    }

    public void setmStandardImageResolLink(String mStandardImageResolLink) {
        this.mStandardImageResolLink = mStandardImageResolLink;
    }

    public String getmLikeCounts() {
        return mLikeCounts;
    }

    public void setmLikeCounts(String mLikeCounts) {
        this.mLikeCounts = mLikeCounts;
    }

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmMediaId() {
        return mMediaId;
    }

    public String getmProfilePic() {
        return mProfilePic;
    }

    public void setmProfilePic(String mProfilePic) {
        this.mProfilePic = mProfilePic;
    }

    public void setmMediaId(String mMediaId) {
        this.mMediaId = mMediaId;
    }


}
