package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cvic.anirevo.GuestActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.Starrable;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.utils.IOUtils;

public class ArGuestAdapter extends RecyclerView.Adapter<CardViewHolder> implements Starrable.StarListener {

    public static final String EXTRA_GUEST_ID = "cvic.anirevo.EXTRA_GUEST_ID";

    private Context mCtx;
    private List<ArGuest> items;

    private Bitmap DEFAULT_IMAGE;
    private Map<ArGuest, Bitmap> imageCache;

    ArGuestAdapter(Context mCtx, List<ArGuest> items) {
        this.mCtx = mCtx;
        this.items = items;
        imageCache = new HashMap<>();
        fetchImages();
    }

    private void fetchImages() {
        float bitmapSize = mCtx.getResources().getDimension(R.dimen.guest_card_portrait_size);
        DEFAULT_IMAGE = ((BitmapDrawable) mCtx.getResources().getDrawable(R.drawable.placeholder_portrait)).getBitmap();
        DEFAULT_IMAGE = Bitmap.createScaledBitmap(DEFAULT_IMAGE, (int) bitmapSize, (int) bitmapSize, true);
        for (ArGuest guest : items) {
            try {
                Bitmap bm = IOUtils.getBitmap(mCtx, "images/" + guest.getPortraitPath());
                bm = Bitmap.createScaledBitmap(bm, (int) bitmapSize, (int) bitmapSize, true);
                imageCache.put(guest, bm);
            } catch (Exception ignored) { }
        }
    }

    private Bitmap getImage(ArGuest guest) {
        if (imageCache.containsKey(guest)) {
            return imageCache.get(guest);
        }
        return DEFAULT_IMAGE;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_card_layout, parent, false);
        TextView star = view.findViewById(R.id.guest_card_star);
        final CardViewHolder holder = new CardViewHolder(view);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStar(holder);
            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clickGuest(holder);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder viewHolder, int i) {
        ArGuest guest = items.get(i);

        View view = viewHolder.getCardView();
        ImageView img = view.findViewById(R.id.guest_card_portrait);
        TextView name = view.findViewById(R.id.guest_card_name);
        TextView title = view.findViewById(R.id.guest_card_title);
        TextView star = view.findViewById(R.id.guest_card_star);

        img.setImageBitmap(getImage(guest));
        name.setText(guest.getName());
        title.setText(guest.getTitle());
        star.setText(guest.isStarred() ? mCtx.getString(R.string.star_filled) : mCtx.getString(R.string.star_empty));

        guest.setListener(this, viewHolder.getAdapterPosition());
    }

    void clickStar(CardViewHolder holder) {
        int idx = holder.getAdapterPosition();
        getGuest(idx).toggleStarred();
    }

    private void clickGuest(CardViewHolder holder) {
        int idx = holder.getAdapterPosition();
        //Move to GuestActivity
        Intent intent = new Intent(mCtx, GuestActivity.class);
        ArGuest guest = getGuest(idx);
        if (guest != null) {
            intent.putExtra(EXTRA_GUEST_ID, getGuest(idx).getId());
            mCtx.startActivity(intent);
        }
    }

    ArGuest getGuest(int i) {
        if (i >= items.size()) {
            return null;
        }
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

    @Override
    public void onValueChange(Starrable object) {
        notifyItemChanged(object.getListenerIndex());
    }
}
