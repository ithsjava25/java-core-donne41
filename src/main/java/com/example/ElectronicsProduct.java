package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    private int warrantyMonths;
    private BigDecimal price;
    private BigDecimal weight;



    public ElectronicsProduct(UUID id, String productName, Category category,
                              BigDecimal price, int warrantyMonths,
                              BigDecimal weight) {
        this.setId(id);
        this.setName(productName);
        this.setCategory(category);
        this.setPrice(price);
        this.setWarrantyMonths(warrantyMonths);
        this.weight(weight);

    }


    public void setWarrantyMonths(int warrantyMonths) {
        if(warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        this.warrantyMonths = warrantyMonths;
    }

    public int warrantyMonths() {
        return this.warrantyMonths;
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

    @Override
    public BigDecimal calculateShippingCost() {
        BigDecimal price = BigDecimal.valueOf(79);
        if(this.weight.doubleValue() > 5.0){
            price = price.add(BigDecimal.valueOf(49));
        }
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String productDetails() {
        String productName = this.name();
        int warrantyMonths = this.warrantyMonths();
        return String.format("""
                Electronics: %s, Warranty: %s months""", productName, warrantyMonths);
    }

    @Override
    public void setPrice(BigDecimal price) {
        if (price.doubleValue() >= 0.0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    public BigDecimal price() {
        return price;
    }


    @Override
    public String toString() {
        return "ElectronicsProduct {" +
                " Name= " + this.name()+
                " warrantyMonths=" + warrantyMonths +
                ", price=" + price +
                ", weight=" + weight +
                ", UUid= " + this.uuid()+
                '}';
    }
}
