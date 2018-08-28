package cvic.anirevo.model.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DateManager implements Iterable<CalendarDate>{

    private static DateManager instance;

    private List<CalendarDate> dates;

    public static DateManager getInstance() {
        if (instance == null ){
            instance = new DateManager();
        }
        return instance;
    }

    private DateManager() {
        dates = new ArrayList<>();
    }

    public CalendarDate getDate(int index) {
        return dates.get(index);
    }

    public CalendarDate getDate(String dateString) {
        for (CalendarDate date : dates) {
            if (date.getName().equals(dateString)) {
                return date;
            }
        }
        CalendarDate newDate = new CalendarDate(dateString, dates.size());
        dates.add(newDate);
        return newDate;
    }

    @NonNull
    @Override
    public Iterator<CalendarDate> iterator() {
        return dates.iterator();
    }

    public void clear() {
        dates.clear();
    }

}
