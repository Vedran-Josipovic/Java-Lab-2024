package hr.java.production.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents an item in a production system.
 */
public class Item extends NamedEntity {
    protected Category category;
    protected BigDecimal width, height, length, productionCost, sellingPrice;
    protected Discount discount;

    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount) {
        super(name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountedSellingPrice() {
        return sellingPrice.subtract(discount.discountAmount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(sellingPrice));
    }

    public BigDecimal calculateVolume() {
        return width.multiply(height).multiply(length);
    }

    @Override
    public String toString() {
        return "Item{" + "category=" + category + ", width=" + width + ", height=" + height + ", length=" + length + ", productionCost=" + productionCost + ", sellingPrice=" + sellingPrice + ", discount=" + discount + "%" + ", name='" + name + '\'' + '}';
    }
}
