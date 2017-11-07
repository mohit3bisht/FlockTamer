package com.flocktamer.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.flocktamer.R;
import com.flocktamer.fragments.TwitterUserFragment;
import com.flocktamer.model.childmodel.Feed;
import com.flocktamer.model.childmodel.TwitterUser;
import com.flocktamer.utils.Constants;

import java.util.List;

public class TwitterUsersActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
//        setMyContentView();
        if (getIntent().getExtras() != null) {
            List<TwitterUser> list = getIntent().getParcelableArrayListExtra(Constants.TWITTER_USERS);
            Feed feed = getIntent().getParcelableExtra(Constants.TWITTER_FEED);
            int clickCode = getIntent().getIntExtra(Constants.TWITTER_CLICK_TYPE, 101);
            boolean isMyTweet = getIntent().getBooleanExtra(Constants.TWITTER_IS_MY_TWEET,false);
            TwitterUserFragment mFragment = TwitterUserFragment.newInstance(list, feed, clickCode,isMyTweet);
            replaceFragment(mFragment, R.id.content_twitter_user);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
