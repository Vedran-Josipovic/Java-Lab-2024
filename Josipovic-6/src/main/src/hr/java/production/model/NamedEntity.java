package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an abstract entity with a name.
 * This class serves as a base for all entities that have a name.
 */
public abstract class NamedEntity implements Serializable {
    protected Long id;
    protected String name;


    public NamedEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(getName(), that.getName()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId());
    }

    @Override
    public String toString() {
        return "NamedEntity{" + "name='" + name + '\'' + ", id=" + id + '}';
    }
}
