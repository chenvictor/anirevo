package cvic.anirevo.handlers;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cvic.anirevo.R;
import cvic.anirevo.SettingsActivity;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.ui.AniRevoFragment;
import cvic.anirevo.ui.EventsFragment;
import cvic.anirevo.ui.GuestsFragment;
import cvic.anirevo.ui.ScheduleFragment;
import cvic.anirevo.ui.StarredFragment;

public class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener{

    public static final int AGE_CHANGED_REQUEST_CODE = 190;

    private FragmentStateHolderHandler mFragmentStateHolderHandler;

    private final NavigationView mNavigationView;
    private final AppCompatActivity mActivity;
    private final TabLayout mAppBarTabs;
    private final DrawerLayout mDrawer;

    private AniRevoFragment mCurrentFragment;

    private Class mFragClass = null;

    public NavigationHandler(AppCompatActivity activity, TabLayout appBarTabs) {
        this.mActivity = activity;
        mAppBarTabs = appBarTabs;
        mDrawer = mActivity.findViewById(R.id.drawer_layout);

        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mFragmentStateHolderHandler = new FragmentStateHolderHandler();

        mNavigationView = mActivity.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void start() {
        //start, and set the year
        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
        TextView subtitle = mNavigationView.getHeaderView(0).findViewById(R.id.header_subtitle);
        subtitle.setText(String.valueOf(DateManager.getInstance().getYear()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentFragment != null) {
            mActivity.getMenuInflater().inflate(mCurrentFragment.menuResource(), menu);
            mCurrentFragment.onMenuInflated(menu);
        }
        return true;
    }

    /**
     * @return  true if back press was handled, false otherwise
     */
    public boolean onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_events:
                fragmentClass = EventsFragment.class;
                break;
            case R.id.nav_guests:
                fragmentClass = GuestsFragment.class;
                break;
            case R.id.nav_starred:
                fragmentClass = StarredFragment.class;
                break;
            case R.id.nav_settings:
                Intent i = new Intent(mActivity, SettingsActivity.class);
                mActivity.startActivityForResult(i, AGE_CHANGED_REQUEST_CODE);
                return true;
            default:
                Toast.makeText(mActivity, "TODO", Toast.LENGTH_SHORT).show();
                break;
        }

        if (fragmentClass != null && (mFragClass == null || !mFragClass.equals(fragmentClass))) {
            mFragClass = fragmentClass;
            AniRevoFragment fragment;
            try {
                fragment = (AniRevoFragment) fragmentClass.newInstance();
                changeFragment(fragment);
                item.setChecked(true);
                mActivity.setTitle(item.getTitle());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return false;
            }
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;

    }

    // Helpers --------------

    private void changeFragment(AniRevoFragment fragment) {
        mCurrentFragment = fragment;

        fragment.setStateHandler(mFragmentStateHolderHandler);
        fragment.setAppBarTabs(mAppBarTabs);
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_ani_revo, fragment).commit();
        mActivity.supportInvalidateOptionsMenu();
    }
}
