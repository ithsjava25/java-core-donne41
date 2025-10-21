package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FoodProduct extends Product
        implements Perishable, Shippable {
    LocalDate expirationDate;
    BigDecimal price;
    BigDecimal weight;



    public FoodProduct(UUID id, String productName, Category categoryName, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        this.setId(id);
        this.setName(productName);
        this.setCategory(categoryName);
        this.expirationDate(expirationDate);
        this.setPrice(price);
        this.weight(weight);
    }

    @Override
    public void setPrice(BigDecimal price) {
        double tempPrice = price.doubleValue();
        if (tempPrice >= 0.0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }
    public void weight(BigDecimal weight) {
        if(weight.doubleValue() < 0.0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.weight = weight;
    }
    public double weight() {
        return this.weight.doubleValue();
    }



    public BigDecimal price() {
        return price;
    }

    public LocalDate expirationDate() {
        return this.expirationDate;
    }
    public void expirationDate(LocalDate date){
        this.expirationDate = date;
    }


    @Override
    public String toString() {
        return "FoodProduct {" +
                " name= "+ this.name() +
                " expirationDate= " + expirationDate +
                ", price= " + price +
                ", weight= " + weight +
                ", UUid= " + this.uuid() +
                '}';
    }

    @Override
    public String productDetails() {
        String name = this.name();
        LocalDate expi = this.expirationDate();
        return String.format("""
                Food: %s, Expires: %s""", name,expi);
    }



    @Override
    public BigDecimal calculateShippingCost() {
        BigDecimal weight = this.weight;
        BigDecimal shippingCost = weight.multiply(new BigDecimal(50));
        return shippingCost.setScale(2, RoundingMode.HALF_UP);
    }
}
