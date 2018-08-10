package com.example.anirevo.model;

import java.util.HashSet;
import java.util.Set;

public class CategoryManager {

    private static CategoryManager instance;

    private Set<ArCategory> categories;

    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    private CategoryManager() {
        categories = new HashSet<>();
    }

    public ArCategory getCategory(String title) {
        for (ArCategory cat : categories) {
            if (cat.getTitle().equals(title)){
                return cat;
            }
        }
        ArCategory newCat = new ArCategory(title);
        categories.add(newCat);
        return newCat;
    }

    public void clear() {

    }
}
