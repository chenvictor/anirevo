package cvic.anirevo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private final static String TAG = "anirevo.SchedFrag";


    private TabLayout mTabs;
    private ScrollView mScrollView;
    private CalendarDayView mView;

    private CalendarDate mDate;
    private ArLocation mLocation;

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
        mScrollView = getView().findViewById(R.id.calendar_scroller);
        mView = getView().findViewById(R.id.calendar_view);

        initTabs();
        initDateLoc();
        setEvents();
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
                changeLocation(tab.getPosition());
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

    private void initDateLoc() {
        mLocation = LocationManager.getInstance().getLocation(0);
        mDate = DateManager.getInstance().getDate(0);

        mView.setLimitTime(mDate.getStartHour(), mDate.getEndHour());
    }

    private void setEvents() {
        List<CalendarEvent> events = new ArrayList<>();

        for (ArEvent arEvent : mLocation) {
            for (CalendarEvent calEvent : arEvent.getTimeblocks()) {
                if (calEvent.getDate().equals(mDate)) {
                    events.add(calEvent);
                }
            }
        }
        mView.setEvents(events);
    }

    public void changeDate(int idx) {
        mDate = DateManager.getInstance().getDate(idx);
        mView.setLimitTime(mDate.getStartHour(), mDate.getEndHour());
        mScrollView.setScrollY(0);
        setEvents();
    }

    public void changeLocation(int idx) {
        mLocation = LocationManager.getInstance().getLocation(idx);
        setEvents();
    }

}