package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.model.map.MapVenue;
import cvic.anirevo.model.map.VenueManager;

public class MapParser {

    public static final String TAG = "anirevo.mapparser";

    public static void parse(JSONArray venues) {
        Log.i(TAG, "Parsing " + venues.length() + " venue(s)");
        for (int i = 0; i < venues.length(); i++) {
            try {
                parseVenue(venues.getJSONObject(i));
            } catch (JSONException e) {
                Log.i(TAG, "JSON error hit, skipping venue");
            }
        }
    }

    private static void parseVenue(JSONObject venue) throws JSONException {
        MapVenue mapVenue = VenueManager.getInstance().getVenue(venue.getString("name"));
        mapVenue.setImagePath(venue.getString("image"));
    }

}
