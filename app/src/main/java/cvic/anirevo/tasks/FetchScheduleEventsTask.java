package cvic.anirevo.tasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;

/**
 * Task will return CalendarEvents for the given date and location
 */
public class FetchScheduleEventsTask extends AsyncTask <Integer, Void, List<CalendarEvent>>{

    private FetchScheduleEventsTaskListener mListener;

    public FetchScheduleEventsTask(FetchScheduleEventsTaskListener mListener) {
        this.mListener = mListener;
    }

    /**
     * Do in background
     * @param integers  2 integers. The first is the location id, the second is the date id
     * @return      the list of calendarevents for the given location and date
     */
    @Override
    protected List<CalendarEvent> doInBackground(Integer... integers) {
        if (integers.length != 2) {
            cancel(true);
        }
        ArLocation location = LocationManager.getInstance().getLocation(integers[0]);
        CalendarDate date = DateManager.getInstance().getDate(integers[1]);

        List<CalendarEvent> events = new ArrayList<>();

        for (CalendarEvent calEvent : location) {
            if (calEvent.getDate().equals(date)) {
                events.add(calEvent);
            }
        }

        return events;
    }

    @Override
    protected void onPostExecute(List<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);
        mListener.onFinished(calendarEvents);
    }

    public interface FetchScheduleEventsTaskListener {
        void onFinished(List<CalendarEvent> events);
    }

}
