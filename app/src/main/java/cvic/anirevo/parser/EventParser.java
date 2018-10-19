package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.exceptions.RestrictedException;
import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArCategory;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.CategoryManager;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.anirevo.TagManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.model.calendar.EventTime;

public class EventParser {

    private static final String TAG = "anirevo.EventParser";

    public static void parseEvents(JSONArray categories, AgeRestriction restriction){
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
                //Log.i(TAG, "Skipping an AgeRestricted event.");
            }
        }
    }

    private static void parseEvent(JSONObject event, ArCategory category, AgeRestriction restriction) throws JSONException, RestrictedException {
        String title = event.getString("title");
        ArEvent arEvent = EventManager.getInstance().getEvent(title);

        //Check age restriction first in case this should be skipped
        if (event.has("age")) {
            //Set age restriction
            AgeRestriction restrict = AgeRestriction.getRestriction(event.getInt("age"));
            if (restrict != null) {
                if (restriction.restricts(restrict)) {
                    throw new RestrictedException();
                }
                arEvent.setRestriction(restrict);
            }
        }

        //Set basic properties
        String loc = event.getString("location");
        String desc = event.getString("desc");
        ArLocation arLocation = LocationManager.getInstance().getLocation(loc);
        arEvent.setDesc(desc);

        //Establish mutual reference with category
        arEvent.setCategory(category);
        category.addEvent(arEvent);

        //Add to StarManager if necessary
        StarManager sManager = StarManager.getInstance();
        if (sManager.isEventStarred(arEvent.getTitle())) {
            StarManager.getInstance().add(arEvent);
        }

        //Set CalendarEvents
        if (event.has("time")) {
            JSONArray timeblocks = event.getJSONArray("time");
            DateManager dManager = DateManager.getInstance();
            for (int i = 0; i < timeblocks.length(); i++) {
                JSONObject time = timeblocks.getJSONObject(i);
                CalendarDate date = dManager.getDate(time.getString("date"));
                EventTime start = EventTimeParser.parse(time.getString("start"));
                EventTime end = EventTimeParser.parse(time.getString("end"));
                CalendarEvent calEvent = new CalendarEvent(arEvent);
                calEvent.setLocation(arLocation);
                arLocation.addEvent(calEvent);
                calEvent.setDate(date);
                calEvent.setStart(start);
                calEvent.setEnd(end);
                date.addEvent(calEvent);
                arEvent.addTimeblock(calEvent);
            }
        }

        //Set Tags
        if (event.has("tags")) {
            TagManager tManager = TagManager.getInstance();
            JSONArray tags = event.getJSONArray("tags");
            for (int i = 0; i < tags.length(); i++) {
                arEvent.addTag(tManager.getTag(tags.getString(i)));
            }
        }

        //Set guests
        if (event.has("guests")) {
            GuestManager gManager = GuestManager.getInstance();
            JSONArray array = event.getJSONArray("guests");
            for (int i = 0; i < array.length(); i++) {
                ArGuest guest = gManager.getGuest(array.getString(i));
                arEvent.addGuest(guest);
                guest.addEvent(arEvent);
            }
        }
    }

}
