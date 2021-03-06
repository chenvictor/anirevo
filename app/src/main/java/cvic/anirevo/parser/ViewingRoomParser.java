package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.exceptions.InvalidTimeException;
import cvic.anirevo.exceptions.RestrictedException;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.model.calendar.EventTime;

public class ViewingRoomParser {

    private static final String TAG = "anirevo.VRParser";

    public static void parseViewingRoom(JSONArray array, AgeRestriction restriction) {
        Log.i(TAG, array.length() + " day(s) to parse");
        for (int i = 0; i < array.length(); i++) {
            try {
                parseDay(array.getJSONObject(i), restriction);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit, skipping day");
            }
        }
    }

    private static void parseDay(JSONObject day, AgeRestriction restriction) throws JSONException {
        CalendarDate date = DateManager.getInstance().getDate(day.getString("day"));
        EventTime start = EventTimeParser.parse(day.getString("start"));
        if (start == null) {
            Log.i(TAG, "JSON Missing 'start' value, skipping room.");
            return;
        }
        JSONArray rooms = day.getJSONArray("rooms");
        for (int i = 0; i < rooms.length(); i++) {
            try {
                parseRoom(rooms.getJSONObject(i), date, start.getHour(), restriction);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit, skipping room");
            }
        }
    }

    private static void parseRoom(JSONObject room, CalendarDate date, int startHour, AgeRestriction restriction) throws JSONException {
        ArLocation roomLoc = LocationManager.getInstance().getLocation(room.getString("purpose"));
        JSONArray shows = room.getJSONArray("shows");
        for (int i = 0; i < shows.length(); i++) {
            try {
                parseShow(shows.getJSONObject(i), date, startHour + i, roomLoc, restriction);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit, skipping show");
            } catch (RestrictedException e) {
                Log.i(TAG,"Skipped a restricted show");
            }
        }
    }

    private static void parseShow(JSONObject show, CalendarDate date, int hour, ArLocation location, AgeRestriction restriction) throws JSONException, RestrictedException {
        String title = show.getString("title");
        ArEvent arEvent = EventManager.getInstance().getEvent(title);
        if (show.has("age")) {
            //Set age restriction
            AgeRestriction restrict = AgeRestriction.getRestriction(show.getInt("age"));
            if (restrict != null) {
                if (restriction.restricts(restrict)) {
                    throw new RestrictedException();
                }
                arEvent.setRestriction(restrict);
            }
        }
        if (show.has("desc")) {
            arEvent.setDesc(show.getString("desc"));
        }
        //Create CalendarEvent
        CalendarEvent calEvent = new CalendarEvent(arEvent);
        calEvent.setDate(date);
        calEvent.setLocation(location);
        try {
            calEvent.setStart(new EventTime(hour, 0));
            calEvent.setEnd(new EventTime(hour + 1, 0));
        } catch (InvalidTimeException e) {
            e.printStackTrace();
            Log.i(TAG, "Invalid time! " + hour + " - " + hour + 1);
        }
        arEvent.addTimeblock(calEvent);
    }

}
