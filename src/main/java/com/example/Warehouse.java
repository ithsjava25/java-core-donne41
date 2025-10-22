package com.example;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private static Map<String, Warehouse> singleInstancePerName = new HashMap<>();
    private final Set<Product> changedSet = new HashSet<>();
    private final Map<Category, List<Product>> categoryMap = new HashMap<>();


    private Warehouse(String name) {
    }

    public static Warehouse getInstance(String name) {
        return singleInstancePerName.computeIfAbsent(name, Warehouse::new);
    }


    public static Warehouse getInstance() {
        return singleInstancePerName.computeIfAbsent("Default", Warehouse::new);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        checkForAlreadyExistentId(product);
        categoryMap.putIfAbsent(product.category(), new ArrayList<>());
        categoryMap.get(product.category()).add(product);
    }

    /**
     * Checks if a product in the warehouse has the same ID as the product.
     * Throws IlleagalArgumentException if ID is found.
     * Void if product ID isn't in the warehouse.
     * @param product about to be added in the warehouse.
     */
    private void checkForAlreadyExistentId(Product product) {
        for (var p : getProducts()) {
            if (p.uuid().equals(product.uuid())) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }
    }


    public List<Product> findOutliners() {
        var productList = getProducts();
        List<Product> outliners = new ArrayList<>();
        int medianIndex = (int) ((productList.size()-1) / 2.0 + 0.5);
        int lowerQIndex = (int) ((productList.size()-1) / 4.0 + 0.5);
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

    /**
     * Removes a product if it exists in the warehouse.
     * @param id for the product to be removed
     */
    public void remove(UUID id) {
        Category tempCat = null;
        boolean found = false;
        for (Product product : getProducts()) {
            if (product.uuid().equals(id)) {
                tempCat = product.category();
                found = true;
            }
        }
        if (found) {
            List<Product> categoryList = categoryMap.get(tempCat);
            if (categoryList.size() == 1) {
                categoryMap.remove(tempCat);
            }
            categoryList.stream().filter(p -> p.uuid()
                            .equals(id))
                    .findFirst()
                    .ifPresent(categoryList::remove);

            categoryMap.replace(tempCat, categoryList);
        }else System.out.println("product not found with UUID: " + id);
    }

    /**
     * Get all product from the warehouse.
     *
     * @return Unmodifiable list.
     */
    public List<Product> getProducts() {
        if (categoryMap.isEmpty()) {
            return List.of();
        }
        List<Product> list = new ArrayList<>();
        categoryMap.values()
                .forEach(list::addAll);
        return List.copyOf(list);
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
        Product updateProduct = getProducts().stream()
                .filter(p -> p.uuid()
                        .equals(id))
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
        if (categoryMap.isEmpty()) return true;
        return false;
    }

    public List<Perishable> expiredProducts() {
        List<Perishable> perishables = new ArrayList<>();
        for (Product product : getProducts()) {
            if (product instanceof Perishable && ((Perishable) product).isExpired()) {
                perishables.add((Perishable) product);
            }
        }
        return perishables;
    }

    public List<Shippable> shippableProducts() {
        List<Shippable> shipList = new ArrayList<>();
        for (Product product : getProducts()) {
            shipList.add((Shippable) product);
        }
        return shipList;
    }
}
