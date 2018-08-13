package com.example.anirevo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    PagerAdapter mAdapter;
    TabLayout mTabs;
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mTabs = getView().findViewById(R.id.schedule_tabs);
//        //add 5 tabs temporarily
//        for (int i = 0; i< 5; i++) {
//            mTabs.addTab(mTabs.newTab().setText("Tab " + String.valueOf(i)));
//        }

        mAdapter = new CustomPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = getView().findViewById(R.id.schedule_pager);
        mViewPager.setAdapter(mAdapter);
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        String[] tempPages = {"MainHall", "Meeting Room 121", "Vendor Hall - Blue Stage", "Many Locations", "Overflow is used"};

        CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment temp = CalendarFragment.newInstance("Location " + String.valueOf(i));
            return temp;
        }

        @Override
        public int getCount() {
            return tempPages.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tempPages[position];
        }
    }
}
