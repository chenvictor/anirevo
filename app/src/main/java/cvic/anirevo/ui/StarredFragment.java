package cvic.anirevo.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cvic.anirevo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarredFragment extends Fragment {

    public StarredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StarredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StarredFragment newInstance() {
        StarredFragment fragment = new StarredFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_starred, container, false);
        TabLayout mTabs = view.findViewById(R.id.star_tabs);
        ViewPager mPager = view.findViewById(R.id.star_pager);
        mTabs.setupWithViewPager(mPager);
        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0: return StarredEventFragment.newInstance();
                    case 1: return StarredGuestFragment.newInstance();
                    default: return new Fragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position) {
                    case 0: return "Events";
                    case 1: return "Guests";
                    default: return "Error";
                }
            }
        });
        return view;
    }

}
