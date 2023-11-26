package hr.java.production.model;

import java.util.Set;

/**
 * Represents a generic container of {@link Item} objects.
 * <p>
 * This interface is designed to be implemented by various entities in the production model
 * that can contain items, such as {@link Store} and {@link Factory}.
 * It provides a common contract to access the items contained within these entities.
 * </p>
 * Implementing classes are expected to provide their own specific implementation
 * of how items are stored and retrieved.
 */
public interface ItemContainer {
    Set<Item> getItems();
    String getName();
}
