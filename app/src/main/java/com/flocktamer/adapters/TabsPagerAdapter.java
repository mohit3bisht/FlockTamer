package com.flocktamer.adapters;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.flocktamer.fragments.DefaultTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amandeepsingh on 11/8/16.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private final List<DefaultTabFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public DefaultTabFragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(DefaultTabFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
