package hr.java.production.model;

import java.math.BigDecimal;

public interface Edible {

    /**
     * Vraća broj kilokalorija (cijeli broj) koji ima navedena namirnica koja je jestiva.
     */
    Integer calculateKilocalories();

    /**
     * Vraća cijenu za namirnicu koja će se izračunavati prema težini.
     * Također uračunava popust.
     */
    BigDecimal calculatePrice();

}
