package cvic.anirevo.model.calendar;

public class CalendarDate {

    private String name;

    public CalendarDate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
