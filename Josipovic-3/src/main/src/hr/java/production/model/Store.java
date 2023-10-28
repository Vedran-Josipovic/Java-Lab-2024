package hr.java.production.model;

import java.util.Arrays;

public class Store extends NamedEntity {
    private String webAddress;
    private Item[] items;

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

    @Override
    public String toString() {
        return "Store{" + "webAddress='" + webAddress + '\'' + ", items=" + Arrays.toString(items) + ", name='" + name + '\'' + '}';
    }
}
