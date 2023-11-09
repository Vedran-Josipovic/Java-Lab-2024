package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

/**
 * Sorts items in based on their discounted selling price.
 * By default, the items are sorted in ascending order (From the cheapest to the priciest).
 * If a {@code reversed()} method is called on the sorter, the items get sorted in descending order.
 */
public class ProductionSorter implements Comparator<Item> {

    /**
     * Compares two items based on their discounted selling price.
     *
     * @param i1 The first item to be compared.
     * @param i2 The second item to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @deprecated For edible items the price is calculated per kilo ({@code getDiscountedSellingPrice}),
     * and not by taking into account the weight like the {@code calculatePrice} method
     * defined in the {@code Edible} interface does. Might want to change that in the future.
     */
    @Override
    public int compare(Item i1, Item i2) {
        return i1.getDiscountedSellingPrice().compareTo(i2.getDiscountedSellingPrice());
    }
}
