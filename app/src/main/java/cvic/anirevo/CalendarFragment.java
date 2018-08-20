package cvic.anirevo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.model.anirevo.LocationManager;
import cvic.anirevo.model.calendar.CalendarDate;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.DateManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements ScheduleFragment.ScheduleInteractionListener{

    private static final String ARG_LOC_IDX = "AR_LOC_IDX";
    private static final String ARG_DATE_IDX = "AR_DATE_IDX";

    private CalendarDate mDate;
    private ArLocation mLocation;

    private CalendarDayView mView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOC_IDX, 0);
        args.putInt(ARG_DATE_IDX, 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = LocationManager.getInstance().getLocation(getArguments().getInt(ARG_LOC_IDX));
            mDate = DateManager.getInstance().getDate(getArguments().getInt(ARG_DATE_IDX));
        } else {
            mLocation = LocationManager.getInstance().getLocation(0);
            mDate = DateManager.getInstance().getDate(0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView = getView().findViewById(R.id.calendar_view);
        mView.setLimitTime(mDate.getStartHour(), mDate.getEndHour());

        setEvents();
    }

    private void setEvents() {
        List<CalendarEvent> events = new ArrayList<>();

        for (ArEvent arEvent : mLocation) {
            for (CalendarEvent calEvent : arEvent.getTimeblocks()) {
                if (calEvent.getDate().equals(mDate)) {
                    events.add(calEvent);
                }
            }
        }
        mView.setEvents(events);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    //ScheduleFragment interaction methods

    @Override
    public void changeDate(int idx) {
        mDate = DateManager.getInstance().getDate(idx);
        mView.setLimitTime(mDate.getStartHour(), mDate.getEndHour());
        setEvents();
    }

    @Override
    public void changeLocation(int idx) {
        mLocation = LocationManager.getInstance().getLocation(idx);
        setEvents();
    }
}
