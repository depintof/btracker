package com.innovamos.btracker.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.innovamos.btracker.fragments.HelpFragmentOne;
import com.innovamos.btracker.fragments.HelpFragmentThree;
import com.innovamos.btracker.fragments.HelpFragmentTwo;

public class PagerAdapter extends FragmentPagerAdapter {

    public static int PAGES = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("Position", Integer.toString(position));

        switch (position) {
            case 0:
                return new HelpFragmentOne();
            case 1:
                return new HelpFragmentTwo();
            case 2:
                return new HelpFragmentThree();
        }

        return new HelpFragmentOne();
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}
