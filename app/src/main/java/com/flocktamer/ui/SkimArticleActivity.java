package com.flocktamer.ui;

import android.os.Bundle;

import com.flocktamer.R;
import com.flocktamer.fragments.SkimArticleFragment;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Keys;

public class SkimArticleActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_skim_article);
//        FlockTamerLogger.createLog("Url:+" + getIntent().getExtras().getString("url"));
        Bundle bundle = new Bundle();
        bundle.putString(Keys.URL, getIntent().getExtras().getString(Keys.URL));
        bundle.putString(Keys.TITLE, getIntent().getExtras().getString(Keys.TITLE));
        SkimArticleFragment skimArticleFragment = SkimArticleFragment.newInstance();
        skimArticleFragment.setArguments(bundle);
        replaceFragment(skimArticleFragment, R.id.frame_article_skim);
        removeToolbar();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }
}
