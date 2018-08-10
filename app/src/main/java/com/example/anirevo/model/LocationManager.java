package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Set;

public class LocationManager {

    private static LocationManager instance;

    private Set<ArLocation> locations;

    public static LocationManager getInstance() {
        if(instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    private LocationManager() {
        locations = new HashSet<>();
    }

    public ArLocation getLocation(String title) {
        for(ArLocation loc : locations) {
            if (loc.getTitle().equals(title)) {
                return loc;
            }
        }
        ArLocation newLoc = new ArLocation(title);
        locations.add(newLoc);
        return newLoc;
    }

    public void clear() {
        locations.clear();
    }

}
