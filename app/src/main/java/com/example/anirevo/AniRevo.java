package com.example.anirevo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anirevo.model.CategoryManager;
import com.example.anirevo.model.EventManager;
import com.example.anirevo.model.GuestManager;
import com.example.anirevo.model.LocationManager;
import com.example.anirevo.model.TagManager;
import com.example.anirevo.parser.EventParser;
import com.example.anirevo.parser.GuestParser;
import com.example.anirevo.parser.LocationParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class AniRevo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    private int menuToChoose = R.menu.ani_revo;

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

        loadJSONData();

        //Load first fragment
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }



    private void setScheduleSpinnerOptions(Menu menu) {
        MenuItem item = menu.findItem(R.id.schedule_spinner_date);
        Spinner spinner = (Spinner) item.getActionView();
        String[] tempSpinnerOptions = {"Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, tempSpinnerOptions);
        spinner.setAdapter(adapter);
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
    private void loadJSONData() {
        CategoryManager.getInstance().clear();
        TagManager.getInstance().clear();
        loadLocations();
        loadGuests();
        loadEvents();
    }

    private void loadLocations() {
        LocationManager.getInstance().clear();
        try {
            JSONArray locs = new JSONArray(getJSONString(getApplicationContext(),"locations.json"));
            Log.i(TAG, "JSON Loaded");
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
            Log.i(TAG, "JSON Loaded");
            GuestParser.parseGuests(guests);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            //Simple toast feedback
            Log.i(TAG, "JSON Errored");
        }
    }

    private void loadEvents() {
        EventManager.getInstance().clear();
        try {
            JSONArray events = new JSONArray(getJSONString(getApplicationContext(),"events.json"));
            Log.i(TAG, "JSON Loaded");
            EventParser.parseEvents(events);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if (!item.isChecked()) {
            Class fragmentClass = null;
            int newMenuResource = R.menu.ani_revo; //default menu

            switch (item.getItemId()) {
                case R.id.nav_schedule:
                    Log.i(TAG, "Schedule btn pressed");
                    fragmentClass = ScheduleFragment.class;
                    newMenuResource = R.menu.activity_schedule;
                    break;
                case R.id.nav_events:
                    Log.i(TAG, "Events btn pressed");
                    fragmentClass = EventsFragment.class;
                    newMenuResource = R.menu.activity_events;
                    break;
                case R.id.nav_guests:
                    Log.i(TAG, "Guests btn pressed");
                    fragmentClass = BrowseGuestsFragment.class;
                    newMenuResource = R.menu.activity_browse_guests;
                    break;
                case R.id.nav_settings:
                    Log.i(TAG, "Settings btn pressed");
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                    break;
            }

            try {
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_ani_revo, fragment).commitNow();
                item.setChecked(true);
                setTitle(item.getTitle());
                changeMenu(newMenuResource);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String getJSONString(Context context, String path) {
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

}
