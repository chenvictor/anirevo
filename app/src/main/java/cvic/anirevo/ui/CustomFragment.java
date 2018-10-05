package cvic.anirevo.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import cvic.anirevo.handlers.FragmentStateHolderHandler;

/**
 * Fragments that wish to have a saved state should extend this class
 * OR
 * Fragments that will utilize the app bar tab layout should extend this class
 */
public abstract class CustomFragment extends Fragment {

    private FragmentStateHolderHandler mStateHandler;
    private final String ID;
    protected TabLayout mAppBarTabs;

    CustomFragment(String id) {
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
        handleAppBar();
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
    protected Object storeState() {
        return null;
    }

    /**
     * Restore the state of the fragment. Called during Fragment.onResume()
     *      Will NOT be called if no state was stored or the stored state was null
     * @param state     the stored state of the fragment
     */
    protected void restoreState(Object state) {

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
    protected void handleAppBar() {
        mAppBarTabs.setVisibility(View.GONE);
        mAppBarTabs.setupWithViewPager(null);
    }

}