package hr.java.production.model;

/**
 * Provides a method for calculating the remaining warranty in months for technical goods.
 */
public sealed interface Technical permits Laptop {
    /**
     * Calculates the remaining warranty for the technical good in months.
     *
     * @return The remaining warranty for the technical good in months.
     */
    Integer getRemainingWarrantyInMonths();
}
