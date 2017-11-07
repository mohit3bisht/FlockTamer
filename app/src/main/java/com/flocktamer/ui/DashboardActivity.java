package com.flocktamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.fragments.CategoriesFragment;
import com.google.gson.JsonArray;

public class DashboardActivity extends BaseActivity {

    private JsonArray mRssData;
    private JsonArray mKeyTopics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        boolean isFromNavigation = false;
        if (getIntent() != null) {
            isFromNavigation = getIntent().getBooleanExtra("isFromNavigation", false);
        }

        setMyContentView(R.layout.activity_dashboard);
        replaceFragment(CategoriesFragment.newInstance(isFromNavigation), R.id.content_dashboard);
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

    public void logout(View view) {
        if (getAppPref().isUserLogin()) {
            getAppPref().clearSession();
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void setRssData(JsonArray rssData) {
        mRssData = rssData;
    }

    public void setKeyTopics(JsonArray topics) {
        mKeyTopics = topics;
    }

    public JsonArray getKeyTopics() {
        return mKeyTopics;
    }

    public JsonArray getRssData() {
        return mRssData;
    }

}
