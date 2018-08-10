package com.example.anirevo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.example.anirevo.parser.EventParser;
import com.example.anirevo.parser.GuestParser;
import com.example.anirevo.parser.LocationParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class AniRevo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_revo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadJSONData();

        //Load first fragment
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    /**
     * Load all JSON files
     */
    private void loadJSONData() {
        loadLocations();
        loadGuests();
        loadEvents();
    }

    private void loadLocations() {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ani_revo, menu);
        return true;
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

        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                Log.i(TAG, "Schedule btn pressed");
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_events:
                Log.i(TAG, "Events btn pressed");
                fragmentClass = EventsFragment.class;
                break;
            case R.id.nav_guests:
                Log.i(TAG, "Guests btn pressed");
                fragmentClass = BrowseGuestsFragment.class;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;
    }

    public void onLoaded() {
        //Close drawer once fragment is loaded

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String getJSONString(Context context, String path) {
        String json = null;
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
