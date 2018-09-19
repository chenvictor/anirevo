package cvic.anirevo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_guest, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_guest);
        mRecyclerView.setLayoutManager(LayoutUtils.createGridLayoutManager(getContext(), getResources().getDimension(R.dimen.guest_card_width)));

        if (mAdapter == null) {
            //temp
            List<ArGuest> guests = new ArrayList<>(StarManager.getInstance().getStarredGuests());
            mAdapter = new ArGuestAdapter(getContext(), guests);
        }
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
