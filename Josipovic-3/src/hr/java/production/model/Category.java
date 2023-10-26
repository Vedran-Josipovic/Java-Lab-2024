package hr.java.production.model;

/**
 * Represents the category of an item in a production system.
 */
public class Category extends NamedEntity {
    private String description;

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

    @Override
    public String toString() {
        return "Category{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
