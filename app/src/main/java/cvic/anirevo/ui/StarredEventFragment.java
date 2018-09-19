package cvic.anirevo.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.R;
import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.ArEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class StarredEventFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public StarredEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StarredGuestFragment.
     */
    public static StarredEventFragment newInstance() {
        StarredEventFragment fragment = new StarredEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<EventListItem> starredEvents = new ArrayList<>();
        for (ArEvent event : StarManager.getInstance().getStarredEvents()) {
            starredEvents.add(new EventListItem(event));
        }

        mAdapter = new ArEventAdapter(getContext(), starredEvents);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
