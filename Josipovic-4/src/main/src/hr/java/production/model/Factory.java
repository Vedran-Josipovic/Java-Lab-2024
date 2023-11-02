package hr.java.production.model;

import java.util.Set;


public class Factory extends NamedEntity {
    private Address address;
    private Set<Item> items;

    public Factory(String name, Address address, Set<Item> items) {
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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }


}
