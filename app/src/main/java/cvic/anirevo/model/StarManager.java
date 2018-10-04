package cvic.anirevo.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArGuest;

public class StarManager {

    private static StarManager instance;

    //Contains titles of events/guests for EventParser to use
    private Set<String> eventTitles;
    private Set<String> guestTitles;

    //Actual event and guest set for program use
    private Set<ArEvent> starredEvents;
    private Set<ArGuest> starredGuests;

    public static StarManager getInstance() {
        if (instance == null) {
            instance = new StarManager();
        }
        return instance;
    }

    private StarManager() {
        starredEvents = new HashSet<>();
        starredGuests = new HashSet<>();
    }

    public void add(ArGuest guest) {
        starredGuests.add(guest);
        guest.setStarred(true);
    }

    public void add(ArEvent event) {
        starredEvents.add(event);
        event.setStarred(true);
    }

    public void remove(ArGuest guest) {
        starredGuests.remove(guest);
        guest.setStarred(false);
    }

    public void remove(ArEvent event) {
        starredEvents.remove(event);
        event.setStarred(false);
    }

    public Set<ArEvent> getStarredEvents() {
        return Collections.unmodifiableSet(starredEvents);
    }

    public Set<ArGuest> getStarredGuests() {
        return Collections.unmodifiableSet(starredGuests);
    }

    public void addGuest(String name) {
        if (guestTitles == null) {
            guestTitles = new HashSet<>();
        }
        guestTitles.add(name);
    }

    public void addEvent(String name) {
        if (eventTitles == null) {
            eventTitles = new HashSet<>();
        }
        eventTitles.add(name);
    }

    public void clearNames() {
        //Should be called after EventParser completes to allow for garbage collection
        eventTitles = null;
        guestTitles = null;
    }

    public boolean isGuestStarred(String name) {
        return guestTitles != null && guestTitles.contains(name);
    }

    public boolean isEventStarred(String name) {
        return eventTitles != null && eventTitles.contains(name);
    }

    public void clear() {
        starredEvents.clear();
        starredGuests.clear();
        eventTitles = null;
        guestTitles = null;
    }

}
