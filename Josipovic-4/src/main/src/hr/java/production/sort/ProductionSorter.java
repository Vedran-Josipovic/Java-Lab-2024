package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

public class ProductionSorter implements Comparator<Item> {
    /**
     * Uzlazno sortira (Od najmanjeg prema najvećem)
     *
     * @param i1 the first object to be compared.
     * @param i2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Item i1, Item i2) {
        return i1.getDiscountedSellingPrice().compareTo(i2.getDiscountedSellingPrice());
    }
}
