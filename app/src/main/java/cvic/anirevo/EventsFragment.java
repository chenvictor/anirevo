package cvic.anirevo;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArCategory;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.CategoryManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public static final String TAG = "cvic.anirevo.EVENTS";

    public static final String EXTRA_EVENT_ID = "cvic.anirevo.EXTRA_EVENT_ID";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        mRecyclerView = view.findViewById(R.id.recycler_view_events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        //Generate dataset
        List<EventListItem> dataset = new ArrayList<>();
        for (ArCategory category : CategoryManager.getInstance()) {
            dataset.add(new EventListItem(category.getTitle(), Color.BLACK));
            for (ArEvent event : category) {
                dataset.add(new EventListItem(event));
            }
        }

        mAdapter = new MyAdapter(dataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(
                new HeaderItemDecoration(mRecyclerView,
                        (HeaderItemDecoration.StickyHeaderInterface) mAdapter));

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface{

        private static final int HEADER_ITEM = 0;
        private static final int EVENT_ITEM = 1;

        private List<EventListItem> items;

        @Override
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

        @Override
        public int getHeaderLayout(int headerPosition) {
            return R.layout.event_header_card_layout;
        }

        @Override
        public void bindHeaderData(View header, int headerPosition) {
            CardView card = header.findViewById(R.id.event_header_card);
            TextView text = header.findViewById(R.id.event_header_card_text);
            EventListItem item = items.get(headerPosition);
            text.setText(item.getHeaderText());
            card.setCardBackgroundColor(Color.BLACK);
        }

        @Override
        public boolean isHeader(int itemPosition) {
            return items.get(itemPosition).isHeader();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            CardView mView;
            ViewHolder(CardView v) {
                super(v);
                mView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        MyAdapter(List<EventListItem> items) {
            this.items = items;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return HEADER_ITEM;
            }
            return EVENT_ITEM;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            CardView view;
            switch (viewType) {
                case HEADER_ITEM:
                    view = (CardView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.event_header_card_layout, parent, false);
                    break;
                case EVENT_ITEM:
                    view = (CardView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.event_card_layout, parent, false);
                    break;
                default:
                    throw new UnsupportedOperationException("ViewType is invalid");
            }
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            EventListItem item = items.get(position);
            switch (holder.getItemViewType()) {
                case HEADER_ITEM:
                    holder.mView.setCardBackgroundColor(item.getHeaderColor());
                    TextView text = holder.mView.findViewById(R.id.event_header_card_text);
                    text.setText(item.getHeaderText());
                    holder.itemView.setOnClickListener(null);
                    break;
                case EVENT_ITEM:
                    TextView name = holder.mView.findViewById(R.id.event_header_card_text);
                    TextView details = holder.mView.findViewById(R.id.event_card_details);
                    TextView desc = holder.mView.findViewById(R.id.event_card_description);
                    TextView age = holder.mView.findViewById(R.id.event_card_age_restriction);
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
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), EventActivity.class);
                            intent.putExtra(EXTRA_EVENT_ID, event.getId());
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    throw new UnsupportedOperationException("ViewType is invalid");
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    static class HeaderItemDecoration extends RecyclerView.ItemDecoration {

        private StickyHeaderInterface mListener;
        private int mStickyHeaderHeight;

        HeaderItemDecoration(RecyclerView recyclerView, @NonNull StickyHeaderInterface listener) {
            mListener = listener;

            // On Sticky Header Click
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                    if (motionEvent.getY() <= mStickyHeaderHeight) {
                        // Handle the clicks on the header here ...
                        return true;
                    }
                    return false;
                }

                public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                }

                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
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

        public interface StickyHeaderInterface {

            /**
             * This method gets called by {@link HeaderItemDecoration} to fetch the position of the header item in the adapter
             * that is used for (represents) item at specified position.
             *
             * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
             * @return int. Position of the header item in the adapter.
             */
            int getHeaderPositionForItem(int itemPosition);

            /**
             * This method gets called by {@link HeaderItemDecoration} to get layout resource id for the header item at specified adapter's position.
             *
             * @param headerPosition int. Position of the header item in the adapter.
             * @return int. Layout resource id.
             */
            int getHeaderLayout(int headerPosition);

            /**
             * This method gets called by {@link HeaderItemDecoration} to setup the header View.
             *
             * @param header         View. Header to set the data on.
             * @param headerPosition int. Position of the header item in the adapter.
             */
            void bindHeaderData(View header, int headerPosition);

            /**
             * This method gets called by {@link HeaderItemDecoration} to verify whether the item represents a header.
             *
             * @param itemPosition int.
             * @return true, if item at the specified adapter's position represents a header.
             */
            boolean isHeader(int itemPosition);
        }
    }
}
