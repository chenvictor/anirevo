package cvic.anirevo.handlers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.ui.BrowseGuestsFragment;
import cvic.anirevo.ui.EventsFragment;
import cvic.anirevo.ui.ScheduleFragment;
import cvic.anirevo.ui.SettingsFragment;
import cvic.anirevo.ui.StateHolderFragment;

public class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener{

    private static final int DEFAULT_MENU = R.menu.empty;

    private FragmentStateHolderHandler mFragmentStateHolderHandler;

    private final NavigationView mNavigationView;
    private final AppCompatActivity mActivity;
    private final DrawerLayout mDrawer;

    private int mMenuToChoose = R.menu.empty;
    private MenuHandler mMenuHandler;

    private Class mFragClass = null;

    public NavigationHandler(AppCompatActivity activity) {
        this.mActivity = activity;
        mDrawer = mActivity.findViewById(R.id.drawer_layout);

        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        mActivity.getMenuInflater().inflate(mMenuToChoose, menu);
        if (mMenuHandler != null) {
            mMenuHandler.onCreateOptionsMenu(menu);
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
        int newMenuResource = DEFAULT_MENU;

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                fragmentClass = ScheduleFragment.class;
                newMenuResource = R.menu.activity_schedule;
                break;
            case R.id.nav_events:
                fragmentClass = EventsFragment.class;
                break;
            case R.id.nav_guests:
                fragmentClass = BrowseGuestsFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                Toast.makeText(mActivity, "TODO", Toast.LENGTH_SHORT).show();
                break;
        }

        if (mFragClass == null || !mFragClass.equals(fragmentClass)) {
            try {
                mFragClass = fragmentClass;
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                if (fragment instanceof StateHolderFragment) {
                    ((StateHolderFragment) fragment).setStateHandler(mFragmentStateHolderHandler);
                }
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_ani_revo, fragment).commit();
                item.setChecked(true);
                mActivity.setTitle(item.getTitle());
                changeMenu(newMenuResource);
                if (newMenuResource == DEFAULT_MENU) {
                    removeMenuHandler();
                } else {
                    setMenuHandler((MenuHandler) fragment);
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    private void changeMenu(int menuRes) {
        mMenuToChoose = menuRes;
        mActivity.supportInvalidateOptionsMenu();
    }

    private void removeMenuHandler() {
        mMenuHandler = null;
    }

    private void setMenuHandler(MenuHandler menuHandler) {
        mMenuHandler = menuHandler;
    }

    /**
     * Fragments with menu items may implement this to handle menu related events
     */

    public interface MenuHandler {
        void onCreateOptionsMenu(Menu menu);
    }

}
