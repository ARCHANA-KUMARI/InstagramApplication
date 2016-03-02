package com.robosoft.archana.instagramapplication.Interfaces;

import com.robosoft.archana.instagramapplication.Modal.AccessToken;

import java.util.List;

/**
 * Created by archana on 24/2/16.
 */
public interface Communicator {
    public void sendUserData(List<AccessToken> accessTokens);
}
