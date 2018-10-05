package cvic.anirevo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.handlers.NavigationHandler;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.tasks.FetchScheduleEventsTask;

import static cvic.anirevo.ui.ArEventAdapter.EXTRA_EVENT_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends CustomFragment implements NavigationHandler.MenuHandler, FetchScheduleEventsTask.FetchScheduleEventsTaskListener, ScheduleFragmentHitboxHandler.EventTappedListener{

    private final static String TAG = "anirevo.SchedFrag";

    private MenuItem mSelector;
    private Dialog mDateSelectorDialog;

    private ViewPager mPager;
    private NestedScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private DayViewAdapter mAdapter;

    private List<ArLocation> mLocations;

    private int mDate = 0;
    private int mLocation = 0;

    private int mStartHour = 0;

    private ScheduleFragmentHitboxHandler mHitboxHandler;

    public ScheduleFragment() {
        super("SCHEDULE");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        //get views
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
        updateDate();
    }

    @Override
    protected void handleAppBar() {
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
        for (CalendarEvent event : events) {
            mRecyclerView.addItemDecoration(new EventDecoration(mHitboxHandler, event, mStartHour));
        }
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
        if (idx == mDate) {
            //no change
            return null;
        }
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
                if (mDateSelectorDialog != null) {
                    mDateSelectorDialog.show();
                }
                return true;
            }
        });
    }

    private void selectorPicked(int which) {
        String name = changeDate(which);
        if (name != null && mSelector != null) {
            mSelector.setTitle(name);
        }
        updateDate();
        setEvents();
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
        setEvents();
    }

    @Override
    public void restoreState(Object state) {
        final SchedState schedState = (SchedState) state;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectorPicked(schedState.date);
                TabLayout.Tab  tab = mAppBarTabs.getTabAt(schedState.loc);
                if (tab != null) {
                    tab.select();
                }
                updateDate();
                mScrollView.scrollTo(mScrollView.getScrollX(), schedState.scrollY);
            }
        }, 100);
    }

    @Override
    public void onFinished(List<CalendarEvent> events) {
        updateEvents(events);
    }

    @Override
    public void eventTapped(CalendarEvent event) {
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
