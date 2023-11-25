package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Organizes items in a production system by category.
 * Extends the {@code NamedEntity} class and includes a description.
 */
public class Category extends NamedEntity implements Serializable {
    private String description;

    /**
     * Constructs a new Category with the specified name and description.
     *
     * @param name        The name of the category.
     * @param description The description of the category.
     */
    public Category(Long id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(getDescription(), category.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription());
    }

    @Override
    public String toString() {
        return "Category{" + "description='" + description + '\'' + ", name='" + name + '\'' + '}';
    }
}
