package hr.java.production.model;

public abstract class NamedEntity {
    protected String name;

    public NamedEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NamedEntity{" + "name='" + name + '\'' + '}';
    }
}
