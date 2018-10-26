package cvic.anirevo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArCategory;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.CategoryManager;

import static cvic.anirevo.ui.ArEventAdapter.EXTRA_EVENT_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends AniRevoFragment {

    public static final String TAG = "cvic.anirevo.EVENTS";

    private RecyclerView mRecyclerView;
    private StickyHeaderAdapter<ArEvent> mAdapter;
    private List<ListItem<ArEvent>> mEvents;

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
        mEvents = new ArrayList<>();
        for (ArCategory category : CategoryManager.getInstance()) {
            mEvents.add(new ListItem<ArEvent>(category.getTitle()));
            for (ArEvent event : category) {
                mEvents.add(new ListItem<>(event));
            }
        }
        mAdapter = new StickyHeaderAdapter<ArEvent>(getContext(), mEvents) {

            @Override
            protected int headerLayout() {
                return R.layout.event_header_card_layout;
            }

            @Override
            public int itemLayout() {
                return R.layout.event_card_layout;
            }

            protected CardViewHolder createHeader(@NonNull ViewGroup parent) {
                CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(headerLayout(), parent, false);
                return new CardViewHolder(view);
            }

            protected CardViewHolder createItem(@NonNull ViewGroup parent) {
                CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(itemLayout(), parent, false);
                final CardViewHolder holder = new CardViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClicked(holder);
                    }
                });
                TextView star = view.findViewById(R.id.btn_star);
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        starClicked(holder);
                    }
                });
                return holder;
            }

            @Override
            public void bindItem(@NonNull CardView view, ArEvent item) {
                TextView name = view.findViewById(R.id.cal_event_title);
                TextView details = view.findViewById(R.id.cal_event_time);
                TextView desc = view.findViewById(R.id.event_card_description);
                TextView age = view.findViewById(R.id.event_card_age_restriction);
                TextView star = view.findViewById(R.id.btn_star);

                name.setText(item.getTitle());
                details.setText(item.getDetails());
                desc.setText(item.getDesc());
                if (item.isAgeRestricted()) {
                    AgeRestriction restriction = item.getRestriction();
                    age.setText(restriction.toString());
                    age.setBackgroundColor(restriction.getColor());
                    age.setVisibility(View.VISIBLE);
                } else {
                    age.setVisibility(View.INVISIBLE);
                }
                if (item.isStarred()) {
                    star.setText(mCtx.getString(R.string.star_filled));
                } else {
                    star.setText(mCtx.getString(R.string.star_empty));
                }
            }

            @Override
            public void bindHeader(@NonNull CardView card, ListItem header) {
                TextView text = card.findViewById(R.id.cal_event_title);
                text.setText(header.getHeaderText());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new StickyHeaderAdapter.HeaderItemDecoration(mRecyclerView, mAdapter));

        return view;
    }

    private void itemClicked(CardViewHolder holder) {
        ArEvent event = mEvents.get(holder.getAdapterPosition()).getItem();
        Intent intent = new Intent(getContext(), EventActivity.class);
        if (event != null) {
            intent.putExtra(EXTRA_EVENT_ID, event.getId());
            startActivity(intent);
        }
    }

    private void starClicked(CardViewHolder holder) {
        ArEvent event = mEvents.get(holder.getAdapterPosition()).getItem();
        event.toggleStarred();
        mAdapter.notifyItemChanged(holder.getAdapterPosition());
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
