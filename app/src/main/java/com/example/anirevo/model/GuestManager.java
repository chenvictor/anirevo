package com.example.anirevo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuestManager {

    /**
     * Singleton Manager to keep track of ArGuests
     */

    private static GuestManager instance;

    private List<ArGuest> guests;

    public static GuestManager getInstance() {
        if (instance == null) {
            instance = new GuestManager();
        }
        return instance;
    }

    private GuestManager() {
        guests = new ArrayList<>();
    }

    public ArGuest getGuest(String name) {
        for (ArGuest guest : guests) {
            if (guest.getName().equals(name)) {
                return guest;
            }
        }
        ArGuest newGuest = new ArGuest(name);
        guests.add(newGuest);
        return newGuest;
    }

    public ArGuest getGuest(int idx) {
        return guests.get(idx);
    }

    public List<ArGuest> getGuests() {
        return Collections.unmodifiableList(guests);
    }

    public int getNumGuests(){
        return guests.size();
    }

    public void clear() {
        guests.clear();
    }
}
