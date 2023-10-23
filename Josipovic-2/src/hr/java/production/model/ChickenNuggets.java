package hr.java.production.model;

import java.math.BigDecimal;

public class ChickenNuggets extends Item implements Edible {
    private static final Integer caloriesPerKilo = 2970;

    private BigDecimal weightInKG;

    public ChickenNuggets(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, BigDecimal weightInKG) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount);
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
        BigDecimal totalCalories = weightInKG.multiply(BigDecimal.valueOf(caloriesPerKilo));
        return totalCalories.toBigInteger().intValue();
    }

    /**
     * Pretpostavlja se da se popust obračunava na cijenu po kili, a ne na ukupnu cijenu s obzirom da se discount primjenjuje na nadklasu Item
     * Računa cijenu s popustom
     */
    @Override
    public BigDecimal calculatePrice() {
        return weightInKG.multiply(getDiscountedSellingPrice());
    }

    @Override
    public String toString() {
        return "ChickenNuggets{" +
                "weightInKG=" + weightInKG +
                ", category=" + category +
                ", width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", productionCost=" + productionCost +
                ", sellingPrice=" + sellingPrice +
                ", discount=" + discount + "%" +
                ", name='" + name + '\'' +
                '}';
    }
}
