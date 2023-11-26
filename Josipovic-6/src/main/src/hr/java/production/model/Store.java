package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a store in the production model.
 * Extends the NamedEntity class.
 */
public class Store extends NamedEntity implements Serializable, ItemContainer {
    private String webAddress;
    private Set<Item> items;

    /**
     * Constructs a new Store with the specified name, web address, and items.
     *
     * @param name       The name of the store.
     * @param webAddress The web address of the store.
     * @param items      The items sold by the store.
     */
    public Store(Long id, String name, String webAddress, Set<Item> items) {
        super(id, name);
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
        return "Store{" + "webAddress='" + webAddress + '\'' + ", items=" + items + ", name='" + name + '\'' + '}';
    }
}
