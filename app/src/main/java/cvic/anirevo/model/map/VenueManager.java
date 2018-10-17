package cvic.anirevo.model.map;

import java.util.ArrayList;
import java.util.List;

public class VenueManager {

    private static VenueManager instance;

    private static List<MapVenue> venues;

    public static VenueManager getInstance() {
        if (instance == null) {
            instance = new VenueManager();
        }
        return instance;
    }

    private VenueManager () {
        venues = new ArrayList<>();
    }

    public List<MapVenue> getVenues() {
        return venues;
    }

    public MapVenue getVenue (String name) {
        for (MapVenue venue : venues) {
            if (venue.getName().equals(name)) {
                return venue;
            }
        }
        MapVenue newVenue = new MapVenue(name);
        venues.add(newVenue);
        return newVenue;
    }

    public void clear () {
        venues.clear();
    }

}
