package hr.java.production.model;

import java.util.Arrays;

public class Factory extends NamedEntity {
    private Address address;
    private Item[] items;

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

    @Override
    public String toString() {
        return "Factory{" + "address=" + address + ", items=" + Arrays.toString(items) + ", name='" + name + '\'' + '}';
    }
}
