package cvic.anirevo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.handlers.NavigationHandler;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.CategoryManager;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.anirevo.TagManager;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.parser.EventParser;
import cvic.anirevo.parser.GuestParser;
import cvic.anirevo.parser.InfoParser;
import cvic.anirevo.parser.LocationParser;
import cvic.anirevo.utils.JSONUtils;
import cvic.anirevo.utils.TempUtils;

public class AniRevo extends AppCompatActivity implements SettingsFragment.SettingsFragmentInteractionListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    private NavigationHandler navHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_revo);

        init();

        //Load JSON
        loadJSONData(TempUtils.getAgeRestriction());

        initHandlers();
    }

    private void init() {
        CalendarEvent.setDefaultColor(getResources().getColor(R.color.calendarEventDefault));
        TempUtils.init(PreferenceManager.getDefaultSharedPreferences(this), getString(R.string.display_13_key), getString(R.string.display_18_key));
    }

    private void initHandlers() {
        navHandler = new NavigationHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return navHandler.onCreateOptionsMenu(menu);
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
            JSONObject info = JSONUtils.getObject(getApplicationContext(), "info.json");
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
            JSONArray locs = JSONUtils.getArray(getApplicationContext(),"locations.json");
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
            JSONArray guests = JSONUtils.getArray(getApplicationContext(),"guests.json");
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
            JSONArray events = JSONUtils.getArray(getApplicationContext(),"events.json");
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
        if (!navHandler.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void reloadJSON() {
        loadJSONData(TempUtils.getAgeRestriction(true));
    }
}
