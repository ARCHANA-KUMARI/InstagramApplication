package com.robosoft.archana.instagramapplication.Interfaces;

import com.robosoft.archana.instagramapplication.Modal.CommentDetails;

import java.util.List;

/**
 * Created by archana on 27/2/16.
 */
public interface SendCommentDetails {
    public void sendComment(List<CommentDetails> commentDetailsList);
}
