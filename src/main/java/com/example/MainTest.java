package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainTest {
    public static void main(String[] args) {


        FoodProduct Milk1 = new FoodProduct(UUID.randomUUID(), "Milk",
                Category.of("diary"), new BigDecimal("10"),
                LocalDate.now().plusDays(1),
                new BigDecimal(2));

        FoodProduct Milk2 = new FoodProduct(UUID.randomUUID(), "Milk",
                Category.of("diary"), new BigDecimal("20"),
                LocalDate.now().minusDays(2),
                new BigDecimal(2));
        FoodProduct Milk3 = new FoodProduct(UUID.randomUUID(), "Milk",
                Category.of("diary"), new BigDecimal("30"),
                LocalDate.now().plusDays(5),
                new BigDecimal(2));
        ElectronicsProduct laptop = new ElectronicsProduct(UUID.randomUUID(), "laptop",
                Category.of("electronics"),new BigDecimal (4000), 24,
                new BigDecimal (5));


        Warehouse warehouse = Warehouse.getInstance("New Warehouse");
        warehouse.addProduct(Milk1);
        warehouse.addProduct(Milk2);
        warehouse.addProduct(laptop);
        List<Product> test = warehouse.getProducts();
        System.out.println(test.size());
        System.out.println(Milk2);
        warehouse.addProduct(Milk3);
        warehouse.updateProductPrice(laptop.uuid(),new BigDecimal (9500));
        warehouse.updateProductPrice(laptop.uuid(),new BigDecimal (8000));
        System.out.println(warehouse.isEmpty());
        System.out.println(warehouse.shippableProducts().stream()
                .map(Shippable::calculateShippingCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("all Expired products: " +warehouse.expiredProducts());
        System.out.println("All changed products: " + warehouse.getChangedProducts());
        System.out.println("All products price");
        List<Product> list = warehouse.getProducts();
        for(Product p : list){
            System.out.println(p.price());
        }
    }
}
