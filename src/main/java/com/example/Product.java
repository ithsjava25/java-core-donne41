package com.example;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {
    private UUID id;
    private String name;
    private Category category;
    private BigDecimal price;


    public UUID uuid() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String name(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setCategory(Category categoryName) {
        this.category = categoryName;
    }

    public Category category() {
        return this.category;
    }

    public abstract BigDecimal price();
    public abstract double weight();

    public abstract void setPrice(BigDecimal price);

    public abstract String productDetails();
}
