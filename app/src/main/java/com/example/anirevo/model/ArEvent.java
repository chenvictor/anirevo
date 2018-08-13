package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ArEvent {

    /**
     * Represents an event with a time, location, etc.
     */

    //Reference of the array position in EventManager
    private int id;

    private String title;
    private String date;
    private String start;

    private String end;
    private ArLocation location;
    private String desc;
    private AgeRestriction restriction;

    private ArCategory category;
    private Set<ArGuest> guests;
    private Set<ArTag> tags;

    protected ArEvent(String title, int id) {
        this.id = id;
        this.title = title;
        guests = new HashSet<>();
        tags = new HashSet<>();
    }

    public String getDetails() {
        return getDate() + ", " + getStart() + " - " + getEnd() + " | " + getLocation().getTitle();
    }

    public void addGuest(ArGuest guest) {
        guests.add(guest);
    }

    public void addTag(ArTag tag) {
        tags.add(tag);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLocation(ArLocation location) {
        this.location = location;
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

    public AgeRestriction getRestriction() {
        return restriction;
    }

    public boolean isAgeRestricted() {
        return restriction != null;
    }

    public void setRestriction(AgeRestriction restriction) {
        this.restriction = restriction;
    }

    public int getId() {
        return id;
    }
}
