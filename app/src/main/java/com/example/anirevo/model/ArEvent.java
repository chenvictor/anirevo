package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ArEvent {

    /**
     * Represents an event with a time, location, etc.
     */

    private String title;
    private String desc;
    private ArLocation location;
    private int start;
    private int end;

    private ArCategory category;
    private Set<ArGuest> guests;
    private Set<ArTag> tags;

    protected ArEvent(String title) {
        this.title = title;
        guests = new HashSet<>();
        tags = new HashSet<>();
    }

    public void addGuest(ArGuest guest) {
        guests.add(guest);
    }

    public void addTag(ArTag tag) {
        tags.add(tag);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLocation(ArLocation location) {
        this.location = location;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public ArLocation getLocation() {
        return location;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArEvent arEvent = (ArEvent) o;
        return start == arEvent.start &&
                end == arEvent.end &&
                Objects.equals(title, arEvent.title) &&
                Objects.equals(location, arEvent.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    public ArCategory getCategory() {
        return category;
    }

    public void setCategory(ArCategory category) {
        this.category = category;
    }
}
