package hr.java.production.genericsi;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TechnicalStore<T extends Technical> extends Store {
    private List<T> technicalStoreItems;

    public TechnicalStore(Long id, String name, String webAddress, Set<Item> items, List<T> technicalStores) {
        super(id, name, webAddress, items);
        this.technicalStoreItems = technicalStores;
    }

    public void addTechnicalStoreItem(T item) {
        technicalStoreItems.add(item);
    }

    public List<T> getTechnicalStoreItems() {
        return technicalStoreItems;
    }

    public void setTechnicalStoreItems(List<T> technicalStoreItems) {
        this.technicalStoreItems = technicalStoreItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TechnicalStore<?> that = (TechnicalStore<?>) o;
        return Objects.equals(getTechnicalStoreItems(), that.getTechnicalStoreItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTechnicalStoreItems());
    }

    @Override
    public String toString() {
        return "TechnicalStore{" +
                "technicalStoreItems=" + technicalStoreItems +
                ", name='" + name + '\'' +
                '}';
    }
}
