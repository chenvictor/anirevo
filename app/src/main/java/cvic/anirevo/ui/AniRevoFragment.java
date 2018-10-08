package cvic.anirevo.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;

import cvic.anirevo.R;
import cvic.anirevo.handlers.FragmentStateHolderHandler;

/**
 * Fragments contained by the AniRevoActivity should extend this class
 * to provide functionality for state saving, tab_layouts, and more
 */
public abstract class AniRevoFragment extends Fragment {

    private FragmentStateHolderHandler mStateHandler;
    private final String ID;
    TabLayout mAppBarTabs;

    AniRevoFragment(String id) {
        ID = id;
    }

    public void setAppBarTabs(TabLayout tabs) {
        mAppBarTabs = tabs;
    }

    public void setStateHandler(FragmentStateHolderHandler stateHandler) {
        mStateHandler = stateHandler;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStateHandler != null) {
            Object state = storeState();
            if (state != null) {
                mStateHandler.storeState(ID, state);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handleAppBarTabLayout();
        if (mStateHandler != null) {
            Object storedState = mStateHandler.getState(ID);
            if (storedState != null) {
                restoreState(storedState);
            } else {
                onFirstState();
            }
        }
    }

    /*
     * Optional Overrides for extra functionality
     */

    /**
     * Store the state of the fragment. Called during Fragment.onPause()
     * @return          the state of the fragment
     */
    Object storeState() {
        return null;
    }

    /**
     * Restore the state of the fragment. Called during Fragment.onResume()
     *      Will NOT be called if no state was stored or the stored state was null
     * @param state     the stored state of the fragment
     */
    void restoreState(Object state) {

    }

    /**
     * Run anything that should be run if no state is found
     *      Only one of restoreState or onFirstState will be called
     */
    void onFirstState() {

    }

    /**
     * Subclasses can optionally override this function if they wish to use the appBar
     * If not overriden, will reset and hide app bar tab_layout
     */
    void handleAppBarTabLayout() {
        mAppBarTabs.setVisibility(View.GONE);
        mAppBarTabs.setupWithViewPager(null);
    }

    /**
     * Subclasses can optionally override this function if they wish to set menu items
     * The Menu object will be accessible through onMenuInflated()
     * @return  int id for the menu resource
     */
    public int menuResource() {
        return R.menu.empty;
    }

    /**
     * Subclasses will also override this function to handle menu after its inflation
     * @param menu  the inflate Menu object
     */
    public void onMenuInflated(Menu menu) {

    }

}