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
import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;

public class ArEventAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public static final String EXTRA_EVENT_ID = "cvic.anirevo.EXTRA_EVENT_ID";

    private Context mCtx;
    List<EventListItem> items;

    // Provide a suitable constructor (depends on the kind of dataset)
    ArEventAdapter(Context ctx, List<EventListItem> items) {
        this.items = items;
        mCtx = ctx;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        EventListItem item = items.get(position);
        CardView view = holder.getCardView();
        TextView name = view.findViewById(R.id.event_header_card_text);
        TextView details = view.findViewById(R.id.event_card_details);
        TextView desc = view.findViewById(R.id.event_card_description);
        TextView age = view.findViewById(R.id.event_card_age_restriction);
        TextView star = view.findViewById(R.id.event_card_star);

        final ArEvent event = item.getEvent();
        name.setText(event.getTitle());
        details.setText(event.getDetails());
        desc.setText(event.getDesc());
        if (event.isAgeRestricted()) {
            AgeRestriction restriction = event.getRestriction();
            age.setText(restriction.toString());
            age.setTextColor(restriction.getTextColor());
            age.setVisibility(View.VISIBLE);
        } else {
            age.setVisibility(View.INVISIBLE);
        }
        if (event.isStarred()) {
            star.setText(mCtx.getString(R.string.star_filled));
        } else {
            star.setText(mCtx.getString(R.string.star_empty));
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleStar(holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, EventActivity.class);
                intent.putExtra(EXTRA_EVENT_ID, event.getId());
                mCtx.startActivity(intent);
            }
        });
    }

    private void toggleStar(int i) {
        ArEvent event = getEvent(i);
        if (event.toggleStarred()) {
            StarManager.getInstance().add(event);
        } else {
            StarManager.getInstance().remove(event);
        }
        notifyItemChanged(i);
    }

    private ArEvent getEvent(int i) {
        return items.get(i).getEvent();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
