package cvic.anirevo.handlers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import cvic.anirevo.model.anirevo.CategoryManager;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.anirevo.TagManager;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.parser.EventParser;
import cvic.anirevo.parser.GuestParser;
import cvic.anirevo.parser.InfoParser;
import cvic.anirevo.parser.LocationParser;
import cvic.anirevo.parser.ViewingRoomParser;
import cvic.anirevo.utils.IOUtils;
import cvic.anirevo.utils.JSONUtils;
import cvic.anirevo.utils.TempUtils;

public class StorageHandler {

    private static final String TAG = "anirevo.Storage";
    private static final boolean DEBUG_MODE = false; //DEBUG MODE, forces StorageHandler to fetch fileStrings from asset folder every time

    private Context mContext;

    public StorageHandler(Context context) {
        mContext = context;
    }

    public void loadJSON() {
        TagManager.getInstance().clear();
        CategoryManager.getInstance().clear();
        EventManager.getInstance().clear();
        GuestManager.getInstance().clear();
        LocationManager.getInstance().clear();
        DateManager.getInstance().clear();
        loadInfo();
        loadLocations();
        loadGuests();
        loadEvents();
        loadViewingRooms();
    }

    private void loadInfo() {
        try {
            JSONObject info = new JSONObject(getFileString("json/info.json"));
            InfoParser.parseInfo(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadLocations() {
        try {
            JSONArray locs = new JSONArray(getFileString("json/locations.json"));
            LocationParser.parseLocs(locs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadGuests() {
        try {
            JSONArray guests = new JSONArray(getFileString("json/guests.json"));
            GuestParser.parseGuests(guests);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadEvents() {
        try {
            JSONArray events = new JSONArray(getFileString("json/events.json"));
            EventParser.parseEvents(events, TempUtils.getAgeRestriction());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadViewingRooms() {
        try {
            JSONArray viewRoomArray = new JSONArray(getFileString("json/viewing_rooms.json"));
            ViewingRoomParser.parseViewingRoom(viewRoomArray, TempUtils.getAgeRestriction());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a data file String, creating it from the default assets if it does not exist
     * @param path  path to check
     * @return      file contents as a String
     */
    private String getFileString(String path) {
        try {
            if (DEBUG_MODE) {
                //Quick was to force write from assets
                throw new FileNotFoundException();
            }
            return IOUtils.readFile(mContext, path);
        } catch (FileNotFoundException e) {
            //Read the file from assets
            Log.i(TAG, path + " not found. Writing from assets.");
            TempUtils.wipeLastUpdate();
            String asset = getAsset(path);
            IOUtils.writeFile(mContext, path, asset);
            return asset;
        }
    }

    /**
     * Fetch a asset file String
     * @param path  path to check
     * @return      file contents as a String
     */
    private String getAsset (String path) {
        return JSONUtils.getString(mContext, path);
    }

}
