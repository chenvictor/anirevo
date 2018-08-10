package com.example.anirevo.model;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EventManager implements Iterable<ArEvent>{

    private static EventManager instance;

    private Set<ArEvent> events;

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    private EventManager() {
        events = new HashSet<>();
    }

    public ArEvent getEvent(String title) {
        for (ArEvent event : events) {
            if (event.getTitle().equals(title)) {
                return event;
            }
        }
        ArEvent newEvent = new ArEvent(title);
        events.add(newEvent);
        return newEvent;
    }

    @NonNull
    @Override
    public Iterator<ArEvent> iterator() {
        return events.iterator();
    }

    public void clear() {
        events.clear();
    }
}
