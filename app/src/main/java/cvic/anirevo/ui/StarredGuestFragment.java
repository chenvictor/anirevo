package cvic.anirevo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.R;
import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.utils.LayoutUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link StarredGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarredGuestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private TextView mTextView;

    public StarredGuestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StarredGuestFragment.
     */
    public static StarredGuestFragment newInstance() {
        StarredGuestFragment fragment = new StarredGuestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.emptyable_recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextView = view.findViewById(R.id.empty_text);
        mTextView.setText(R.string.empty_starred_guests_prompt);
        mRecyclerView.setLayoutManager(LayoutUtils.createGridLayoutManager(getContext(), getResources().getDimension(R.dimen.guest_card_width)));

        List<ArGuest> guests = new ArrayList<>(StarManager.getInstance().getStarredGuests());
        mAdapter = new StarredGuestAdapter(getContext(), guests);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
        });
        checkEmpty();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void checkEmpty() {
        boolean empty = mAdapter.getItemCount() == 0;
        mTextView.setVisibility(empty ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private class StarredGuestAdapter extends ArGuestAdapter {

        StarredGuestAdapter(Context mCtx, List<ArGuest> guests) {
            super(mCtx, guests);
        }

        @Override
        protected void clickStar(CardViewHolder holder) {
            int idx = holder.getAdapterPosition();
            ArGuest guest = getGuest(idx);
            if (!guest.toggleStarred()) {
                items.remove(idx);
                notifyItemRemoved(idx);
            }
        }
    }

}
