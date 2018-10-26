package cvic.anirevo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.Starrable;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.calendar.CalendarEvent;

import static cvic.anirevo.ui.ArEventAdapter.EXTRA_EVENT_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestEventsFragment extends Fragment {

    private RecyclerView mRecycler;
    private EventAdapter mAdapter;

    private List<CalendarEvent> events;

    public GuestEventsFragment() {
        // Required empty public constructor
        events = Collections.emptyList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_events, container, false);

        mRecycler = view.findViewById(R.id.recyclerview);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new EventAdapter();
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    public void setEvents(List<CalendarEvent> calendarEvents) {
        events = calendarEvents;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void eventClicked(CardViewHolder holder) {
        CalendarEvent event = events.get(holder.getAdapterPosition());
        switch (event.getType()) {
            case CalendarEvent.TYPE_EVENT:
                openEvent(event.getEvent());
                break;
        }
    }

    private void starClicked(CardViewHolder holder) {
        Starrable starrable = events.get(holder.getAdapterPosition()).getStarrable();
        starrable.toggleStarred();
    }

    private void openEvent(ArEvent event) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        if (event != null) {
            intent.putExtra(EXTRA_EVENT_ID, event.getId());
            startActivity(intent);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<CardViewHolder> {

        private EventAdapter() {

        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            CardView view = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cal_event_card_layout, viewGroup, false);
            TextView star = view.findViewById(R.id.btn_star);

            final CardViewHolder holder = new CardViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClicked(holder);
                }
            });
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starClicked(holder);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {
            CalendarEvent event = events.get(i);

            CardView view = cardViewHolder.getCardView();
            TextView title = view.findViewById(R.id.cal_event_title);
            TextView time = view.findViewById(R.id.cal_event_time);
            TextView location = view.findViewById(R.id.cal_event_location);
            TextView star = view.findViewById(R.id.btn_star);

            time.setText(getResources().getString(R.string.time_range, event.getStartTime().toString(), event.getEndTime().toString()));
            location.setText(event.getLocation().toString());
            if (event.getType() == CalendarEvent.TYPE_EVENT) {
                star.setVisibility(View.VISIBLE);
                star.setText(event.getStarrable().isStarred() ? getResources().getString(R.string.star_filled) : getResources().getString(R.string.star_empty));
                title.setText(event.getName());
            } else {
                star.setVisibility(View.GONE);
                title.setText(event.getLocation().getPurpose().toLowerCase().contains("autograph".toLowerCase()) ? "Autograph Session" : "Photobooth Session");
            }
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }

}
