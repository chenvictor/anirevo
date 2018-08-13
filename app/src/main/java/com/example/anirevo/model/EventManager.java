package com.example.anirevo.model;

import android.support.annotation.NonNull;

import com.example.anirevo.InvalidIdException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EventManager implements Iterable<ArEvent>{

    private static EventManager instance;

    private List<ArEvent> events;

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    private EventManager() {
        events = new ArrayList<>();
    }

    public ArEvent getEvent(int id) throws InvalidIdException{
        if (id < 0 || id >= events.size())
            throw new InvalidIdException();
        return events.get(id);
    }

    public ArEvent getEvent(String title) {
        for (ArEvent event : events) {
            if (event.getTitle().equals(title)) {
                return event;
            }
        }
        ArEvent newEvent = new ArEvent(title, events.size());
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
