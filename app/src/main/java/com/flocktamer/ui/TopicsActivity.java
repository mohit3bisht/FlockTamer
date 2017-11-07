package com.flocktamer.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.flocktamer.R;
import com.flocktamer.fragments.AboutUsFragment;
import com.flocktamer.fragments.EditProfileFragment;
import com.flocktamer.fragments.FaqFragment;
import com.flocktamer.fragments.TopicsFragment;
import com.flocktamer.utils.Constants;

public class TopicsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_topics);
        if (getIntent() != null && getIntent().getIntExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_TOPICS_FRAGMENT) == Constants.OPEN_FAQ_FRAGMENT) {
            initToolBar("FAQ", true);
            replaceFragment(FaqFragment.newInstance(), R.id.fl_topics_content);
        } else if (getIntent() != null && getIntent().getIntExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_TOPICS_FRAGMENT) == Constants.OPEN_ABOUT_US) {
            initToolBar("About Us", true);
            replaceFragment(AboutUsFragment.newInstance(), R.id.fl_topics_content);
        } else if (getIntent() != null && getIntent().getIntExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_TOPICS_FRAGMENT) == Constants.OPEN_EDIT_PROFILE) {
            initToolBar("Edit Profile", true);
            replaceFragment(EditProfileFragment.newInstance(), R.id.fl_topics_content);
        } else {
            initToolBar("Topics", true);
            replaceFragment(TopicsFragment.newInstance(), R.id.fl_topics_content);
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
