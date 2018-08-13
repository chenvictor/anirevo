package com.example.anirevo.parser;

import android.util.Log;

import com.example.anirevo.model.AgeRestriction;
import com.example.anirevo.model.ArCategory;
import com.example.anirevo.model.ArEvent;
import com.example.anirevo.model.CategoryManager;
import com.example.anirevo.model.EventManager;
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
        String date = event.getString("date");
        String start = event.getString("start");
        String end = event.getString("end");
        String loc = event.getString("location");
        String desc = event.getString("desc");
        JSONArray tags = event.getJSONArray("tags");

        //Set basic properties
        ArEvent arEvent = EventManager.getInstance().getEvent(title);
        arEvent.setDate(date);
        arEvent.setStart(start);
        arEvent.setEnd(end);
        arEvent.setLocation(LocationManager.getInstance().getLocation(loc));
        arEvent.setDesc(desc);

        if (event.has("age")) {
            //Set age restriction
            int ageRestrict = event.getInt("age");
            AgeRestriction restrict = null;
            switch(ageRestrict) {
                case 13:
                    restrict = AgeRestriction.AGE_RESTRICTION_13;
                    break;
                case 18:
                    restrict = AgeRestriction.AGE_RESTRICTION_18;
                    break;
                default:
                    break;
            }
            if (restrict != null) {
                arEvent.setRestriction(restrict);
            }
        }

        //Establish mutual reference
        arEvent.setCategory(category);
        category.addEvent(arEvent);

        TagManager tManager = TagManager.getInstance();
        for (int i = 0; i< tags.length(); i++) {
            arEvent.addTag(tManager.getTag(tags.getString(i)));
        }
    }

}
