package com.example;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private String name;
    private static final Map<String, Category> categories = new HashMap<>();

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Factory method of creating a category
     * @param name of the category, case-insensitive.
     * @return category name if it already exisits or creates a new with input name.
     */
    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        } else if (name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Category name can't be blank");
        }
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        categories.putIfAbsent(formattedName, new Category(formattedName));
        return categories.get(formattedName);
    }
}

