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

public class GuestsFragment extends StateHolderFragment {

    private RecyclerView mRecyclerView;

    public GuestsFragment() {
        super("BROWSE_GUESTS");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(LayoutUtils.createGridLayoutManager(getContext(), getResources().getDimension(R.dimen.guest_card_width)));

        RecyclerView.Adapter mAdapter = new ArGuestAdapter(getContext(), GuestManager.getInstance().getGuests());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
    /**
     * Save and store the fragment's scroll state
     */

    @Override
    public Object storeState() {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null)
            return null;
        return mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void restoreState(Object state) {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null)
            return;
        mRecyclerView.getLayoutManager().onRestoreInstanceState((Parcelable) state);
    }

}
