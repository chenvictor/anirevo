package cvic.anirevo.model.calendar;

import java.util.ArrayList;
import java.util.List;

public class DateManager {

    private static DateManager instance;

    private List<CalendarDate> days;

    public static DateManager getInstance() {
        if (instance == null ){
            instance = new DateManager();
        }
        return instance;
    }

    private DateManager() {
        days = new ArrayList<>();
    }

    public CalendarDate getDay(String dayString) {
        for (CalendarDate day : days) {
            if (day.getName().equals(dayString)) {
                return day;
            }
        }
        CalendarDate newDay = new CalendarDate(dayString);
        days.add(newDay);
        return newDay;
    }
}
