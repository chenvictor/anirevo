package cvic.anirevo.model.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DateManager {

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
        CalendarDate newDate = new CalendarDate(dateString);
        dates.add(newDate);
        return newDate;
    }

    public String[] getSpinnerOptions() {
        String[] options = new String[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            options[i] = dates.get(i).getName();
            Log.i("temp", options[i]);
        }
        return options;
    }
}
