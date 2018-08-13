package com.example.anirevo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
