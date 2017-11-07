package com.flocktamer.interfaces;

import com.flocktamer.model.childmodel.TwitterUser;

/**
 * Created by amandeepsingh on 5/9/16.
 */
public interface OnTwitterAccountDelete {

    void onDeleteClick(TwitterUser user);
}
