package com.flocktamer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;

import com.flocktamer.adapters.SplashPagerAdapter;
import com.flocktamer.fragments.IntroCompanyFragment;
import com.flocktamer.fragments.IntroCustomSearchFragment;
import com.flocktamer.fragments.IntroReadLaterFragment;
import com.flocktamer.fragments.LoginFragment;
import com.flocktamer.fragments.SplashFragment;
import com.flocktamer.interfaces.WelcomeScreenInterface;
import com.flocktamer.ui.BaseActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class WelcomeActivity extends BaseActivity implements WelcomeScreenInterface {

    private ViewPager pager;
    private SplashPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();

    }

    @Override
    protected void initViews() {
        pager = (ViewPager) findViewById(R.id.vp_splash);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(SplashFragment.newInstance(this));
        fragments.add(new IntroCompanyFragment(this));
        fragments.add(new IntroCustomSearchFragment(this));
        fragments.add(new IntroReadLaterFragment(this));
        fragments.add(LoginFragment.newInstance());
        mAdapter = new SplashPagerAdapter(getFragmentManager(), fragments);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(mAdapter);
        indicator.setViewPager(pager);
    }

    @Override
    protected void initManagers() {
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
    public void onGetStarted() {
        pager.setCurrentItem(4, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (mAdapter != null) {
            Fragment fragment = mAdapter.getItem(pager.getCurrentItem());
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
//       =(SplashPagerAdapter) pager.getAdapter().getIte

    }
}

