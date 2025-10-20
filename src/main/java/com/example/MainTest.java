package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
//        FoodProduct Milk3 = new FoodProduct(UUID.randomUUID(), "Milk",
//                Category.of("diary"), new BigDecimal("30"),
//                LocalDate.now().plusDays(5),
//                new BigDecimal(2));
//
//        ElectronicsProduct laptop = new ElectronicsProduct(UUID.randomUUID(), "laptop",
//                Category.of("electronics"),new BigDecimal (4000), 24,
//                new BigDecimal (2));


        Milk2.productDetails();
        Warehouse warehouse = Warehouse.getInstance("New Warehouse");
        warehouse.addProduct(Milk1);
        warehouse.remove(Milk1.uuid());
        System.out.println(warehouse.isEmpty());
        warehouse.addProduct(Milk2);
        warehouse.shippableProducts().stream()
                .map(Shippable::calculateShippingCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//        warehouse.addProduct(Milk3);
//        warehouse.addProduct(laptop);
        warehouse.expiredProducts();
        System.out.println("All products ");
        List<Product> list = warehouse.getProducts();
        for(Product p : list){
            System.out.println(p.price());
        }
        //warehouse.updateProductPrice(Milk1.uuid(),new BigDecimal(60));
        List<Product> changed = warehouse.getChangedProducts();
        for(Product p : changed){
            System.out.println(p.price());
        }
    }
}
