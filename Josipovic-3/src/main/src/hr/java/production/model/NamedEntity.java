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

    /**
     * Returns a string representation of the NamedEntity instance.
     *
     * @return A string representation of this NamedEntity.
     */
    @Override
    public String toString() {
        return "NamedEntity{" + "name='" + name + '\'' + '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(getName(), that.getName());
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by {@code HashMap}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
