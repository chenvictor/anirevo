package cvic.anirevo.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.GuestManager;
import cvic.anirevo.utils.LayoutUtils;

public class GuestsFragment extends AniRevoFragment {

    private static final String[] SORT_OPTIONS = {"Default", "A-Z", "Title"};

    private RecyclerView mRecyclerView;
    private MenuItem mSelector;
    private Dialog mSortSelectorDialog;

    public GuestsFragment() {
        super("BROWSE_GUESTS");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(LayoutUtils.createGridLayoutManager(getContext(), getResources().getDimension(R.dimen.guest_card_width)));
        mRecyclerView.setHasFixedSize(true);
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

    @Override
    public int menuResource() {
        return R.menu.fragment_guests;
    }

    @Override
    public void onMenuInflated(Menu menu) {
        mSelector = menu.findItem(R.id.guests_sort_selector);
        mSelector.setTitle("Sort: " + SORT_OPTIONS[0]);
        mSelector.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mSortSelectorDialog == null) {
                    mSortSelectorDialog = buildSelectorDialog();
                }
                if (mSortSelectorDialog != null) {
                    mSortSelectorDialog.show();
                }
                return true;
            }
        });
    }

    private Dialog buildSelectorDialog() {
        if (getActivity() == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort");

        builder.setSingleChoiceItems(SORT_OPTIONS, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectorPicked(i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    private void selectorPicked(int i) {
        mSelector.setTitle("Sort: " + SORT_OPTIONS[i]);
    }
}
