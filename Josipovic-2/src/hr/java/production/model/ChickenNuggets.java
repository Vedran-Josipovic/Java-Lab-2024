package hr.java.production.model;

import java.math.BigDecimal;

public class ChickenNuggets extends Item implements Edible {
    private static final Integer caloriesPerKilo = 2970;

    private BigDecimal weightInKG;

    public ChickenNuggets(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weightInKG) {
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
        return "ChickenNuggets{" +
                "weightInKG=" + weightInKG +
                ", name='" + name + '\'' +
                '}';
    }
}
