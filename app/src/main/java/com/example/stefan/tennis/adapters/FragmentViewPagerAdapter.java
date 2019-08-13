package com.example.stefan.tennis.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marian on 06/03/2017.
 */

public abstract class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public FragmentViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = mFragmentList.get(position);
        if (position == getCount() - 1) {
            onFinishLoading();
        }
        return result;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void setFragment(int position, Fragment fragment, String title) {
        mFragmentList.set(position, fragment);
        mFragmentTitleList.set(position, title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public List<Fragment> getPagerFragments() {
        return mFragmentList;
    }

    public abstract void onFinishLoading();
}

