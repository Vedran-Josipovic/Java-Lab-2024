package hr.java.production.main;

import hr.java.production.enumeration.Cities;
import hr.java.production.exception.CityNotSupportedException;
import hr.java.production.exception.IdenticalCategoryInputException;
import hr.java.production.exception.IdenticalItemChoiceException;
import hr.java.production.model.*;
import hr.java.production.sort.ProductionSorter;
import hr.java.production.utility.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static hr.java.production.utility.InputHandler.numInputHandler;
import static hr.java.production.utility.ObjectFinder.*;

/**
 * Initializes and manages the production of different items in various factories and stores.
 * Contains the logic for the main method as well as all other methods used in it. Also contains constants used throughout the entire class.
 * A Logger instance is used for logging application events.
 */
public class Main {
    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;
    private static final Integer PIZZA = 1, CHICKEN_NUGGETS = 2;
    private static final Integer FOOD = 1, LAPTOP = 2;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Starts the application and manages the production process.
     * <p>
     * Performs the following steps:
     * - Logs the start of the application. <p>
     * - Reads input data from a file. <p>
     * - Initializes categories, items, factories, and stores based on the input data. <p>
     * - Finds and prints information about the factory that produces the item with the greatest volume, the store that sells the cheapest item, the most caloric food, the highest priced food, and the laptop with the shortest warranty. <p>
     * - Groups items by category and by interface (Edible or Technical), and prints the cheapest and priciest items for each group. <p>
     * - Logs the end of the application. <p>
     *
     * @param args Command-line arguments. Not used in this application.
     * @throws FileNotFoundException If the input file is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        logger.info("Aplikacija započela s radom.");

        File file = new File("Josipovic-4/src/main/files/lab-4-input");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        List<Category> categories = inputCategories(scanner);
        List<Item> items = inputItems(scanner, categories);
        List<Factory> factories = inputFactories(scanner, items);
        List<Store> stores = inputStores(scanner, items);

        Factory bestFactory = findFactoryWithLargestVolumeOfAnItem(factories);
        System.out.println("The factory that produces an item with the greatest volume is: '" + bestFactory.getName() + "'.");

        Store bestStore = findStoreWithCheapestItem(stores);
        System.out.println("The store that sells an item with the cheapest price is: '" + bestStore.getName() + "'.");

        Item mostCaloricFood = findMostCaloricFood(items);
        if (mostCaloricFood instanceof Edible e)
            System.out.println("The food product with the most calories is " + mostCaloricFood.getName() + " [" + e.calculateKilocalories() + "]");

        Item highestPricedFood = findHighestPricedFood(items);
        if (highestPricedFood instanceof Edible e)
            System.out.println("The food product with the highest price (with discount) is " + highestPricedFood.getName() + " [" + e.calculatePrice() + "]");

        Item shortestWarrantyLaptop = findLaptopWithShortestWarranty(items);
        if (shortestWarrantyLaptop instanceof Technical t)
            System.out.println("The laptop with the shortest warranty is " + shortestWarrantyLaptop.getName() + " [" + t.getRemainingWarrantyInMonths() + "]");


        Map<Category, List<Item>> itemsPerCategoryMap = new HashMap<>();
        for (var i : items) itemsPerCategoryMap.computeIfAbsent(i.getCategory(), k -> new ArrayList<>()).add(i);
        printCheapestAndPriciestItemsByKey(itemsPerCategoryMap);


        Map<String, List<Item>> itemsPerInterfaceMap = new HashMap<>();
        for (Item i : items) {
            String key;
            if (i instanceof Edible) key = "Edible";
            else if (i instanceof Technical) key = "Technical";
            else continue;
            itemsPerInterfaceMap.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
        }
        printCheapestAndPriciestItemsByKey(itemsPerInterfaceMap);

