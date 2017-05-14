package com.example.niko.musicschool.adapter.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wzd on 2016/8/12.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles;
    private List<Fragment> mFragments;

    public FragmentAdapter(FragmentManager fm, String[] tabTitles, List<Fragment> fragments) {
        super(fm);
        mTabTitles = tabTitles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments == null) {
            return null;
        }
        return mFragments.get(position % mFragments.size());
    }

    @Override
    public int getCount() {
        if (mFragments == null) {
            return 0;
        }
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTabTitles == null) {
            return "";
        }
        return mTabTitles[position % mTabTitles.length];
    }
}
