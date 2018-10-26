package cvic.anirevo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cvic.anirevo.exceptions.InvalidIdException;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.ui.ArGuestAdapter;
import cvic.anirevo.ui.GuestEventsFragment;
import cvic.anirevo.utils.IOUtils;

public class GuestActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    private static final String TAG = "anirevo.guestAct";

    private ArGuest mGuest;
    private MenuItem mStar;

    private ImageView mImage;
    private TextView mName;
    private TextView mJapanese;

    private TextView mEventsHeader;
    private TabLayout mEventsTabs;
    private ViewPager mEventsPager;
    private SparseArrayCompat<List<CalendarEvent>> mEventsMap;
    private ArrayList<Integer> mDatesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        Intent intent = getIntent();
        try {
            mGuest = GuestManager.getInstance().getGuest(intent.getIntExtra(ArGuestAdapter.EXTRA_GUEST_ID, 0));
        } catch (InvalidIdException e) {
            //Make toast
            Toast.makeText(getApplicationContext(), "Invalid Guest ID Provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mEventsMap = new SparseArrayCompat<>();
        mDatesMap = new ArrayList<>();

        mImage = findViewById(R.id.guest_portrait);
        mName = findViewById(R.id.guest_name);
        mJapanese = findViewById(R.id.guest_japanese_name);
        mEventsHeader = findViewById(R.id.events_header);
        mEventsTabs = findViewById(R.id.events_tabs);
        mEventsPager = findViewById(R.id.events_pager);
        init();
    }

    private void init() {
        String portraitPath = mGuest.getPortraitPath();
        if (portraitPath != null) {
            try {
                Bitmap bm = IOUtils.getBitmap(getApplicationContext(), "images/" + portraitPath);
                mImage.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                mImage.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_portrait));
            }
        } else {
            mImage.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_portrait));
        }
        mName.setText(mGuest.getName());
        if (mGuest.hasJapanese()) {
            mJapanese.setText(mGuest.getJapanese());
            mJapanese.setVisibility(View.VISIBLE);
        } else {
            mJapanese.setVisibility(View.GONE);
        }
        initMap();
        if (!mDatesMap.isEmpty()) {
            setEventsVisibility(View.VISIBLE);
            mEventsTabs.setupWithViewPager(mEventsPager);
            mEventsPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    GuestEventsFragment frag = new GuestEventsFragment();
                    frag.setEvents(mEventsMap.get(mDatesMap.get(i)));
                    return frag;
                }

                @Override
                public int getCount() {
                    return mEventsMap.size();
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int i) {
                    return DateManager.getInstance().getDate(mDatesMap.get(i)).getName();
                }
            });
        } else {
            setEventsVisibility(View.GONE);
        }
    }

    private void setEventsVisibility(int visibility) {
        mEventsHeader.setVisibility(visibility);
        mEventsTabs.setVisibility(visibility);
        mEventsPager.setVisibility(visibility);
    }

    private void initMap() {
        for (CalendarDate date : DateManager.getInstance()) {
            List<CalendarEvent> events = getCalEvents(date);
            if (events.size() != 0) {
                mEventsMap.put(date.getId(), events);
                mDatesMap.add(date.getId());
            }
        }
    }

    private List<CalendarEvent> getCalEvents(CalendarDate date) {
        List<CalendarEvent> events = new ArrayList<>();
        for (CalendarEvent event : date) {
            switch (event.getType()) {
                case CalendarEvent.TYPE_EVENT:
                    if (event.getEvent().getGuests().contains(mGuest)) {
                        events.add(event);
                    }
                    break;
                case CalendarEvent.TYPE_GUEST:
                    if (event.getGuest().equals(mGuest)) {
                        events.add(event);
                    }
            }
        }
        Collections.sort(events, new Comparator<CalendarEvent>() {
            @Override
            public int compare(CalendarEvent event1, CalendarEvent event2) {
                if (event1.getStartTime().toInt() < event2.getStartTime().toInt()) {
                    return -1;
                }
                if (event2.getStartTime().toInt() < event1.getStartTime().toInt()) {
                    return 1;
                }
                return Integer.compare(event1.getEndTime().toInt(), event2.getEndTime().toInt());
            }
        });
        return events;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_star, menu);
        mStar = menu.findItem(R.id.star);
        mStar.setOnMenuItemClickListener(this);
        setStarTitle();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.equals(mStar)) {
            mGuest.toggleStarred();
            setStarTitle();
            return true;
        }
        return false;
    }

    private void setStarTitle() {
        SpannableString string = new SpannableString(mGuest.isStarred() ? getResources().getString(R.string.star_filled) : getResources().getString(R.string.star_empty));
        string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStar)), 0, string.length(), 0);
        string.setSpan(new AbsoluteSizeSpan(30), 0, string.length(), 0);
        mStar.setTitle(string);
        mStar.setTitleCondensed(string);
    }

}
