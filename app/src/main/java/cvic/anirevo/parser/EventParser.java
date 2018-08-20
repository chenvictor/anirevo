package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.exceptions.RestrictedException;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArCategory;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.CategoryManager;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.anirevo.TagManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.model.calendar.EventTime;

public class EventParser {

    private static final String TAG = "anirevo.EventParser";

    public static void parseEvents(JSONArray categories, AgeRestriction restriction){
        //Clear EventManager
        EventManager.getInstance().clear();
        //
        Log.i(TAG, categories.length() + " categories(s) to parse");
        for(int i = 0; i < categories.length(); i++) {
            try {
                parseCategory(categories.getJSONObject(i), restriction);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit, skipping category");
            }
        }
    }

    private static void parseCategory(JSONObject cat, AgeRestriction restriction) throws JSONException{
        String categoryTitle = cat.getString("category");
        JSONArray events = cat.getJSONArray("events");

        ArCategory category = CategoryManager.getInstance().getCategory(categoryTitle);

        for (int i = 0; i < events.length(); i++) {
            try {
                parseEvent(events.getJSONObject(i), category, restriction);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit, skipping event.");
            } catch (RestrictedException e) {
                Log.i(TAG, "Skipping an AgeRestricted event.");
            }
        }
    }

    private static void parseEvent(JSONObject event, ArCategory category, AgeRestriction restriction) throws JSONException, RestrictedException {
        String title = event.getString("title");
        ArEvent arEvent = EventManager.getInstance().getEvent(title);


        //Check age restriction first in case this should be skipped
        if (event.has("age")) {
            //Set age restriction
            int ageRestrict = event.getInt("age");
            AgeRestriction restrict = null;
            switch(ageRestrict) {
                case 13:
                    if (restriction == null) {
                        throw new RestrictedException();
                    }
                    restrict = AgeRestriction.AGE_RESTRICTION_13;
                    break;
                case 18:
                    if (restriction == null || restriction == AgeRestriction.AGE_RESTRICTION_13) {
                        throw new RestrictedException();
                    }
                    restrict = AgeRestriction.AGE_RESTRICTION_18;
                    break;
                default:
                    break;
            }
            if (restrict != null) {
                arEvent.setRestriction(restrict);
            }
        }

        String loc = event.getString("location");
        String desc = event.getString("desc");
        JSONArray timeblocks = event.getJSONArray("time");
        JSONArray tags = event.getJSONArray("tags");

        //Set basic properties
        arEvent.setLocation(LocationManager.getInstance().getLocation(loc));
        arEvent.setDesc(desc);

        //Establish mutual reference
        arEvent.setCategory(category);
        category.addEvent(arEvent);

        //Set CalendarEvents
        DateManager dManager = DateManager.getInstance();
        for (int i = 0; i < timeblocks.length(); i++) {
            JSONObject time = timeblocks.getJSONObject(i);
            CalendarDate date = dManager.getDate(time.getString("date"));
            EventTime start = EventTimeParser.parse(time.getString("start"));
            EventTime end = EventTimeParser.parse(time.getString("end"));
            CalendarEvent calEvent = new CalendarEvent(arEvent);
            calEvent.setDate(date);
            calEvent.setStart(start);
            calEvent.setEnd(end);
            date.addEvent(calEvent);
            arEvent.addTimeblock(calEvent);
        }

        //Set Tags
        TagManager tManager = TagManager.getInstance();
        for (int i = 0; i < tags.length(); i++) {
            arEvent.addTag(tManager.getTag(tags.getString(i)));
        }
    }

}
