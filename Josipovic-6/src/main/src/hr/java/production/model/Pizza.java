package hr.java.production.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a type of edible item in a production system, specifically pizza.
 * Extends the {@code Item} class and implements the {@code Edible} interface.
 */
public class Pizza extends Item implements Edible {
    private static final Integer caloriesPerKilo = 2200;
    private BigDecimal weightInKG;

    /**
     * Constructs a new Pizza with the specified name, category, dimensions, cost, selling price, discount, and weight.
     *
     * @param name           The name of the pizza.
     * @param category       The category of the pizza.
     * @param width          The width of the pizza.
     * @param height         The height of the pizza.
     * @param length         The length of the pizza.
     * @param productionCost The cost to produce the pizza.
     * @param sellingPrice   The price at which the pizza is sold.
     * @param discount       The discount on the pizza.
     * @param weightInKG     The weight of the pizza in kilograms.
     */
    public Pizza(Long id,String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, BigDecimal weightInKG) {
        super(id,name, category, width, height, length, productionCost, sellingPrice, discount);
        this.weightInKG = weightInKG;
    }

    public BigDecimal getWeightInKG() {
        return weightInKG;
    }

    public void setWeightInKG(BigDecimal weightInKG) {
        this.weightInKG = weightInKG;
    }

    /**
     * Calculates the number of kilocalories in the pizza based on its weight.
     * The result is rounded to the nearest whole number using {@code RoundingMode.HALF_UP}.
     *
     * @return The {@code Integer} number of kilocalories in the pizza.
     */
    @Override
    public Integer calculateKilocalories() {
        return weightInKG.multiply(BigDecimal.valueOf(caloriesPerKilo)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * Calculates the price of the pizza after applying the discount and considering its weight.
     * It is assumed that the discount is applied to the price per kilogram, not the total price,
     * as the discount is applied to the superclass {@code Item}.
     * The result is rounded to the nearest cent using {@code RoundingMode.HALF_UP}.
     *
     * @return The discounted price of the pizza.
     */
    @Override
    public BigDecimal calculatePrice() {
        return weightInKG.multiply(getDiscountedSellingPrice()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(getWeightInKG(), pizza.getWeightInKG());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeightInKG());
    }

    @Override
    public String toString() {
        return "Pizza{" + "weightInKG=" + weightInKG + ", category=" + category + ", width=" + width + ", height=" + height + ", length=" + length + ", productionCost=" + productionCost + ", sellingPrice=" + sellingPrice + ", discount=" + discount + ", name='" + name + '\'' + '}';
    }
}
