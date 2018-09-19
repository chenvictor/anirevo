package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;

public class ArStickyHeaderEventAdapter extends ArEventAdapter {

    private static final int HEADER_ITEM = 0;
    private static final int EVENT_ITEM = 1;

    ArStickyHeaderEventAdapter(Context ctx, List<EventListItem> items) {
        super(ctx, items);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_ITEM:
                return new CardViewHolder((CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_header_card_layout, parent, false));
            case EVENT_ITEM:
                return super.onCreateViewHolder(parent, viewType);
            default:
                throw new UnsupportedOperationException("ViewType is invalid");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        EventListItem item = items.get(position);
        CardView view = holder.getCardView();
        switch (holder.getItemViewType()) {
            case HEADER_ITEM:
                view.setCardBackgroundColor(item.getHeaderColor());
                TextView text = view.findViewById(R.id.event_header_card_text);
                text.setText(item.getHeaderText());
                holder.itemView.setOnClickListener(null);
                break;
            case EVENT_ITEM:
                super.onBindViewHolder(holder, position);
                break;
            default:
                throw new UnsupportedOperationException("ViewType is invalid");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return HEADER_ITEM;
        }
        return EVENT_ITEM;
    }

    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    public int getHeaderLayout(int headerPosition) {
        return R.layout.event_header_card_layout;
    }

    public void bindHeaderData(View header, int headerPosition) {
        CardView card = header.findViewById(R.id.event_header_card);
        TextView text = header.findViewById(R.id.event_header_card_text);
        EventListItem item = items.get(headerPosition);
        text.setText(item.getHeaderText());
        card.setCardBackgroundColor(Color.BLACK);
    }

    public boolean isHeader(int itemPosition) {
        return items.get(itemPosition).isHeader();
    }

    static class HeaderItemDecoration extends RecyclerView.ItemDecoration {

        private ArStickyHeaderEventAdapter mListener;
        private int mStickyHeaderHeight;

        HeaderItemDecoration(RecyclerView recyclerView, @NonNull ArStickyHeaderEventAdapter listener) {
            mListener = listener;

            // On Sticky Header Click
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                    if (motionEvent.getY() <= mStickyHeaderHeight) {
                        // Handle the clicks on the header here ...
                        // TODO: Header clicks scroll user to the header content
                        return true;
                    }
                    return false;
                }

                public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                }

                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            View topChild = parent.getChildAt(0);
            if (topChild == null) {
                return;
            }

            int topChildPosition = parent.getChildAdapterPosition(topChild);
            if (topChildPosition == RecyclerView.NO_POSITION) {
                return;
            }

            View currentHeader = getHeaderViewForItem(topChildPosition, parent);
            fixLayoutSize(parent, currentHeader);
            int contactPoint = currentHeader.getBottom();
            View childInContact = getChildInContact(parent, contactPoint);
            if (childInContact == null) {
                return;
            }

            if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
                moveHeader(c, currentHeader, childInContact);
                return;
            }

            drawHeader(c, currentHeader);
        }

        private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
            int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
            int layoutResId = mListener.getHeaderLayout(headerPosition);
            View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
            mListener.bindHeaderData(header, headerPosition);
            return header;
        }

        private void drawHeader(Canvas c, View header) {
            c.save();
            c.translate(0, 0);
            header.draw(c);
            c.restore();
        }

        private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
            c.save();
            c.translate(0, nextHeader.getTop() - currentHeader.getHeight());
            currentHeader.draw(c);
            c.restore();
        }

        private View getChildInContact(RecyclerView parent, int contactPoint) {
            View childInContact = null;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getBottom() > contactPoint) {
                    if (child.getTop() <= contactPoint) {
                        // This child overlaps the contactPoint
                        childInContact = child;
                        break;
                    }
                }
            }
            return childInContact;
        }

        /**
         * Properly measures and layouts the top sticky header.
         *
         * @param parent ViewGroup: RecyclerView in this case.
         */
        private void fixLayoutSize(ViewGroup parent, View view) {

            // Specs for parent (RecyclerView)
            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

            // Specs for children (headers)
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

            view.measure(childWidthSpec, childHeightSpec);

            view.layout(0, 0, view.getMeasuredWidth(), mStickyHeaderHeight = view.getMeasuredHeight());
        }
    }
}
