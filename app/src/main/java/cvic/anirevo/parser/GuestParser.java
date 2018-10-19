package cvic.anirevo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.EventManager;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;
import cvic.anirevo.model.calendar.EventTime;

public class GuestParser {

    private static final String TAG = "anirevo.GuestParser";

    public static void parseGuests(JSONArray guests) {
        //Clear GuestManager
        GuestManager.getInstance().clear();
        //
        Log.i(TAG, guests.length() + " guests(s) to parse");
        for(int i = 0; i < guests.length(); i++) {
            try {
                parseGuest(guests.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "JSONError hit.");
            }
        }
    }

    private static void parseGuest(JSONObject guest) throws JSONException {
        String name = guest.getString("name");
        String title = guest.getString("title");
        ArGuest arGuest = GuestManager.getInstance().getGuest(name);
        arGuest.setTitle(title);

        //Add to StarManager if necessary
        StarManager sManager = StarManager.getInstance();
        if (sManager.isGuestStarred(arGuest.getName())) {
            sManager.add(arGuest);
        }

        if (guest.has("japanese")) {
            String japanese = guest.getString("japanese");
            arGuest.setJapanese(japanese);
        }
        if (guest.has("image")) {
            String image = guest.getString("image");
            arGuest.setPortraitPath(image);
        }
        if (guest.has("autographs")) {
            parseGuestEvents(arGuest, guest.getJSONArray("autographs"), arGuest.getName() + "Autograph Sessions");
        }
        if (guest.has("photobooth")) {
            parseGuestEvents(arGuest, guest.getJSONArray("photobooth"), arGuest.getName() + "Photobooth Sessions");
        }
    }

    private static void parseGuestEvents (ArGuest guest, JSONArray events, String eventName) {
        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject eventObject = events.getJSONObject(i);
                CalendarEvent event = new CalendarEvent(guest);
                CalendarDate date = DateManager.getInstance().getDate(eventObject.getString("date"));
                ArLocation location = LocationManager.getInstance().getLocation(eventObject.getString("location"));
                EventTime start = EventTimeParser.parse(eventObject.getString("start"));
                EventTime end = EventTimeParser.parse(eventObject.getString("end"));

                event.setStart(start);
                event.setEnd(end);

                ArEvent baseEvent = EventManager.getInstance().getEvent(eventName);
                baseEvent.addGuest(guest);
                guest.addEvent(baseEvent);
                event.setDate(date);
                date.addEvent(event);
                event.setLocation(location);
                location.addEvent(event);
                baseEvent.addTimeblock(event);
            } catch (JSONException e) {
                Log.i(TAG, "JSON error hit, skipping Guest Event");
                e.printStackTrace();
            }
        }
    }

}
