package com.flocktamer.ui;

import android.os.Bundle;

import com.flocktamer.R;
import com.flocktamer.fragments.ComposeFragment;
import com.flocktamer.fragments.TwitterAccountListFragment;
import com.flocktamer.utils.Keys;

import java.security.Key;

public class ComposeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_compose_new);
        replaceFragment(ComposeFragment.newInstance(this,getIntent().getExtras().getString(Keys.TWITTER_ID),getIntent().getExtras().getString(Keys.USER_ID)), R.id.frame_layout);

        initToolBar("Post Tweet",true);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }
}
