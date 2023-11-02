package hr.java.production.model;

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


}
