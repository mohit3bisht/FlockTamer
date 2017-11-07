package com.flocktamer.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Adapter of Seats Selection in View Pager.
 * Created by amandeepsingh on 4/11/15.
 */
public class SplashPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;


    public SplashPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        mFragmentList = fragments;
    }

    // Returns total number of pages
    @Override
    public int getCount() {

        return mFragmentList.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}