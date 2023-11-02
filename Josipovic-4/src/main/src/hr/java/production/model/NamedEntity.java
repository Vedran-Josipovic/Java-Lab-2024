package hr.java.production.model;

import java.util.Objects;

/**
 * Represents an abstract entity with a name.
 * This class serves as a base for all entities that have a name.
 */
public abstract class NamedEntity {
    protected String name;

    /**
     * Constructs a new NamedEntity with the specified name.
     *
     * @param name The name of the entity.
     */
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "NamedEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
