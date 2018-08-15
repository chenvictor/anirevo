package cvic.anirevo.model.anirevo;

import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    private static LocationManager instance;

    private List<ArLocation> locations;

    public static LocationManager getInstance() {
        if(instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    private LocationManager() {
        locations = new ArrayList<>();
    }

    public ArLocation getLocation(String location) {
        for(ArLocation loc : locations) {
            if (loc.getPurpose().equals(location)) {
                return loc;
            }
        }
        ArLocation newLoc = new ArLocation(location);
        locations.add(newLoc);
        return newLoc;
    }

    public int size() {
        return locations.size();
    }

    public ArLocation getLocation(int id) {
        return locations.get(id);
    }

    public void clear() {
        locations.clear();
    }

}