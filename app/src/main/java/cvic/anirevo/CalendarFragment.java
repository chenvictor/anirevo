package cvic.anirevo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

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
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private static final String ARG_LOC_IDX = "AR_LOC_IDX";
    private static final String ARG_DATE_IDX = "AR_DATE_IDX";

    private CalendarDate mDate;
    private ArLocation mLocation;

    private ScrollView mScrollView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param locIdx     Index of the location in LocationManager.
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance(int locIdx, int dateIdx) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOC_IDX, locIdx);
        args.putInt(ARG_DATE_IDX, dateIdx);
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
            throw new UnsupportedOperationException("CalendarFragment requires a location and date index");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScrollView = getView().findViewById(R.id.calendar_scroller);

        CalendarDayView view = getView().findViewById(R.id.calendar_view);
        view.setLimitTime(mDate.getStartHour(), mDate.getEndHour());

        setEvents(view);
    }

    private void setEvents(CalendarDayView view) {
        List<CalendarEvent> events = new ArrayList<>();

        for (ArEvent arEvent : mLocation) {
            for (CalendarEvent calEvent : arEvent.getTimeblocks()) {
                if (calEvent.getDate().equals(mDate)) {
                    events.add(calEvent);
                }
            }
        }
        view.setEvents(events);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
