package com.example;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private static Warehouse singleInstance = null;
    private final List<Product> changedList = new ArrayList<>();
    private final Map<Category, List<Product>> categoryMap = new HashMap<>();


    private Warehouse(String name) {
    }

    public static Warehouse getInstance(String name) {
        if (singleInstance == null) {
            singleInstance = new Warehouse(name);
        }
        return singleInstance;
    }
    public static Warehouse getInstance() {
        return singleInstance;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        categoryMap.putIfAbsent(product.category(), new ArrayList<>());
        categoryMap.get(product.category()).add(product);
    }

    public void remove(UUID id) {
        List<Product> products = getProducts();
        Category tempCat;
        for (Product product : products) {
            if (product.uuid().equals(id)) {
                tempCat = product.category();
            } else {
                System.out.println("product not found with UUID: " + id);
                return;
            }
            List<Product> catList = getProductsByCategory(tempCat);
            if (catList.size() == 1) {
                categoryMap.remove(tempCat);
            }
            Iterator<Product> iterator = catList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().uuid().equals(id)) {
                    iterator.remove();
                }
            }
            categoryMap.replace(tempCat, catList);
        }
    }

    private List<Product> getProductsByCategory(Category singelCat) {
        return categoryMap.get(singelCat);
    }

    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        for (List<Product> list : categoryMap.values()) {
            productList.addAll(list);
        }
        return productList;
    }

    public void clearProducts() {
        Perishable.expiredList.clear();
        FoodProduct.shipList.clear();
        ElectronicsProduct.shipList.clear();
        changedList.clear();
        categoryMap.clear();
    }

    public Optional<Product> getProductById(UUID id) {
        List<Product> products = getProducts();
        Iterator<Product> iterator = products.iterator();
        if (!products.isEmpty()) {
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (product.uuid().equals(id)) {
                    return Optional.of(product);
                }
            }
        }
        return Optional.empty();
    }

    public void updateProductPrice(UUID id, BigDecimal price) {
        if (id == null) {
            throw new IllegalArgumentException("id cant be null.");
        }
        double tempPrice = price.doubleValue();
        if (tempPrice < 0) {
            throw new IllegalArgumentException("price cant be negative.");
        }
        List<Product> products = getProducts();
        Product updateProduct = products.stream().filter(p -> p.uuid().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found with id:" + id));
        updateProduct.setPrice(price);
        changedList.add(updateProduct);

    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return categoryMap;
    }

    public List<Product> getChangedProducts() {
        return changedList;
    }

    public boolean isEmpty() {
        if (categoryMap.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Perishable> expiredProducts() {
        return Perishable.expiredList;
    }

    public List<Shippable> shippableProducts() {
        List<Shippable> ships = new ArrayList<Shippable>();
        ships.addAll(FoodProduct.shipList);
        ships.addAll(ElectronicsProduct.shipList);
        return ships;
    }
}
