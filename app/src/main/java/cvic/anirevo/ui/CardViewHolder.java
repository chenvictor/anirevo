package cvic.anirevo.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    private CardView mCardView;

    public CardViewHolder(CardView v) {
        super(v);
        mCardView = v;
    }

    public CardView getCardView() {
        return mCardView;
    }
}
