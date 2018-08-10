package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Set;

public class ArCategory {

    /**
     * Represents a general category for ArEvents
     */

    private String title;

    private Set<ArEvent> events;

    public ArCategory(String title) {
        this.title = title;
        events = new HashSet<>();
    }

    public void addEvent(ArEvent event) {
        events.add(event);
    }

    public String getTitle() {
        return title;
    }
}
