package hr.java.production.model;

import java.util.Arrays;

/**
 * Represents a store in a production system in which items produced by factories are sold.
 * Extends the {@code NamedEntity} class.
 */
public class Store extends NamedEntity {
    private String webAddress;
    private Item[] items;

    /**
     * Constructs a new Store with the specified name, web address, and items.
     *
     * @param name The name of the store.
     * @param webAddress The web address of the store.
     * @param items The items sold by the store.
     */
    public Store(String name, String webAddress, Item[] items) {
        super(name);
        this.webAddress = webAddress;
        this.items = items;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    /**
     * Returns a string representation of this Store instance.
     *
     * @return A string representation of this Store instance.
     */
    @Override
    public String toString() {
        return "Store{" + "webAddress='" + webAddress + '\'' + ", items=" + Arrays.toString(items) + ", name='" + name + '\'' + '}';
    }
}
