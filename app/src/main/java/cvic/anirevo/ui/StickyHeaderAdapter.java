package cvic.anirevo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A custom RecyclerView Adapter class that implements StickyHeaders
 *      can be applied to a generic type
 * @param <T>   type of items the adapter should hold
 */

public abstract class StickyHeaderAdapter<T> extends RecyclerView.Adapter<CardViewHolder> {

    private static final int ITEM_HEADER = 0;
    private static final int ITEM_ITEM = 1;

    Context mCtx;
    private List<ListItem<T>> mItems;

    public StickyHeaderAdapter(Context ctx, List<ListItem<T>> items) {
        mCtx = ctx;
        mItems = items;
    }

    /**
     * Override this method to set the header layout
     *  should be a CardView
     * @return  resource id of the header layout
     */
    protected abstract int headerLayout();

    /**
     * Override this method to set the item layout
     *  should be a CardView
     * @return  resource id of the item layout
     */
    protected abstract int itemLayout();

    protected abstract CardViewHolder createHeader(@NonNull ViewGroup parent);

    protected abstract CardViewHolder createItem(@NonNull ViewGroup parent);

    /**
     * Override this method to bind the header to its view
     * @param card      CardView of the header
     * @param header    ListItem with the header information
     */
    protected abstract void bindHeader(@NonNull CardView card, ListItem header);

    /**
     * Override this method to bind the item to its view
     * @param view      CardView of the item
     * @param item      Generic item of type T to bind
     */
    protected abstract void bindItem(@NonNull CardView view, T item);

    @NonNull
    @Override
    final public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_HEADER:
                return createHeader(parent);
            case ITEM_ITEM:
                return createItem(parent);
            default:
                throw new UnsupportedOperationException("ViewType is invalid");
        }
    }

    @Override
    final public int getItemCount() {
        return mItems.size();
    }

    @Override
    final public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ListItem<T> item = mItems.get(position);
        CardView view = holder.getCardView();
        switch (holder.getItemViewType()) {
            case ITEM_HEADER:
                bindHeader(view, item);
                break;
            case ITEM_ITEM:
                bindItem(view, item.getItem());
                break;
            default:
                throw new UnsupportedOperationException("ViewType is invalid");
        }
    }

    @Override
    final public int getItemViewType(int position) {
        if (isHeader(position)) {
            return ITEM_HEADER;
        }
        return ITEM_ITEM;
    }

    private int getHeaderPositionForItem(int itemPosition) {
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

    private void bindHeaderData(CardView card, int headerPosition) {
        ListItem item = mItems.get(headerPosition);
        bindHeader(card, item);
    }

    private boolean isHeader(int itemPosition) {
        return mItems.get(itemPosition).isHeader();
    }

    public static class HeaderItemDecoration extends RecyclerView.ItemDecoration {

        private StickyHeaderAdapter mListener;
        private int mStickyHeaderHeight;

        public HeaderItemDecoration(RecyclerView recyclerView, @NonNull StickyHeaderAdapter listener) {
            mListener = listener;

            // On Sticky Header Click
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                    return motionEvent.getY() <= mStickyHeaderHeight;
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
            int layoutResId = mListener.headerLayout();
            CardView header = (CardView) LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
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
