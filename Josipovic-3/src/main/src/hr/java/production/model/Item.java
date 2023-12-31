package hr.java.production.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents items in a production system by category, dimensions, cost, selling price, and discount.
 * Extends the {@code NamedEntity} class.
 */
public class Item extends NamedEntity {
    protected Category category;
    protected BigDecimal width, height, length, productionCost, sellingPrice;
    protected Discount discount;

    /**
     * Constructs a new Item with the specified name, category, dimensions, cost, selling price, and discount percentage.
     *
     * @param name The name of the item.
     * @param category The category of the item.
     * @param width The width of the item.
     * @param height The height of the item.
     * @param length The length of the item.
     * @param productionCost The cost to produce the item.
     * @param sellingPrice The price at which the item is sold.
     * @param discount The discount percentage on the item.
     */
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


    /**
     * Calculates the selling price of the item after applying the discount.
     *
     * @return The discounted selling price of the item.
     */
    public BigDecimal getDiscountedSellingPrice() {
        return sellingPrice.subtract(discount.discountAmount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(sellingPrice));
    }

    /**
     * Calculates the volume of the item based on its dimensions.
     *
     * @return The volume of the item.
     */
    public BigDecimal calculateVolume() {
        return width.multiply(height).multiply(length);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * It first checks if the super class's equals method returns {@code true},
     * and then checks if the category and all other fields are equal.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return Objects.equals(getCategory(), item.getCategory()) && Objects.equals(getWidth(), item.getWidth()) && Objects.equals(getHeight(), item.getHeight()) && Objects.equals(getLength(), item.getLength()) && Objects.equals(getProductionCost(), item.getProductionCost()) && Objects.equals(getSellingPrice(), item.getSellingPrice()) && Objects.equals(getDiscount(), item.getDiscount());
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by {@code HashMap}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategory(), getWidth(), getHeight(), getLength(), getProductionCost(), getSellingPrice(), getDiscount());
    }

    /**
     * Returns a string representation of the Item instance.
     *
     * @return A string representation of this Item.
     */
    @Override
    public String toString() {
        return "Item{" + "category=" + category + ", width=" + width + ", height=" + height + ", length=" + length + ", productionCost=" + productionCost + ", sellingPrice=" + sellingPrice + ", discount=" + discount + "%" + ", name='" + name + '\'' + '}';
    }
}
