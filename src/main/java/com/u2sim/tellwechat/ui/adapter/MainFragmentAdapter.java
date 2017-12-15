package com.u2sim.tellwechat.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.u2sim.tellwechat.ui.fragment.impl.CallFragment;
import com.u2sim.tellwechat.ui.fragment.impl.SmsFragment;

/**
 * Created by hanguojing on 2017/12/4 15:10
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SmsFragment();
            case 1:
            default:
                return new CallFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String result = null;
        switch (position) {
            case 0:
                result = "短信转发";
                break;
            case 1:
                result = "电话转发";
                break;
            default:
                break;
        }
        return result;
    }
}
