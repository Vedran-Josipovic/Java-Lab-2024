package hr.java.production.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a type of technical good in a production system, specifically a laptop.
 * Extends the {@code Item} class and implements the {@code Technical} interface.
 */
public final class Laptop extends Item implements Technical {
    private Integer warrantyYears;

    /**
     * Constructs a new Laptop with the specified name, category, dimensions, cost, selling price, discount, and warranty years.
     *
     * @param name           The name of the laptop.
     * @param category       The category of the laptop.
     * @param width          The width of the laptop.
     * @param height         The height of the laptop.
     * @param length         The length of the laptop.
     * @param productionCost The cost to produce the laptop.
     * @param sellingPrice   The price at which the laptop is sold.
     * @param discount       The discount on the laptop.
     * @param warrantyYears  The warranty of the laptop in years.
     */
    public Laptop(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Integer warrantyYears) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount);
        this.warrantyYears = warrantyYears;
    }

    public Integer getWarrantyYears() {
        return warrantyYears;
    }

    public void setWarrantyYears(Integer warrantyYears) {
        this.warrantyYears = warrantyYears;
    }

    /**
     * Calculates the remaining warranty for the laptop in months based on its warranty in years.
     *
     * @return The remaining warranty for the laptop in months.
     */
    @Override
    public Integer getRemainingWarrantyInMonths() {
        return warrantyYears * 12;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * It first checks if the super class's equals method returns {@code true},
     * and then checks if the warranty years are equal.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return Objects.equals(getWarrantyYears(), laptop.getWarrantyYears());
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by {@code HashMap}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWarrantyYears());
    }

    /**
     * Returns a string representation of this Laptop instance.
     *
     * @return A string representation of this Laptop instance.
     */
    @Override
    public String toString() {
        return "Laptop{" + "warrantyYears=" + warrantyYears + ", category=" + category + ", width=" + width + ", height=" + height + ", length=" + length + ", productionCost=" + productionCost + ", sellingPrice=" + sellingPrice + ", discount=" + discount + ", name='" + name + '\'' + '}';
    }
}
