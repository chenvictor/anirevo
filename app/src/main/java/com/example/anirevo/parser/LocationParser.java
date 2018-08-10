package com.example.anirevo.parser;

import android.util.Log;

import com.example.anirevo.model.ArLocation;
import com.example.anirevo.model.LocationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationParser {

    private static final String TAG = "cvic.anirevo.LocationParser";

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
