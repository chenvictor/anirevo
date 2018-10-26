package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;

public class ArEventAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public static final String EXTRA_EVENT_ID = "cvic.anirevo.EXTRA_EVENT_ID";

    private Context mCtx;
    List<ListItem<ArEvent>> items;

    // Provide a suitable constructor (depends on the kind of dataset)
    ArEventAdapter(Context ctx, List<ListItem<ArEvent>> items) {
        this.items = items;
        mCtx = ctx;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_card_layout, parent, false);
        TextView star = view.findViewById(R.id.btn_star);
        final CardViewHolder holder = new CardViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEvent(holder);
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStar(holder);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        ListItem<ArEvent> item = items.get(position);
        CardView view = holder.getCardView();
        TextView name = view.findViewById(R.id.cal_event_title);
        TextView details = view.findViewById(R.id.cal_event_time);
        TextView desc = view.findViewById(R.id.event_card_description);
        TextView age = view.findViewById(R.id.event_card_age_restriction);
        TextView star = view.findViewById(R.id.btn_star);

        final ArEvent event = item.getItem();
        name.setText(event.getTitle());
        details.setText(event.getDetails());
        desc.setText(event.getDesc());
        if (event.isAgeRestricted()) {
            AgeRestriction restriction = event.getRestriction();
            age.setText(restriction.toString());
            age.setBackgroundColor(restriction.getColor());
            age.setVisibility(View.VISIBLE);
        } else {
            age.setVisibility(View.INVISIBLE);
        }
        if (event.isStarred()) {
            star.setText(mCtx.getString(R.string.star_filled));
        } else {
            star.setText(mCtx.getString(R.string.star_empty));
        }
    }

    void clickStar(CardViewHolder holder) {
        int i = holder.getAdapterPosition();
        getEvent(i).toggleStarred();
        notifyItemChanged(i);
    }

    private void clickEvent(CardViewHolder holder) {
        int i = holder.getAdapterPosition();
        Intent intent = new Intent(mCtx, EventActivity.class);
        ArEvent event = items.get(i).getItem();
        if (event != null) {
            intent.putExtra(EXTRA_EVENT_ID, event.getId());
            mCtx.startActivity(intent);
        }
    }

    ArEvent getEvent(int i) {
        return items.get(i).getItem();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
