package com.flocktamer.interfaces;

import com.flocktamer.model.childmodel.Feed;

/**
 * Created by amandeepsingh on 2/9/16.
 */
public interface TwitterItemClickListener {
    void onFavoriteClick(Feed feed);

    void onReplyClick(Feed feed);

    void onRetweetClick(Feed feed);

    void onProfileClick(String screenName);

    void onEmailClick(Feed tweetid);

}
