package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

import cvic.anirevo.GuestActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.utils.IOUtils;

public class ArGuestAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public static final String EXTRA_GUEST_ID = "cvic.anirevo.EXTRA_GUEST_ID";

    private Context mCtx;
    List<ArGuest> items;

    ArGuestAdapter(Context mCtx, List<ArGuest> items) {
        this.mCtx = mCtx;
        this.items = items;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder viewHolder, int i) {
        ArGuest guest = items.get(i);

        View view = viewHolder.getCardView();
        ImageView img = view.findViewById(R.id.guest_card_portrait);
        TextView name = view.findViewById(R.id.guest_card_name);
        TextView title = view.findViewById(R.id.guest_card_title);
        TextView star = view.findViewById(R.id.guest_card_star);

        if (guest.getPortraitPath() != null) {
            try {
                Bitmap bm = IOUtils.getBitmap(mCtx, "images/" + guest.getPortraitPath());
                img.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                img.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.placeholder_portrait));
            }
        } else {
            img.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.placeholder_portrait));
        }
        name.setText(guest.getName());
        title.setText(guest.getTitle());

        if (guest.isStarred()) {
            star.setText(mCtx.getString(R.string.star_filled));
        } else {
            star.setText(mCtx.getString(R.string.star_empty));
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleStar(viewHolder.getAdapterPosition());
            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Move to GuestActivity
                Intent intent = new Intent(mCtx, GuestActivity.class);
                intent.putExtra(EXTRA_GUEST_ID, getGuest(viewHolder.getAdapterPosition()).getId());
                mCtx.startActivity(intent);
            }
        });
    }

    void toggleStar(int i) {
        ArGuest guest = getGuest(i);
        guest.toggleStarred();
        notifyItemChanged(i);
    }

    ArGuest getGuest(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
