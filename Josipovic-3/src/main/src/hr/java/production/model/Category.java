package hr.java.production.model;

import java.util.Objects;

/**
 * Organizes items in a production system by category.
 * Extends the {@code NamedEntity} class and includes a description.
 */
public class Category extends NamedEntity {
    private String description;

    /**
     * Constructs a new Category with the specified name and description.
     *
     * @param name The name of the category.
     * @param description The description of the category.
     */
    public Category(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * It first checks if the super class's equals method returns {@code true},
     * and then checks if the description is equal.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(getDescription(), category.getDescription());
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by {@code HashMap}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription());
    }

    /**
     * Returns a string representation of the Category instance.
     *
     * @return A string representation of this Category.
     */
    @Override
    public String toString() {
        return "Category{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
