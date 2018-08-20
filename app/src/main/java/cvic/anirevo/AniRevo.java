package cvic.anirevo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.design.widget.NavigationView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.CategoryManager;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.anirevo.TagManager;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.parser.EventParser;
import cvic.anirevo.parser.GuestParser;
import cvic.anirevo.parser.InfoParser;
import cvic.anirevo.parser.LocationParser;

public class AniRevo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.SettingsFragmentInteractionListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    private Class currentFragClass = null;

    private int menuToChoose = R.menu.empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_revo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        //Load JSON
        loadJSONData(getAgeRestriction());

        //Load first fragment
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    private void init() {
        CalendarEvent.setDefaultColor(getResources().getColor(R.color.calendarEventDefault));
    }

    private AgeRestriction getAgeRestriction() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        AgeRestriction restriction = null;
        if (prefs.getBoolean(getString(R.string.display_13_key), true)) {
            restriction = AgeRestriction.AGE_RESTRICTION_13;
            if (prefs.getBoolean(getString(R.string.display_18_key), false)) {
                restriction = AgeRestriction.AGE_RESTRICTION_18;
            }
        }
        return restriction;
    }

    private void setScheduleSpinnerOptions(Menu menu) {
        MenuItem item = menu.findItem(R.id.schedule_spinner_date);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, DateManager.getInstance().getSpinnerOptions());
        spinner.setAdapter(adapter);
        spinner.setLayoutMode(Spinner.MODE_DIALOG);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.content_ani_revo);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menuToChoose, menu);
        if (menuToChoose == R.menu.activity_schedule) {
            setScheduleSpinnerOptions(menu);
        }
        return true;
    }

    private void changeMenu(int menuRes) {
        menuToChoose = menuRes;
        supportInvalidateOptionsMenu();
    }

    /**
     * Load all JSON files
     */
    private void loadJSONData(AgeRestriction restriction) {
        CategoryManager.getInstance().clear();
        TagManager.getInstance().clear();
        loadInfo();
        loadLocations();
        loadGuests();
        loadEvents(restriction);
    }

    private void loadInfo() {
        try {
            JSONObject info = new JSONObject(getJSONString(getApplicationContext(), "info.json"));
            Log.i(TAG, "Info JSON Loaded");
            InfoParser.parseInfo(info);
        } catch (JSONException e ) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            //Simple toast feedback
            Log.i(TAG, "JSON Errored");
        }
    }

    private void loadLocations() {
        LocationManager.getInstance().clear();
        try {
            JSONArray locs = new JSONArray(getJSONString(getApplicationContext(),"locations.json"));
            Log.i(TAG, "Locations JSON Loaded");
            LocationParser.parseLocs(locs);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            //Simple toast feedback
            Log.i(TAG, "JSON Errored");
        }
    }

    private void loadGuests() {
        GuestManager.getInstance().clear();
        try {
            JSONArray guests = new JSONArray(getJSONString(getApplicationContext(),"guests.json"));
            Log.i(TAG, "Guests JSON Loaded");
            GuestParser.parseGuests(guests);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            //Simple toast feedback
            Log.i(TAG, "JSON Errored");
        }
    }

    private void loadEvents(AgeRestriction restriction) {
        EventManager.getInstance().clear();
        try {
            JSONArray events = new JSONArray(getJSONString(getApplicationContext(),"events.json"));
            Log.i(TAG, "Events JSON Loaded");
            EventParser.parseEvents(events, restriction);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            //Simple toast feedback
            Log.i(TAG, "JSON Errored");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Class fragmentClass = null;
        int newMenuResource = R.menu.empty; //default menu

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                Log.i(TAG, "Schedule btn pressed");
                fragmentClass = ScheduleFragment.class;
                newMenuResource = R.menu.activity_schedule;
                break;
            case R.id.nav_events:
                Log.i(TAG, "Events btn pressed");
                fragmentClass = EventsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            case R.id.nav_guests:
                Log.i(TAG, "Guests btn pressed");
                fragmentClass = BrowseGuestsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            case R.id.nav_settings:
                Log.i(TAG, "Settings btn pressed");
                fragmentClass = SettingsFragment.class;
                newMenuResource = R.menu.empty;
                break;
            default:
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                break;
        }

        if (currentFragClass == null || !currentFragClass.equals(fragmentClass)) {
            try {
                currentFragClass = fragmentClass;
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_ani_revo, fragment).commit();
                item.setChecked(true);
                setTitle(item.getTitle());
                changeMenu(newMenuResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String getJSONString(Context context, String path) {
        String json;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i(TAG, "IOException");
            return null;
        }
        return json;
    }

    @Override
    public void reloadJSON(AgeRestriction restriction) {
        loadJSONData(restriction);
    }
}
