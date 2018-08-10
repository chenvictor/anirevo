package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Set;

public class TagManager {

    /**
     * Singleton Manager to keep track of ArTags
     */

    private static TagManager instance;
    private Set<ArTag> tags;

    public static TagManager getInstance() {
        if (instance == null) {
            instance = new TagManager();
        }
        return instance;
    }

    private TagManager() {
        tags = new HashSet<>();
    }

    public ArTag getTag(String name) {
        return null; //stub
    }

    public void clear() {
        //stub
    }
}
