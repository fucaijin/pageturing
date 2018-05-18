package com.fucaijin.pageturning.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

/**
 * 这是要装载到ViewPager的Fragment
 * 如果要实现动态删除/添加/更改ViewPager中的Fragment,
 * 需要将继承的FragmentPagerAdapter替换成FragmentStatePagerAdapter，
 * 前者只要加载过，fragment中的视图就一直在内存中，
 * 在这个过程中无论你怎么刷新，清除都是无用的，直至程序退出；而后者可以。
 * 然后重写Adapter的getItemPosition()，让其返回PagerAdapter.POSITION_NONE实现动态修改Adapter中的Fragment；
 * Created by fucaijin on 2018/5/17.
 */

public class PagerFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList FragmentList;

    public PagerFragmentAdapter(FragmentManager fm, ArrayList FragmentList) {
        super(fm);
        this.FragmentList = FragmentList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
