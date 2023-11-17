package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Provides methods for calculating kilocalories and price for edible items in a production system.
 */
public interface Edible {
    /**
     * Calculates the {@code Integer} number of kilocalories for an edible item.
     *
     * @return The number of kilocalories for an edible item.
     */
    Integer calculateKilocalories();

    /**
     * Calculates the price for the edible item, taking into account its weight and any applicable discount.
     *
     * @return The price for the edible item.
     */
    BigDecimal calculatePrice();
}
