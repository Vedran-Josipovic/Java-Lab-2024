package hr.java.production.model;

import java.util.Arrays;

/**
 * Represents a factory in a production system.
 * Extends the {@code NamedEntity} class.
 */
public class Factory extends NamedEntity {
    private Address address;
    private Item[] items;

    /**
     * Constructs a new Factory with the specified name, address, and items.
     *
     * @param name The name of the factory.
     * @param address The address of the factory.
     * @param items The items produced by the factory.
     */
    public Factory(String name, Address address, Item[] items) {
        super(name);
        this.address = address;
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    /**
     * Returns a string representation of this Factory instance.
     *
     * @return A string representation of this Factory instance.
     */
    @Override
    public String toString() {
        return "Factory{" + "address=" + address + ", items=" + Arrays.toString(items) + ", name='" + name + '\'' + '}';
    }
}
