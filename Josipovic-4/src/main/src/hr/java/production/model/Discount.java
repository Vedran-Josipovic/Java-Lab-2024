package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Represents a discount amount in percentages for an item in a production system.
 */
public record Discount(BigDecimal discountAmount) {
}
