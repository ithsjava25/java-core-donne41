package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    private String name;
    private static final Map<String,Category>categories = new HashMap<>();

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public static Category getCategory(String name) {
        return categories.get(name);
    }

    /*
    Dairy Electronics Gadgets Fruit
    Gör så jag har en lista med categorier här. Gör en hasmap i producter som håller koll på producter
    och kategorier kankse
    Kanske ska ha en lista med kategorier istället. och sedan ha Mapen i warehouse.
    eller om jag har den här så¨borde det vara Category List product> här. och sedan kalla på denna ifrån
    warehouse.
    men varför ska categoprys ha koll på produkter. dåligt.

     */

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        } else if (name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Category name can't be blank");
        }
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        if(categories.containsKey(formattedName)){
            return categories.get(formattedName);
        }
        Category category = new Category(formattedName);
        categories.put(formattedName, category);
        return category;
    }
    }
        /*
        validera att inte det är null, eller blankt
        normalisera namnen med att  göra fösta bokstaven versal

        Ska kunna både ta emot en sträng/ skicka 
        men under Order2 så vill den ha en Category iställer för en sträng. generic??
        om namnet redan finns retunera den instansen.

        map kanske ska vara kategorier, och sedan lista, som i övnings uppgiften men kolla mängden namn som återkommer.
        sen en lista till som håller koll på ändringar. .add i till den från update metoder.
         */

