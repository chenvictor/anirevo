package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;

public class LocationParser {

    private static final String TAG = "anirevo.LocParser";

    /**
     * Parse the locations JSONArray, into the LocationManager
     * @param locs              JSONArray to parse
     */
    public static void parseLocs(JSONArray locs){
        //Clear LocationManager
        LocationManager.getInstance().clear();
        //
        Log.i(TAG, locs.length() + " location(s) to parse");
        for(int i = 0; i < locs.length(); i++) {
            try {
                parseLoc(locs.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit.");
            }
        }
    }

    /**
     * Parse a location, adding it to the LocationManager
     * @param loc               location to add
     * @throws JSONException    ---
     */
    private static void parseLoc(JSONObject loc) throws JSONException{
        String title = loc.getString("title");
        String subtitle = loc.getString("subtitle");
        ArLocation arLoc = LocationManager.getInstance().getLocation(title);
        arLoc.setSubtitle(subtitle);
    }

}
