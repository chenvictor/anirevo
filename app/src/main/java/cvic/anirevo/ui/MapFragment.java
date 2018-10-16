package cvic.anirevo.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cvic.anirevo.R;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends AniRevoFragment {

    private static final String[] MENU_LEVELS = {"Exhibition Level", "Level 1"};

    private MenuItem mSpinner;
    private ImageViewTouch mMap;

    public MapFragment() {
        super("MAP");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMap = view.findViewById(R.id.mapView);
        mMap.setDoubleTapEnabled(false);
        mMap.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.vcc_level_1));
        return view;
    }

    @Override
    public int menuResource() {
        return R.menu.fragment_map;
    }

    @Override
    public void onMenuInflated(Menu menu) {
        mSpinner = menu.findItem(R.id.map_level_selector);
        mSpinner.setTitle(MENU_LEVELS[0]);

    }
}
