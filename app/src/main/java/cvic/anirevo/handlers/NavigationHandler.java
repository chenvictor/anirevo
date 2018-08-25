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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import cvic.anirevo.BrowseGuestsFragment;
import cvic.anirevo.EventsFragment;
import cvic.anirevo.R;
import cvic.anirevo.ScheduleFragment;
import cvic.anirevo.SettingsFragment;
import cvic.anirevo.model.calendar.DateManager;

public class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener{

    private final AppCompatActivity mActivity;
    private final DrawerLayout mDrawer;
    private final int defaultMenuResource = R.menu.empty;

    private int menuToChoose = R.menu.empty;
    private Class currentFragClass = null;
    private Spinner schedSpinner;

    public NavigationHandler(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
        mDrawer = mActivity.findViewById(R.id.drawer_layout);

        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = mActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(menuToChoose, menu);
        if (menuToChoose == R.menu.activity_schedule) {
            setScheduleSpinnerOptions(menu);
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
        int newMenuResource = defaultMenuResource; //default menu

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                fragmentClass = ScheduleFragment.class;
                newMenuResource = R.menu.activity_schedule;
                break;
            case R.id.nav_events:
                fragmentClass = EventsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            case R.id.nav_guests:
                fragmentClass = BrowseGuestsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            default:
                Toast.makeText(mActivity, "TODO", Toast.LENGTH_SHORT).show();
                break;
        }

        if (currentFragClass == null || !currentFragClass.equals(fragmentClass)) {
            try {
                currentFragClass = fragmentClass;
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_ani_revo, fragment).commit();
                item.setChecked(true);
                mActivity.setTitle(item.getTitle());
                changeMenu(newMenuResource);
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

    private void setScheduleSpinnerOptions(Menu menu) {
        MenuItem item = menu.findItem(R.id.schedule_spinner_date);
        schedSpinner = (Spinner) item.getActionView();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.spinner_item, DateManager.getInstance().getSpinnerOptions());
        schedSpinner.setAdapter(adapter);
        schedSpinner.setLayoutMode(Spinner.MODE_DIALOG);
        schedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment frag = mActivity.getSupportFragmentManager().findFragmentById(R.id.content_ani_revo);
                if (frag instanceof ScheduleFragment) {
                    ((ScheduleFragment) frag).changeDate(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });
    }

    private void changeMenu(int menuRes) {
        menuToChoose = menuRes;
        mActivity.supportInvalidateOptionsMenu();
    }

}
