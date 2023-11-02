package hr.java.production.model;

import java.util.Objects;
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
        return "Factory{" +
                "address=" + address +
                ", items=" + items +
                ", name='" + name + '\'' +
                '}';
    }
}
