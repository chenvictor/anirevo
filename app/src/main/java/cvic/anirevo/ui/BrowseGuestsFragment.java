package cvic.anirevo.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.utils.LayoutUtils;

public class BrowseGuestsFragment extends StateHolderFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

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
            mAdapter = new ArGuestAdapter(getContext(), GuestManager.getInstance().getGuests());
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

}
