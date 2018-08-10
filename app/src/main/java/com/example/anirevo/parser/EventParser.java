package com.example.anirevo.parser;

import android.util.Log;

import com.example.anirevo.model.ArCategory;
import com.example.anirevo.model.ArEvent;
import com.example.anirevo.model.ArGuest;
import com.example.anirevo.model.CategoryManager;
import com.example.anirevo.model.EventManager;
import com.example.anirevo.model.GuestManager;
import com.example.anirevo.model.LocationManager;
import com.example.anirevo.model.TagManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventParser {

    private static final String TAG = "cvic.anirevo.EventParser";

    public static void parseEvents(JSONArray categories){
        //Clear EventManager
        EventManager.getInstance().clear();
        //
        Log.i(TAG, categories.length() + " event(s) to parse");
        for(int i = 0; i < categories.length(); i++) {
            try {
                parseCategory(categories.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit.");
            }
        }
    }

    private static void parseCategory(JSONObject cat) throws JSONException {
        String categoryTitle = cat.getString("category");
        JSONArray events = cat.getJSONArray("events");

        ArCategory category = CategoryManager.getInstance().getCategory(categoryTitle);

        for (int i = 0; i < events.length(); i++) {
            parseEvent(events.getJSONObject(i), category);
        }
    }

    private static void parseEvent(JSONObject event, ArCategory category) throws JSONException{
        String title = event.getString("title");
        String desc = event.getString("desc");
        String loc = event.getString("location");
        int startTime = event.getInt("start");
        int endTime = event.getInt("end");
        JSONArray guests = event.getJSONArray("guests");
        JSONArray tags = event.getJSONArray("tags");

        //Set basic properties
        ArEvent arEvent = EventManager.getInstance().getEvent(title);
        arEvent.setDesc(desc);
        arEvent.setLocation(LocationManager.getInstance().getLocation(loc));
        arEvent.setStart(startTime);
        arEvent.setEnd(endTime);

        //Establish mutual reference
        arEvent.setCategory(category);
        category.addEvent(arEvent);

        //Add guests
        GuestManager gManager = GuestManager.getInstance();
        for (int i = 0; i < guests.length(); i++) {
            ArGuest guest = gManager.getGuest(guests.getString(i));

            //Establish mutual reference
            guest.addEvent(arEvent);
            arEvent.addGuest(guest);
        }

        TagManager tManager = TagManager.getInstance();
        for (int i = 0; i< tags.length(); i++) {
            arEvent.addTag(tManager.getTag(tags.getString(i)));
        }
    }

}
