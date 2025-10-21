package com.example;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private static Warehouse singleInstance = null;
    private final Set<Product> changedSet = new HashSet<>();
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
        checkDuplicate(product);
        categoryMap.putIfAbsent(product.category(), new ArrayList<>());
        categoryMap.get(product.category()).add(product);
    }

    /**
     * Checks if a product already exists in the warehouse based on the UUID.
     * Throws IlleagalArgumentException if duplicate is found.
     *
     * @param product to compare.
     */
    private void checkDuplicate(Product product) {
        var list = getProductList();
        for (var p : list) {
            if (p.uuid().equals(product.uuid())) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }
    }


    public List<Product> findOutliners() {
        var productList = getProductList();
        List<Product> outliners = new ArrayList<>();
        int medianIndex = (int) (productList.size() / 2.0 + 0.5);
        int lowerQIndex = (int) (medianIndex / 2.0 + 0.5);
        int upperQIndex = medianIndex + lowerQIndex;
        var sortedList = productList.stream().sorted(Comparator.comparing(Product::price)).toList();
        BigDecimal lowerPrice = sortedList.get(lowerQIndex).price();
        BigDecimal upperPrice = sortedList.get(upperQIndex).price();
        BigDecimal oneAndHalf = new BigDecimal("1.5");
        BigDecimal IQR = upperPrice.subtract(lowerPrice);
        BigDecimal lowIQR = lowerPrice.subtract(oneAndHalf.multiply(IQR));
        BigDecimal HighIQR = upperPrice.add(oneAndHalf.multiply(IQR));

        for (var product : sortedList) {
            BigDecimal tempDecimalPrice = product.price();
            double tempPrice = tempDecimalPrice.doubleValue();
            if (tempPrice < lowIQR.doubleValue() || tempPrice > HighIQR.doubleValue()) {
                outliners.add(product);
            }
        }
        return outliners;

    }

    public void remove(UUID id) {
        List<Product> products = getProductList();
        Category tempCat;
        for (Product product : products) {
            if (product.uuid().equals(id)) {
                tempCat = product.category();
            } else {
                System.out.println("product not found with UUID: " + id);
                return;
            }
            List<Product> categoryList = categoryMap.get(tempCat);
            if (categoryList.size() == 1) {
                categoryMap.remove(tempCat);
            }
            categoryList.stream().filter(p->p.uuid()
                    .equals(id))
                    .findFirst()
                    .ifPresent(categoryList::remove);

            categoryMap.replace(tempCat, categoryList);
        }
    }

    /**
     * Get all product from the warehouse.
     *
     * @return an immutable list.
     */
    public List<Product> getProducts() {
        if (categoryMap.isEmpty()) {
            return List.of();
        }
        var list = getProductList();
        return List.copyOf(list);
    }

    /**
     * Get all product from the warehouse.
     *
     * @return a mutable list.
     */
    public List<Product> getProductList() {
        List<Product> products = new ArrayList<>();
        categoryMap.values()
                .forEach(products::addAll);
        return products;
    }

    /**
     * Clears the warehouse of all products in all lists.
     */
    public void clearProducts() {
        changedSet.clear();
        categoryMap.clear();
    }

    /**
     * Search the warehouse for a product by UUID
     *
     * @param id UUID
     * @return Optional of product if found or an empty Optional.
     */
    public Optional<Product> getProductById(UUID id) {
        List<Product> products = getProductList();
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
        List<Product> products = getProductList();
        Product updateProduct = products.stream().filter(p -> p.uuid().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found with id:" + id));
        updateProduct.setPrice(price);
        changedSet.add(updateProduct);

    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return categoryMap;
    }

    public Set<Product> getChangedProducts() {
        return changedSet;
    }

    public boolean isEmpty() {
        if (categoryMap.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Perishable> expiredProducts() {
        List<Perishable> perishables = new ArrayList<>();
        var list = getProductList();
        for (Product product : list) {
            if (product instanceof Perishable) {
                if (((Perishable) product).isExpired())
                    perishables.add((Perishable) product);
            }
        }
        return perishables;
    }

    public List<Shippable> shippableProducts() {
        List<Shippable> shipList = new ArrayList<>();
        var prods = getProductList();
        for (Product product : prods) {
            shipList.add((Shippable) product);
        }
        return shipList;
    }
}
