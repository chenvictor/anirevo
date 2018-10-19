package cvic.anirevo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.GuestActivity;
import cvic.anirevo.R;
import cvic.anirevo.handlers.ScheduleFragmentHitboxHandler;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.tasks.FetchScheduleEventsTask;

import static cvic.anirevo.ui.ArEventAdapter.EXTRA_EVENT_ID;
import static cvic.anirevo.ui.ArGuestAdapter.EXTRA_GUEST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends AniRevoFragment implements FetchScheduleEventsTask.FetchScheduleEventsTaskListener, ScheduleFragmentHitboxHandler.EventTappedListener{

    private final static String TAG = "anirevo.SchedFrag";

    private MenuItem mSelector;
    private Dialog mDateSelectorDialog;

    private ViewPager mPager;
    private NestedScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private DayViewAdapter mAdapter;

    private List<ArLocation> mLocations;

    private int mDate = -1;
    private int mLocation = -1;

    private int mStartHour = 0;

    private ScheduleFragmentHitboxHandler mHitboxHandler;

    public ScheduleFragment() {
        super("SCHEDULE");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        mPager = view.findViewById(R.id.pager_tabs);
        mScrollView = view.findViewById(R.id.calendar_scroller);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DayViewAdapter(getLayoutInflater());
        mRecyclerView.setAdapter(mAdapter);
        //setup viewpager
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHitboxHandler = new ScheduleFragmentHitboxHandler(this);
        EventDecoration.initialize(getResources());
        mLocations = LocationManager.getInstance().getScheduleEvents();
        initTabs();
    }

    @Override
    protected void handleAppBarTabLayout() {
        mAppBarTabs.setupWithViewPager(mPager);
        mAppBarTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mAppBarTabs.setVisibility(View.VISIBLE);
    }

    private void initTabs() {
        //Add tabs
        mPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = new View(getContext());
                view.setOnClickListener(mHitboxHandler);
                view.setOnTouchListener(mHitboxHandler);
                view.setTag(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public int getCount() {
                return mLocations.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mLocations.get(position).getPurpose();
            }
        });
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                changeLocation(position);
                setEvents();
            }
        });
    }

    private void setEvents() {
        int locId = mLocations.get(mLocation).getId();
        int dateId = mDate;

        new FetchScheduleEventsTask(this).execute(locId, dateId);
    }

    private void updateEvents(List<CalendarEvent> events) {
        //remove all decorations
        int toRemove = mRecyclerView.getItemDecorationCount();
        for (int i = 0; i < toRemove; i++) {
            mRecyclerView.removeItemDecorationAt(0);
        }
        //clear old hitboxes
        mHitboxHandler.clearHitboxes();
        //add new decorations
        Collections.sort(events, new Comparator<CalendarEvent>() {
            @Override
            public int compare(CalendarEvent calendarEvent, CalendarEvent t1) {
                return (calendarEvent.getStartHour() - t1.getStartHour());
            }
        });

        CalendarEvent prevEvent = null;
        EventDecoration prevDeco = null;
        int rightMargin = 0;
        for (CalendarEvent event : events) {
            if (sameStart(event, prevEvent)) {
                int width = mScrollView.getWidth() - rightMargin - (int) EventDecoration.mLeftMargin;
                int leftShift =  width / 2;
                rightMargin += leftShift;
                prevDeco.setLeftShift(leftShift);
            } else if (intersects(event, prevEvent)) {
                //increment margin
                rightMargin += 40;
            } else {
                //reset margin
                rightMargin = 0;
            }
            EventDecoration deco = new EventDecoration(mHitboxHandler, event, mStartHour, rightMargin);
            mRecyclerView.addItemDecoration(deco);
            prevDeco = deco;
            prevEvent = event;
        }
    }

    private boolean sameStart(CalendarEvent event1, CalendarEvent event2) {
        if (event2 == null) {
            return false;
        }
        return event1.getStartTime().equals(event2.getStartTime());
    }

    /**
     * Calculates if two events will intersect in the calendar
     * @param event1    first event
     * @param event2    second event
     * @return     true if the events will intersect, false otherwise
     */
    private boolean intersects(CalendarEvent event1, CalendarEvent event2) {
        if (event2 == null) {
            return false;
        }
        double event1Start = event1.getStartTime().getHour() + (event1.getStartTime().getMinute() / 60.0);
        double event1End = event1.getEndTime().getHour() + (event1.getEndTime().getMinute() / 60.0);
        double event2Start = event2.getStartTime().getHour() + (event2.getStartTime().getMinute() / 60.0);
        double event2End = event2.getEndTime().getHour() + (event2.getEndTime().getMinute() / 60.0);

        if (event1Start >= event2End) {
            //if event1 starts after event2 ends, false
            return false;
        }
        return !(event2Start >= event1End);
    }



    private void updateDate() {
        CalendarDate date = DateManager.getInstance().getDate(mDate);
        mStartHour = date.getStartHour();
        mAdapter.setHourBounds(date.getStartHour(), date.getEndHour());
    }

    /**
     * Changes the date, and returns the String value of the date
     * @param idx   index of the date
     * @return      The name of teh date
     */
    private String changeDate(int idx) {
        mDate = idx;
        CalendarDate date = DateManager.getInstance().getDate(mDate);
        mScrollView.setScrollY(0);
        return date.getName();
    }

    private void changeLocation(int idx) {
        if (mLocation == idx) {
            //no change
            return;
        }
        mLocation = idx;
    }

    @Override
    public void onMenuInflated(Menu menu) {
        mSelector = menu.findItem(R.id.schedule_date_selector);
        if (mDate >= 0) {
            mSelector.setTitle(DateManager.getInstance().getDate(mDate).toString());
        }
        mSelector.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mDateSelectorDialog == null) {
                    mDateSelectorDialog = buildSelectorDialog();
                }
                if (mDateSelectorDialog != null) {
                    mDateSelectorDialog.show();
                }
                return true;
            }
        });
    }

    @Override
    public int menuResource() {
        return R.menu.fragment_schedule;
    }

    private void selectorPicked(int which) {
        String name = changeDate(which);
        updateDate();
        setEvents();
        if (mSelector != null) {
            mSelector.setTitle(name);
        }
    }

    private Dialog buildSelectorDialog() {
        if (getActivity() == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Date");

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
        return new SchedState(mDate, mLocation, mScrollView.getScrollY());
    }

    @Override
    public void onFirstState() {
        mLocation = 0;
        selectorPicked(0);
    }

    @Override
    public void restoreState(Object state) {
        final SchedState schedState = (SchedState) state;
        mLocation = ((SchedState) state).loc;
        selectorPicked(schedState.date);
        mPager.setCurrentItem(schedState.loc, false);
        updateDate();
        mScrollView.scrollTo(mScrollView.getScrollX(), schedState.scrollY);
    }

    @Override
    public void onFinished(List<CalendarEvent> events) {
        updateEvents(events);
    }

    @Override
    public void eventTapped(CalendarEvent event) {
        switch (event.getType()) {
            case CalendarEvent.TYPE_EVENT:
                openEvent(event);
                break;
            case CalendarEvent.TYPE_GUEST:
                openGuest(event);
                break;
        }
    }

    private void openGuest(CalendarEvent event) {
        if (getContext() == null) {
            return;
        }
        Intent intent = new Intent(getContext(), GuestActivity.class);
        intent.putExtra(EXTRA_GUEST_ID, event.getGuest().getId());
        getContext().startActivity(intent);
    }

    private void openEvent(CalendarEvent event) {
        if (getContext() == null) {
            return;
        }
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, event.getEvent().getId());
        getContext().startActivity(intent);
    }

    @Override
    public void starToggled() {
        mRecyclerView.invalidate();
    }

    class SchedState {
        int date;
        int loc;
        int scrollY;

        SchedState(int date, int loc, int scrollY) {
            this.date = date;
            this.loc = loc;
            this.scrollY = scrollY;
        }
    }
}