        logger.info("Aplikacija završila.");
    }

    /**
     * Sorts and displays the most and least expensive items for each key in the provided map.
     * The items are sorted using the {@code ProductionSorter} comparator.
     * Logs the names of all items associated with each key and prints the most and least expensive items for each key.
     *
     * @param itemsPerKeyMap A map where each key is associated with a list of {@code Item} objects.
     */
    private static void printCheapestAndPriciestItemsByKey(Map<?, List<Item>> itemsPerKeyMap) {
        System.out.println();
        itemsPerKeyMap.forEach((key, valueItems) -> {
            valueItems.sort(new ProductionSorter());
            Item mostExpensive = valueItems.getLast(), leastExpensive = valueItems.getFirst();

            String mostExpensiveString = mostExpensive.getName() + " [" + mostExpensive.getDiscountedSellingPrice() + "]";
            String leastExpensiveString = leastExpensive.getName() + " [" + leastExpensive.getDiscountedSellingPrice() + "]";

            String keyName;
            if (key instanceof Category category) keyName = category.getName();
            else keyName = key.toString();

            String listNames = valueItems.stream().map(NamedEntity::getName).collect(Collectors.joining(", "));
            logger.debug("Key = [" + keyName + "]: Values = [" + listNames + "]");
            String msg = "Key = [" + keyName + "]: Most expensive: " + mostExpensiveString + ", Least expensive: " + leastExpensiveString;
            System.out.println(msg);
            logger.debug(msg);
        });
    }

    /**
     * Collects user input to create a list of categories.
     * <p>
     * Prompts the user to enter the name and description for each category.
     * It ensures that no two categories have the same name and description.
     * If a category already exists, the user is asked to enter a different category.
     *
     * @param scanner A Scanner object for reading user input.
     * @return A list of categories created based on user input.
     */
    private static List<Category> inputCategories(Scanner scanner) {
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < NUM_CATEGORIES; i++) {
            while (true) {
                System.out.println("Enter the information about the " + (i + 1) + ". category: ");

                System.out.print("Enter the category name: ");
                String name = scanner.nextLine();

                System.out.print("Enter the category description: ");
                String description = scanner.nextLine();

                try {
                    checkForIdenticalCategories(new Category(name, description), categories);
                } catch (IdenticalCategoryInputException e) {
                    logger.warn(e.getMessage());
                    System.out.println("This category [" + name + "] has already been added. Please choose a category that hasn't been added.");
                    continue;
                }
                categories.add(new Category(name, description));
                break;
            }
        }
        return categories;
    }

    /**
     * Checks if a category already exists in a list of categories.
     * <p>
     * Compares the input category with each category in the list.
     * If a category with the same name and description is found, an {@code IdenticalCategoryInputException} is thrown.
     *
     * @param categoryInput The category to check.
     * @param categories    The list of categories to check against.
     * @throws IdenticalCategoryInputException If the input category already exists in the list.
     */
    private static void checkForIdenticalCategories(Category categoryInput, List<Category> categories) throws IdenticalCategoryInputException {
        for (Category c : categories) {
            if (c.equals(categoryInput))
                throw new IdenticalCategoryInputException("Entered category [" + categoryInput.getName() + "] has already been added. Added Categories: " + categories.stream().map(Category::getName).collect(Collectors.joining(", ")));
        }
    }

    /**
     * Collects user input to create a list of items. <p>
     * <p>
     * Prompts the user to enter the details for each item, including its name, category, dimensions, production cost, selling price, and discount.
     * If the item is food, the user can specify whether it's pizza or chicken nuggets and input its weight.
     * If the item is a laptop, the user can input its warranty duration.
     * After each item is created, if it's edible, it prints out its kilocalories and price with discount.
     * The created items are added to a list which is returned at the end.
     *
     * @param scanner    A Scanner object for reading user input.
     * @param categories The list of categories to choose from when creating an item.
     * @return A list of items created based on user input.
     */
    private static List<Item> inputItems(Scanner scanner, List<Category> categories) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < NUM_ITEMS; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". item: ");

            System.out.print("Enter the item name: ");
            String name = scanner.nextLine();

            System.out.println("Pick the item category:");

            for (int j = 0; j < categories.size(); j++)
                System.out.println((j + 1) + ". " + categories.get(j).getName());
            int categoryChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, categories.size());

            System.out.println("Enter the item dimensions:");
            BigDecimal width = numInputHandler(scanner, "Enter the item width: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal height = numInputHandler(scanner, "Enter the item height: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal length = numInputHandler(scanner, "Enter the item length: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal productionCost = numInputHandler(scanner, "Enter the item production cost: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal sellingPrice = numInputHandler(scanner, "Enter the item selling price: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal discountAmount = numInputHandler(scanner, "Enter the discount percentage for this item: ", BigDecimal.ZERO, BigDecimal.valueOf(100));
            Discount discount = new Discount(discountAmount);

            int itemSubclassChoice = InputHandler.numInputHandler(scanner, "Is this item food, a laptop, or other:\n1. Food\n2. Laptop\n3. Other\nChoice >> ", 1, 3);
            if (itemSubclassChoice == FOOD) {
                Integer foodChoice = InputHandler.numInputHandler(scanner, "Pick an available food product:\n1. Pizza\n2. Chicken nuggets\nChoice >> ", PIZZA, CHICKEN_NUGGETS);
                BigDecimal weightInKG = numInputHandler(scanner, "Enter the weight (in kg) of the food packet: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));

                if (foodChoice.equals(PIZZA))
                    items.add(new Pizza(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, weightInKG));
                else if (foodChoice.equals(CHICKEN_NUGGETS))
                    items.add(new ChickenNuggets(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, weightInKG));
            } else if (itemSubclassChoice == LAPTOP) {
                Integer warrantyYears = InputHandler.numInputHandler(scanner, "Enter the warranty duration (in years) of the laptop: ", 0, 100);
                items.add(new Laptop(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, warrantyYears));
            } else {
                items.add(new Item(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount));
            }

            if (items.getLast() instanceof Edible e) {
                System.out.println("Kilocalories in " + items.getLast().getName() + ": " + e.calculateKilocalories());
                System.out.println("Price (with " + items.getLast().getDiscount().discountAmount() + "% discount) for " + items.getLast().getName() + ": " + e.calculatePrice());
            }
        }
        return items;
    }

    /**
     * Prompts the user to input information for a specified number of factories.
     * The user can choose the name, address, and items produced by each factory.
     * Ensures that no two factories produce the same item.
     *
     * @param scanner A {@code Scanner} object for user input.
     * @param items   The list of items to choose from when creating a factory.
     * @return A list of factories created based on user input.
     */
    private static List<Factory> inputFactories(Scanner scanner, List<Item> items) {
        List<Factory> factories = new ArrayList<>();
        List<Item> addedItems = new ArrayList<>();
        for (int i = 0; i < NUM_FACTORIES; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". factory: ");
            System.out.print("Enter the factory name: ");
            String name = scanner.nextLine();
            System.out.println("Enter the factory address information: ");
            Address address = inputAddress(scanner);
            System.out.println("Pick which items the factory produces: ");

            Set<Item> factoryItems = chooseItems(scanner, items, addedItems);
            factories.add(new Factory(name, address, factoryItems));
        }
        return factories;
    }

    /**
     * Prompts the user to input information for a specified number of stores.
     * User can choose the name, web address, and items sold by each store.
     * Ensures that no two stores sell the same item.
     *
     * @param scanner A {@code Scanner} object for user input.
     * @param items   The list of items to choose from when creating a store.
     * @return A list of stores created based on user input.
     */
    private static List<Store> inputStores(Scanner scanner, List<Item> items) {
        List<Store> stores = new ArrayList<>();
        List<Item> addedItems = new ArrayList<>();
        for (int i = 0; i < NUM_STORES; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". store: ");
            System.out.print("Enter the store name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the store web address: ");
            String webAddress = scanner.nextLine();

            System.out.println("Pick which items the store sells: ");

            Set<Item> storeItems = chooseItems(scanner, items, addedItems);
            stores.add(new Store(name, webAddress, storeItems));
        }
        return stores;
    }

    /**
     * Allows the user to select items from a given list, ensuring that no item is selected more than once.
     * <p>
     * Prompts the user to choose items until they have either chosen all available items or decided to stop choosing.
     * It ensures that no item is chosen more than once by checking each chosen item against a list of already chosen items.
     *
     * @param scanner    A Scanner object for reading user input.
     * @param items      The list of items to choose from.
     * @param addedItems The list of items that have already been chosen.
     * @return A set of items chosen by the user.
     */
    private static Set<Item> chooseItems(Scanner scanner, List<Item> items, List<Item> addedItems) {
        Set<Item> chosenItemSet = new HashSet<>();
        boolean finishedChoosing = false, isFirstRun = true;
        while (!finishedChoosing) {
            if (items.size() == addedItems.size()) {
                logger.warn("All available items have been added. Possibly returning empty array.");
                System.out.println("All available items have been added.");
                break;
            }
            printAvailableItems(items, isFirstRun);
            int itemChoice;
            if (isFirstRun) itemChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, items.size());
            else itemChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, items.size() + 1);

            if (itemChoice != items.size() + 1) {
                try {
                    checkForIdenticalItems(items.get(itemChoice - 1), addedItems);
                } catch (IdenticalItemChoiceException e) {
                    logger.warn(e.getMessage());
                    System.out.println("This item [" + items.get(itemChoice - 1).getName() + "] has already been added. Please choose an item that isn't in this list: " + addedItems.stream().map(Item::getName).collect(Collectors.joining(", ")));
                    continue;
                }
                chosenItemSet.add(items.get(itemChoice - 1));
                //addedItems mijenja se i van metode
                addedItems.add(items.get(itemChoice - 1));
            } else finishedChoosing = true;
            isFirstRun = false;
        }
        return chosenItemSet;
    }

    /**
     * Displays a list of items for the user to choose from.
     * If this is not the first run, it also prints an option for the user to finish choosing.
     *
     * @param items      The list of items to choose from.
     * @param isFirstRun A boolean indicating whether this is the first run of the method.
     */
    private static void printAvailableItems(List<Item> items, boolean isFirstRun) {
        System.out.println("Choose an item:");
        for (int i = 0; i < items.size(); i++) System.out.println((i + 1) + ". " + items.get(i).getName());
        if (!isFirstRun) System.out.println(items.size() + 1 + ". " + "Finished choosing.");
    }

    /**
     * Checks if an item has already been added to a list.
     * It throws an IdenticalItemChoiceException if the item has already been added.
     *
     * @param itemChoice The Item object to check.
     * @param addedItems A list of Item objects that have already been added.
     * @throws IdenticalItemChoiceException If the item has already been added to the list.
     */
    private static void checkForIdenticalItems(Item itemChoice, List<Item> addedItems) throws IdenticalItemChoiceException {
        if (addedItems.contains(itemChoice))
            throw new IdenticalItemChoiceException("Chosen item [" + itemChoice.getName() + "] has already been added. Added Items: " + addedItems.stream().map(Item::getName).collect(Collectors.joining(", ")));
    }

    /**
     * Collects user input to create an {@code Address} object.
     * <p>
     * Prompts the user to enter the street name, house number, and city.
     * It ensures that the entered city is valid. If the city is not valid, the user is asked to enter a different city.
     *
     * @param scanner A Scanner object for reading user input.
     * @return An {@code Address} object created based on user input.
     */
    private static Address inputAddress(Scanner scanner) {
        System.out.print("Enter the street name: ");
        String street = scanner.nextLine();

        System.out.print("Enter the house number: ");
        String houseNumber = scanner.nextLine();

        Cities city;
        while (true) {
            try {
                city = inputCity(scanner);
                break;
            } catch (CityNotSupportedException e) {
                logger.error(e.getMessage());
                System.out.println("Entered city is not in our database. Please enter a valid city.");
            }
        }

        return new Address.Builder().atStreet(street).atHouseNumber(houseNumber).atCity(city).build();
    }

    /**
     * Collects user input to determine the city.
     * <p>
     * Prompts the user to enter the name of a city. It checks if the entered city is in the list of supported cities.
     * If the city is not supported, a {@code CityNotSupportedException} is thrown.
     *
     * @param scanner A Scanner object for reading user input.
     * @return A {@code Cities} enum value representing the entered city.
     * @throws CityNotSupportedException If the entered city is not supported.
     */
    private static Cities inputCity(Scanner scanner) throws CityNotSupportedException {
        System.out.print("Enter the city name: ");
        String name = scanner.nextLine();

        return switch (name) {
            case "Zagreb" -> Cities.ZAGREB;
            case "Split" -> Cities.SPLIT;
            case "Rijeka" -> Cities.RIJEKA;
            case "Osijek" -> Cities.OSIJEK;
            case "Zadar" -> Cities.ZADAR;
            case "Slavonski Brod" -> Cities.SLAVONSKI_BROD;
            case "Velika Gorica" -> Cities.VELIKA_GORICA;
            default ->
                    throw new CityNotSupportedException("City not in the database: [" + name + "]. Cities in enums: [" + Arrays.stream(Cities.values()).map(Cities::getName).collect(Collectors.joining(", ")) + "]");
        };
    }

}
