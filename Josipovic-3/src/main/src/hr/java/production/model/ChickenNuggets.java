package hr.java.production.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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
        return weightInKG.multiply(BigDecimal.valueOf(caloriesPerKilo)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * Pretpostavlja se da se popust obračunava na cijenu po kili, a ne na ukupnu cijenu s obzirom da se discount primjenjuje na nadklasu Item
     * Računa cijenu s popustom
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
        ChickenNuggets that = (ChickenNuggets) o;
        return Objects.equals(getWeightInKG(), that.getWeightInKG());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeightInKG());
    }

    @Override
    public String toString() {
        return "ChickenNuggets{" + "weightInKG=" + weightInKG + ", category=" + category + ", width=" + width + ", height=" + height + ", length=" + length + ", productionCost=" + productionCost + ", sellingPrice=" + sellingPrice + ", discount=" + discount + "%" + ", name='" + name + '\'' + '}';
    }
}
