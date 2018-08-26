package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cvic.anirevo.GuestActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.utils.LayoutUtils;

public class BrowseGuestsFragment extends StateHolderFragment {

    public static final String EXTRA_GUEST_ID = "cvic.anirevo.EXTRA_GUEST_ID";

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;

    public BrowseGuestsFragment() {
        super("BROWSE_GUESTS");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_guest, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        mRecyclerView = view.findViewById(R.id.recycler_view_guest);
        mRecyclerView.setLayoutManager(LayoutUtils.createGridLayoutManager(getContext(), getResources().getDimension(R.dimen.guest_card_width)));

        if (mAdapter == null) {
            mAdapter = new CustomAdapter(getContext(), GuestManager.getInstance().getGuests());
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Save and store the fragment's scroll state
     */

    @Override
    public Object storeState() {
        return mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void restoreState(Object state) {
        mRecyclerView.getLayoutManager().onRestoreInstanceState((Parcelable) state);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CardViewHolder> {

        Context mCtx;
        List<ArGuest> guests;

        CustomAdapter(Context mCtx, List<ArGuest> guests) {
            this.mCtx = mCtx;
            this.guests = guests;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_card_layout, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder viewHolder, int i) {
            final ArGuest guest = guests.get(i);

            View view = viewHolder.getCardView();
            ImageView img = view.findViewById(R.id.guest_card_portrait);
            TextView name = view.findViewById(R.id.guest_card_name);
            TextView title = view.findViewById(R.id.guest_card_title);

            img.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.placeholder_portrait));
            name.setText(guest.getName());
            title.setText(guest.getTitle());

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //Move to GuestActivity
                    Intent intent = new Intent(getContext(), GuestActivity.class);
                    intent.putExtra(EXTRA_GUEST_ID, guest.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return guests.size();
        }
    }

}
