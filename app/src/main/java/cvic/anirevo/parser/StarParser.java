package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.model.StarManager;

public class StarParser {

    private static final String TAG = "anirevo.StarParser";

    public static void parseStars(JSONObject stars) {
        if (stars.has("guests")) {
            try {
                parseGuests(stars.getJSONArray("guests"));
            } catch (JSONException e) {
                Log.i(TAG, "Something wrong with guests");
            }
        }
        if (stars.has("events")) {
            try {
                parseEvents(stars.getJSONArray("events"));
            } catch (JSONException e) {
                Log.i(TAG, "Something wrong with events");
            }
        }
    }

    private static void parseGuests(JSONArray array) throws JSONException{
        Log.i(TAG, "Parsing guests");
        StarManager manager = StarManager.getInstance();
        for (int i = 0; i < array.length(); i++) {
            Log.i(TAG, "Added: " + array.getString(i));
            manager.addGuest(array.getString(i));
        }
    }

    private static void parseEvents(JSONArray array) throws JSONException{
        Log.i(TAG, "Parsing events");
        StarManager manager = StarManager.getInstance();
        for (int i = 0; i < array.length(); i++) {
            Log.i(TAG, "Added: " + array.getString(i));
            manager.addEvent(array.getString(i));
        }
    }

}
