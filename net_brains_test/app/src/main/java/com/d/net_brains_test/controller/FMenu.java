package com.d.net_brains_test.controller;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.net_brains_test.R;
import com.d.net_brains_test.controller.search.FSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 05.05.2018.
 */

public class FMenu extends Fragment {
    private View mRootView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mPageAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.main_menu, null);
        mTabLayout = mRootView.findViewById(R.id.tab_layout);
        mViewPager = mRootView.findViewById(R.id.view_pager);

        fragments.clear();
        if(savedInstanceState != null)
            mViewPager.setCurrentItem(savedInstanceState.getInt("page", 0));
        Fragment fragment = null;

        try {
            fragment = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, FSearch.class.getSimpleName());
        }catch (Exception e){}

        fragments.add(fragment == null? new FSearch():fragment);

        fragment = null;

        try {
            fragment = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, FFavorites.class.getSimpleName());
        }catch (Exception e){}

        fragments.add(fragment == null? new FFavorites():fragment);


        mPageAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", mTabLayout.getSelectedTabPosition());
        for(Fragment fragment: fragments){
            try {
                getActivity().getSupportFragmentManager().putFragment(outState, fragment.getClass().getSimpleName(), fragment);
            }catch (IllegalStateException e){/*значит фрагмент не активен*/}
        }

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        String[] TAB_NAMES = {getString(R.string.search), getString(R.string.favorites)};
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_NAMES[position];
        }
    }

}
