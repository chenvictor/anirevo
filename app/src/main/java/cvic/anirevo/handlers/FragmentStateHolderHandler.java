package cvic.anirevo.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle fragment's that wish to save a state
 */
public class FragmentStateHolderHandler {

    Map<String, Object> savedStates;

    FragmentStateHolderHandler() {
        savedStates = new HashMap<>();
    }

    /**
     * Stores a state for the given fragment
     * @param id        id to store the state with
     * @param state     state to save
     */
    public void storeState(String id, Object state) {
        savedStates.put(id, state);
    }

    /**
     * Fetch the state for the given fragment
     * @param id        id to which the state was assigned
     * @return          the saved state of the fragment, or null if none was stored
     */
    public Object getState(String id) {
        return savedStates.get(id);
    }

}
