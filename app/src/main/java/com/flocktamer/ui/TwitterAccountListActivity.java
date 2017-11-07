package com.flocktamer.ui;

import android.app.Fragment;
import android.os.Bundle;

import com.flocktamer.R;
import com.flocktamer.fragments.TwitterAccountListFragment;
import com.flocktamer.utils.FlockTamerLogger;

public class TwitterAccountListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_compose);
        setBaseContentView(R.layout.activity_compose);
        FlockTamerLogger.createLog("111111111111112");
        initToolBar("Select Account", true);
        replaceFragment(TwitterAccountListFragment.newInstance(this), R.id.frame_layout);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }
}
