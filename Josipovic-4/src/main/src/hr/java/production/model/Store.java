package hr.java.production.model;

import java.util.Objects;
import java.util.Set;


public class Store extends NamedEntity {
    private String webAddress;
    private Set<Item> items;

    public Store(String name, String webAddress, Set<Item> items) {
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
        Store store = (Store) o;
        return Objects.equals(getWebAddress(), store.getWebAddress()) && Objects.equals(getItems(), store.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWebAddress(), getItems());
    }

    @Override
    public String toString() {
        return "Store{" +
                "webAddress='" + webAddress + '\'' +
                ", items=" + items +
                ", name='" + name + '\'' +
                '}';
    }
}
