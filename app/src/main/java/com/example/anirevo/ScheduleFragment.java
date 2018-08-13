package com.example.anirevo;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anirevo.model.LocationManager;


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

        mViewPager = getView().findViewById(R.id.schedule_pager);
        mAdapter = new CustomPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabs = getView().findViewById(R.id.schedule_tabs);
        mTabs.setupWithViewPager(mViewPager);
    }

    class CustomPagerAdapter extends FragmentStatePagerAdapter {

        CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return CalendarFragment.newInstance(LocationManager.getInstance().getLocation(i).getTitle());
        }

        @Override
        public int getCount() {
            return LocationManager.getInstance().size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return LocationManager.getInstance().getLocation(position).getTitle();
        }
    }
}
