package hr.java.production.model;

/**
 * Predstavlja tehničku robu kao artikl.
 */
public sealed interface Technical permits Laptop {
    /**
     * @return Trajanje garantnog roka tehničke robe kao artikla (cjelobrojnu vrijednost u mjesecima).
     */
    Integer getRemainingWarrantyInMonths();
}
