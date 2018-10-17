package cvic.anirevo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.FileNotFoundException;

import cvic.anirevo.R;
import cvic.anirevo.model.map.MapVenue;
import cvic.anirevo.model.map.VenueManager;
import cvic.anirevo.utils.IOUtils;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends AniRevoFragment {

    private int mVenueIndex = -1;
    private Dialog mVenueSelectorDialog;

    private MenuItem mSpinner;
    private ImageViewTouch mMap;

    public MapFragment() {
        super("MAP");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMap = view.findViewById(R.id.mapView);
        mMap.setDoubleTapEnabled(false);
        return view;
    }

    @Override
    public int menuResource() {
        return R.menu.fragment_map;
    }

    @Override
    public void onMenuInflated(Menu menu) {
        mSpinner = menu.findItem(R.id.map_level_selector);
        if (mVenueIndex >= 0) {
            mSpinner.setTitle(VenueManager.getInstance().getVenues().get(mVenueIndex).getName());
        }
        mSpinner.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mVenueSelectorDialog == null) {
                    mVenueSelectorDialog = buildSelectorDialog();
                }
                if (mVenueSelectorDialog != null) {
                    mVenueSelectorDialog.show();
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
        builder.setTitle("Venue");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_select_dialog_singlechoice);
        for (MapVenue venue : VenueManager.getInstance().getVenues()) {
            adapter.add(venue.getName());
        }

        builder.setSingleChoiceItems(adapter, mVenueIndex, new DialogInterface.OnClickListener() {
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

    private void selectorPicked(int index) {
        mVenueIndex = index;
        MapVenue venue = VenueManager.getInstance().getVenues().get(mVenueIndex);
        try {
            Bitmap bitmap = IOUtils.getBitmap(getContext(), "images/" + venue.getImagePath());
            mMap.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (mSpinner != null) {
            mSpinner.setTitle(venue.getName());
        }
    }

    @Override
    Object storeState() {
        MapState state = new MapState();
        state.venueIndex = mVenueIndex;
        return state;
    }

    @Override
    void onFirstState() {
        selectorPicked(0);
    }

    @Override
    void restoreState(Object state) {
        MapState mapState = (MapState) state;
        selectorPicked(mapState.venueIndex);
    }

    private class MapState {
        int venueIndex;
    }

}
