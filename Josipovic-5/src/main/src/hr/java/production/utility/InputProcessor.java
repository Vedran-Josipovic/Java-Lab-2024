package hr.java.production.utility;

import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.util.List;
import java.util.Scanner;

/**
 * Used for processing user input.
 * <p>
 * Provides methods for reading and validating user input to create various objects such as categories, items, factories, and stores.
 */
public interface InputProcessor {
    /**
     * Collects user input to create a list of categories.
     *
     * @param scanner A Scanner object for reading user input.
     * @return A list of categories created based on user input.
     */
    List<Category> inputCategories(Scanner scanner);

    /**
     * Collects user input to create a list of items.
     *
     * @param scanner    A Scanner object for reading user input.
     * @param categories The list of categories to choose from when creating an item.
     * @return A list of items created based on user input.
     */
    List<Item> inputItems(Scanner scanner, List<Category> categories);

    /**
     * Collects user input to create a list of factories.
     *
     * @param scanner A Scanner object for reading user input.
     * @param items   The list of items to choose from when creating a factory.
     * @return A list of factories created based on user input.
     */
    List<Factory> inputFactories(Scanner scanner, List<Item> items);

    /**
     * Collects user input to create a list of stores.
     *
     * @param scanner A Scanner object for reading user input.
     * @param items   The list of items to choose from when creating a store.
     * @return A list of stores created based on user input.
     */
    List<Store> inputStores(Scanner scanner, List<Item> items);
}
