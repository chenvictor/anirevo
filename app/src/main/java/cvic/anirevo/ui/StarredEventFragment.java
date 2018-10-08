package cvic.anirevo.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private TextView mTextView;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emptyable_recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextView = view.findViewById(R.id.empty_text);
        mTextView.setText(R.string.empty_starred_events_prompt);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<EventListItem> starredEvents = new ArrayList<>();
        for (ArEvent event : StarManager.getInstance().getStarredEvents()) {
            starredEvents.add(new EventListItem(event));
        }
        mAdapter = new StarredEventAdapter(getContext(), starredEvents);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
        });
        checkEmpty();
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void checkEmpty() {
        boolean empty = mAdapter.getItemCount() == 0;
        mTextView.setVisibility(empty ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private class StarredEventAdapter extends ArEventAdapter {

        StarredEventAdapter(Context ctx, List<EventListItem> items) {
            super(ctx, items);
        }

        @Override
        protected void clickStar(CardViewHolder holder) {
            int i = holder.getAdapterPosition();
            ArEvent event = getEvent(i);
            if (!event.toggleStarred()) {
                items.remove(i);
                notifyItemRemoved(i);
            }
        }

    }

}
