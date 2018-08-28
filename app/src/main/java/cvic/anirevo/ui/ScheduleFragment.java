package cvic.anirevo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.R;
import cvic.anirevo.handlers.NavigationHandler;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends StateHolderFragment implements NavigationHandler.MenuHandler{

    private final static String TAG = "anirevo.SchedFrag";

    private MenuItem mSelector;
    private Dialog mDateSelectorDialog;

    private TabLayout mTabs;
    private ScrollView mScrollView;
    private CalendarDayView mView;

    private List<ArLocation> mLocations;

    private int mDate = 0;
    private int mLocation = 0;

    public ScheduleFragment() {
        super("SCHEDULE");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocations = LocationManager.getInstance().getScheduleEvents();
        mTabs = getView().findViewById(R.id.schedule_tabs);
        mScrollView = getView().findViewById(R.id.calendar_scroller);
        mView = getView().findViewById(R.id.calendar_view);

        if (mTabs.getTabCount() == 0) {
            initTabs();
        }

        setEvents();
    }

    private void initTabs() {
        //Add tabs
        if (mLocations.size() != 0) {
            for (ArLocation location : mLocations) {
                mTabs.addTab(mTabs.newTab().setText(location.getPurpose()));
            }

            mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    changeLocation(tab.getPosition());
                    setEvents();
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
        } else {
            mTabs.addTab(mTabs.newTab().setText("ERROR: NO LOCATIONS"));
        }
    }

    private void setEvents() {
        List<CalendarEvent> events = new ArrayList<>();

        if (mLocations.size() != 0) {
            for (ArEvent arEvent : mLocations.get(mLocation)) {
                for (CalendarEvent calEvent : arEvent.getTimeblocks()) {
                    if (calEvent.getDate().equals(DateManager.getInstance().getDate(mDate))) {
                        events.add(calEvent);
                    }
                }
            }
        }
        mView.setEvents(events);
        CalendarDate date = DateManager.getInstance().getDate(mDate);
        mView.setLimitTime(date.getStartHour(), date.getEndHour());
    }

    /**
     * Changes the date, and returns the String value of the date
     * @param idx   index of the date
     * @return      The name of teh date
     */
    public String changeDate(int idx) {
        if (idx == mDate) {
            //no change
            return null;
        }
        mDate = idx;
        CalendarDate date = DateManager.getInstance().getDate(mDate);
        mScrollView.setScrollY(0);
        return date.getName();
    }

    public void changeLocation(int idx) {
        if (mLocation == idx) {
            //no change
            return;
        }
        mLocation = idx;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu) {
        if (mSelector == null) {
            mSelector = menu.findItem(R.id.schedule_date_selector);
        }
        mSelector.setTitle(DateManager.getInstance().getDate(mDate).getName());
        mSelector.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mDateSelectorDialog == null) {
                    mDateSelectorDialog = buildSelectorDialog();
                }
                mDateSelectorDialog.show();
                return true;
            }
        });
    }

    private void selectorPicked(int which) {
        String name = changeDate(which);
        if (name != null && mSelector != null) {
            mSelector.setTitle(name);
        }
        setEvents();
    }

    private Dialog buildSelectorDialog() {
        if (getActivity() == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Date");

//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_select_dialog_singlechoice);
        for (CalendarDate date : DateManager.getInstance()) {
            adapter.add(date.getName());
        }

        builder.setSingleChoiceItems(adapter, mDate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectorPicked(i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    @Override
    public Object storeState() {
        return new SchedState(mDate, mLocation, mTabs.getScrollX());
    }

    @Override
    public void restoreState(Object state) {
        SchedState schedState = (SchedState) state;
        mTabs.getTabAt(schedState.loc).select();
        selectorPicked(schedState.date);
        setEvents();
    }
}

class SchedState {
    int date;
    int loc;
    int tabScroll;

    SchedState(int date, int loc, int tabScroll) {
        this.date = date;
        this.loc = loc;
        this.tabScroll = tabScroll;
    }
}