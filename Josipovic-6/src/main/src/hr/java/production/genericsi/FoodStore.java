package hr.java.production.genericsi;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FoodStore <T extends Edible> extends Store {
    private List<T> foodStoreItems;

    public FoodStore(Long id, String name, String webAddress, Set<Item> items, List<T> foodStoreItems) {
        super(id, name, webAddress, items);
        this.foodStoreItems = foodStoreItems;
    }

    public void addFoodStoreItem(T item){
        foodStoreItems.add(item);
    }

    public List<T> getFoodStoreItems() {
        return foodStoreItems;
    }

    public void setFoodStoreItems(List<T> foodStoreItems) {
        this.foodStoreItems = foodStoreItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FoodStore<?> foodStore = (FoodStore<?>) o;
        return Objects.equals(getFoodStoreItems(), foodStore.getFoodStoreItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFoodStoreItems());
    }

    @Override
    public String toString() {
        return "FoodStore{" +
                "foodStoreItems=" + foodStoreItems +
                ", name='" + name + '\'' +
                '}';
    }
}
