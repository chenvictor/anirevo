package cvic.anirevo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private final static String TAG = "anirevo.SchedFrag";

    private TabLayout mTabs;
    private CalendarFragment calendarFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabs = getView().findViewById(R.id.schedule_tabs);
        calendarFragment = (CalendarFragment) getChildFragmentManager().findFragmentById(R.id.fragment_calendar);
        initTabs();
    }

    private void initTabs() {
        //Add tabs
        mTabs.removeAllTabs();
        for (ArLocation location : LocationManager.getInstance()) {
            mTabs.addTab(mTabs.newTab().setText(location.getPurpose()));
        }

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                calendarFragment.changeLocation(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Do nothing
            }
        });
    }

    public void changeDate(int i) {
        if (calendarFragment != null) {
            calendarFragment.changeDate(i);
        }
    }

    public interface ScheduleInteractionListener {
        void changeDate(int idx);

        void changeLocation(int idx);
    }
}
