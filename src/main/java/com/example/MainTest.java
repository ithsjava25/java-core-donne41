package com.example;

import org.w3c.dom.ls.LSOutput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

public class MainTest {
    public static void main(String[] args) {


        FoodProduct Milk1 = new FoodProduct(UUID.randomUUID(), "Milk",
                Category.of("diary"), new BigDecimal("10"),
                LocalDate.now().plusDays(1),
                new BigDecimal(2));

//        FoodProduct Milk2 = new FoodProduct(UUID.randomUUID(), "Milk",
//                Category.of("diary"), new BigDecimal("20"),
//                LocalDate.now().minusDays(2),
//                new BigDecimal(2));
//        FoodProduct Milk3 = new FoodProduct(UUID.randomUUID(), "Milk",
//                Category.of("diary"), new BigDecimal("24"),
//                LocalDate.now().plusDays(5),
//                new BigDecimal(2));
//        FoodProduct Milk4 = new FoodProduct(UUID.randomUUID(), "Milk",
//                Category.of("diary"), new BigDecimal("24"),
//                LocalDate.now().plusDays(5),
//                new BigDecimal(2));
//        ElectronicsProduct laptop = new ElectronicsProduct(UUID.randomUUID(), "laptop",
//                Category.of("electronics"),new BigDecimal (4000), 24,
//                new BigDecimal (5));
        Warehouse warehouse = Warehouse.getInstance("New Warehouse");
        IntStream.rangeClosed(1, 10).forEach(i ->
                warehouse.addProduct(new FoodProduct(UUID.randomUUID(), "Normal" + i, Category.of("Test"),
                        new BigDecimal("15.00").add(new BigDecimal(i % 3)), LocalDate.now().plusDays(5), BigDecimal.ONE))
        );
        Product outlierHigh = new FoodProduct(UUID.randomUUID(), "Expensive", Category.of("Test"),
                new BigDecimal("500.00"), LocalDate.now().plusDays(5), BigDecimal.ONE);
        Product outlierLow = new FoodProduct(UUID.randomUUID(), "Cheap", Category.of("Test"),
                new BigDecimal("0.01"), LocalDate.now().plusDays(5), BigDecimal.ONE);

        warehouse.addProduct(outlierHigh);
        warehouse.addProduct(outlierLow);
        warehouse.addProduct(Milk1);
//        warehouse.addProduct(Milk2);
//        warehouse.addProduct(Milk3);
//        warehouse.addProduct(Milk4);
        System.out.println("changed: " + warehouse.getChangedProducts().size());
        System.out.println(warehouse.isEmpty());
        System.out.println(warehouse.shippableProducts().stream()
                .map(Shippable::calculateShippingCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("all Expired products: " +warehouse.expiredProducts());
        System.out.println("All changed products: " + warehouse.getChangedProducts());
        System.out.println("All products price");
        List<Product> list = warehouse.getProducts();
        System.out.println(list.size());


    }


    }

