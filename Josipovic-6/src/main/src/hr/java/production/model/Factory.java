package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a factory in the production model.
 * Extends the NamedEntity class.
 */
public class Factory extends NamedEntity implements Serializable, ItemContainer {
    private Address address;
    private Set<Item> items;



    /**
     * Constructs a new Factory with the specified name, address, and items.
     *
     * @param name    The name of the factory.
     * @param address The address of the factory.
     * @param items   The items produced by the factory.
     */
    public Factory(Long id, String name, Address address, Set<Item> items) {
        super(id, name);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Factory factory = (Factory) o;
        return Objects.equals(getAddress(), factory.getAddress()) && Objects.equals(getItems(), factory.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAddress(), getItems());
    }

    @Override
    public String toString() {
        return "Factory{" + "address=" + address + ", items=" + items + ", name='" + name + '\'' + '}';
    }
}
