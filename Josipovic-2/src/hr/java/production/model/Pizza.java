package hr.java.production.model;

import java.math.BigDecimal;

public class Pizza extends Item implements Edible{
    private static final Integer caloriesPerKilo = 2200;
    private BigDecimal weightInKG;

    public Pizza(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weightInKG) {
        super(name, category, width, height, length, productionCost, sellingPrice);
        this.weightInKG = weightInKG;
    }

    public BigDecimal getWeightInKG() {
        return weightInKG;
    }

    public void setWeightInKG(BigDecimal weightInKG) {
        this.weightInKG = weightInKG;
    }


    @Override
    public Integer calculateKilocalories() {
        return null;
    }

    @Override
    public BigDecimal calculatePrice() {
        return null;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "weightInKG=" + weightInKG +
                ", name='" + name + '\'' +
                '}';
    }
}
