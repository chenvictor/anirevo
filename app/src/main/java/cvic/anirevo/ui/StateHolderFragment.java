package cvic.anirevo.ui;

import android.support.v4.app.Fragment;

import cvic.anirevo.handlers.FragmentStateHolderHandler;

/**
 * Fragments that wish to have a saved state should extend this class
 */
public abstract class StateHolderFragment extends Fragment {

    private FragmentStateHolderHandler mStateHandler;
    private final String ID;

    public StateHolderFragment(String id) {
        ID = id;
    }

    public void setStateHandler(FragmentStateHolderHandler stateHandler) {
        mStateHandler = stateHandler;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStateHandler != null) {
            mStateHandler.storeState(ID, storeState());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStateHandler != null) {
            Object storedState = mStateHandler.getState(ID);
            if (storedState != null) {
                restoreState(storedState);
            }
        }
    }

    /**
     * Store the state of the fragment. Called during Fragment.onPause()
     * @return          the state of the fragment
     */
    public abstract Object storeState();

    /**
     * Restore the state of the fragment. Called during Fragment.onResume()
     *      Will NOT be called if no state was stored or the stored state was null
     * @param state     the stored state of the fragment
     */
    public abstract void restoreState(Object state);

}