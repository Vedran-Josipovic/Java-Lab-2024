package hr.java.production.model;

import java.math.BigDecimal;

public final class Laptop extends Item implements Technical {
    private Integer warrantyYears;

    public Laptop(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Integer warrantyYears) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount);
        this.warrantyYears = warrantyYears;
    }

    @Override
    public Integer getRemainingWarrantyInMonths() {
        return warrantyYears * 12;
    }

    public Integer getWarrantyYears() {
        return warrantyYears;
    }

    public void setWarrantyYears(Integer warrantyYears) {
        this.warrantyYears = warrantyYears;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "warrantyYears=" + warrantyYears +
                ", category=" + category +
                ", width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", productionCost=" + productionCost +
                ", sellingPrice=" + sellingPrice +
                ", discount=" + discount +
                ", name='" + name + '\'' +
                '}';
    }
}
