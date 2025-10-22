package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {
    private static final Map<String, Warehouse> singleInstancePerName = new ConcurrentHashMap<>();
    private final Set<Product> changedSet = new HashSet<>();
    private final Map<Category, List<Product>> categoryMap = new HashMap<>();


    private Warehouse(String name) {
    }

    public synchronized static Warehouse getInstance(String name) {
        return singleInstancePerName.computeIfAbsent(name, Warehouse::new);
    }


    public synchronized static Warehouse getInstance() {
        return singleInstancePerName.computeIfAbsent("Default", Warehouse::new);
    }

    public synchronized void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        checkForAlreadyExistentId(product);
        categoryMap.putIfAbsent(product.category(), new ArrayList<>());
        categoryMap.get(product.category()).add(product);
    }

    /**
     * Checks if a product in the warehouse has the same ID as the product.
     * Throws IllegalArgumentException if ID is found.
     * Void if product ID isn't in the warehouse.
     *
     * @param product about to be added in the warehouse.
     */
    private void checkForAlreadyExistentId(Product product) {
        for (var p : getProducts()) {
            if (p.uuid().equals(product.uuid())) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }
    }


    /**
     * Removes a product if it exists in the warehouse.
     * if there are only the product to be removed in one category
     * the category will be removed.
     *
     * @param id for the product to be removed
     */
    public synchronized void remove(UUID id) {
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
        } else System.out.println("product not found with UUID: " + id);
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
    public synchronized void clearProducts() {
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

    /**
     * Updates a products price already added to the warehouse.
     * Takes UUID as key and BigDecimal for new price.
     * Also adds product to changed product list.
     * @param id Product to update
     * @param price New price for the product
     */
    public synchronized void updateProductPrice(UUID id, BigDecimal price) {
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
