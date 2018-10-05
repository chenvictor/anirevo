package cvic.anirevo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.ArCategory;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.CategoryManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends CustomFragment {

    public static final String TAG = "cvic.anirevo.EVENTS";

    private RecyclerView mRecyclerView;

    public EventsFragment() {
        super("EVENTS");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<EventListItem> dataset = new ArrayList<>();
        for (ArCategory category : CategoryManager.getInstance()) {
            dataset.add(new EventListItem(category.getTitle(), Color.BLACK));
            for (ArEvent event : category) {
                dataset.add(new EventListItem(event));
            }
        }
        ArStickyHeaderEventAdapter mAdapter = new ArStickyHeaderEventAdapter(getContext(), dataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ArStickyHeaderEventAdapter.HeaderItemDecoration(mRecyclerView, mAdapter));

        return view;
    }

    @Override
    public Object storeState() {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null)
            return null;
        return mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void restoreState(Object state) {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null)
            return;
        mRecyclerView.getLayoutManager().onRestoreInstanceState((Parcelable) state);
    }

}
