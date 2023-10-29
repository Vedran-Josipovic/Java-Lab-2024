package hr.java.production.main;

import hr.java.production.exception.IdenticalCategoryInputException;
import hr.java.production.exception.IdenticalItemChoiceException;
import hr.java.production.exception.InvalidRangeException;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contains the logic for the main method as well as all other methods used in it. Also contains constants used throughout the entire class.
 */
public class Main {
    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;
    private static final Integer PIZZA = 1, CHICKEN_NUGGETS = 2;
    private static final Integer FOOD = 1, LAPTOP = 2;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * The main method of the application.
     * <p>
     * Starts the application, reads input data from a file, and performs several operations. It creates categories, items, factories, and stores based on the input data.
     * It then finds and prints information about the factory that produces the item with the largest volume, the store that sells the cheapest item, the most caloric food item,
     * the highest priced food item, and the laptop with the shortest warranty.
     *
     * @param args Command line arguments. Not used in this application.
     * @throws FileNotFoundException If the input file cannot be found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        logger.info("Aplikacija započela s radom.");

        File file = new File("josipovic-3/src/main/src/hr/java/production/files/lab-3-input");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        Category[] categories = inputCategories(scanner);
        Item[] items = inputItems(scanner, categories);
        Factory[] factories = inputFactories(scanner, items);
        Store[] stores = inputStores(scanner, items);

        System.out.println();
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

        logger.info("Aplikacija završila.");
    }


    /**
     * Searches for the laptop with the shortest warranty among a given array of items.
     * If no laptops are found in the array, it returns the first item in the array and logs an error.
     *
     * @param items An array of Item objects to search through.
     * @return The laptop with the shortest warranty. If no laptops are found, returns the first item in the array.
     */
    private static Item findLaptopWithShortestWarranty(Item[] items) {
        Item shortestWarrantyLaptop = items[0];
        Integer minWarranty = Integer.MAX_VALUE;

        for (Item i : items) {
            if (i instanceof Technical t) {
                Integer warranty = t.getRemainingWarrantyInMonths();
                if (warranty < minWarranty) {
                    minWarranty = warranty;
                    shortestWarrantyLaptop = i;
                }
            }
        }
        if (minWarranty == Integer.MAX_VALUE) {
            System.out.println("There are no laptops among items. Returning the first item in array.");
            logger.error("There are no laptops among items. Returning the first item in array. " + "Can't find the laptop with the shortest warranty because no instances of Interface Technical have been added.");
        }
        return shortestWarrantyLaptop;
    }

    /**
     * Searches for the food product with the highest price among a given array of items.
     * If no food products are found in the array, it returns the first item in the array and logs an error.
     *
     * @param items An array of Item objects to search through.
     * @return The food product with the highest price. If no food products are found, returns the first item in the array.
     */
    private static Item findHighestPricedFood(Item[] items) {
        Item mostExpensive = items[0];
        BigDecimal highestPrice = BigDecimal.valueOf(-1);
        for (Item i : items) {
            if (i instanceof Edible edible) {
                BigDecimal price = edible.calculatePrice();
                if (price.compareTo(highestPrice) > 0) {
                    highestPrice = price;
                    mostExpensive = i;
                }
            }
        }
        if (highestPrice.equals(BigDecimal.valueOf(-1))) {
            System.out.println("There are no food products among items. Returning the first item in array.");
            logger.error("There are no food products among items. Returning the first item in array. " + "Can't calculate the food product with the highest price because no instances of Interface Edible have been added.");
        }

        return mostExpensive;
    }

    /**
     * Searches for the food product with the most calories among a given array of items.
     * If no food products are found in the array, it returns the first item in the array and logs an error.
     *
     * @param items An array of Item objects to search through.
     * @return The food product with the most calories. If no food products are found, returns the first item in the array.
     */
    private static Item findMostCaloricFood(Item[] items) {
        Item mostCaloric = items[0];
        int maxCalories = -1;
        for (Item i : items) {
            if (i instanceof Edible edible) {
                int calories = edible.calculateKilocalories();
                if (calories > maxCalories) {
                    maxCalories = calories;
                    mostCaloric = i;
                }
            }
        }
        if (maxCalories == -1) {
            System.out.println("There are no food products among items. Returning the first item in array.");
            logger.error("There are no food products among items. Returning the first item in array. " + "Can't calculate the food product with the most calories because no instances of Interface Edible have been added.");
        }
        return mostCaloric;
    }


    /**
     * Prompts the user to input information for a specified number of categories.
     * It ensures that no two categories have the same name and description.
     * If a category already exists, it prompts the user to input a new category.
     *
     * @param scanner A Scanner object for user input.
     * @return An array of Category objects.
     */
    private static Category[] inputCategories(Scanner scanner) {
        Category[] categories = new Category[NUM_CATEGORIES];
        for (int i = 0; i < categories.length; i++) {
            while (true) {
                System.out.println("Enter the information about the " + (i + 1) + ". category: ");

                System.out.print("Enter the category name: ");
                String name = scanner.nextLine();

                System.out.print("Enter the category description: ");
                String description = scanner.nextLine();

                try {
                    checkForIdenticalCategories(new Category(name, description), categories, i);
                } catch (IdenticalCategoryInputException e) {
                    logger.warn(e.getMessage());
                    System.out.println("This category [" + name + "] has already been added. Please choose a category that hasn't been added.");
                    continue;
                }
                categories[i] = new Category(name, description);
                break;
            }
        }
        return categories;
    }

    /**
     * Checks if a category already exists in an array of categories.
     * It throws an {@code IdenticalCategoryInputException} if the category already exists.
     *
     * @param categoryInput  The Category object to check.
     * @param categories     An array of Category objects.
     * @param categoriesSize The number of categories currently in the array.
     * @throws IdenticalCategoryInputException If the category already exists in the array.
     */
    private static void checkForIdenticalCategories(Category categoryInput, Category[] categories, int categoriesSize) throws IdenticalCategoryInputException {
        for (int i = 0; i < categoriesSize; i++) {
            if (categories[i].equals(categoryInput))
                throw new IdenticalCategoryInputException("Entered category [" + categoryInput + "] has already been added. Added Categories: " + Arrays.toString(categories));
        }
    }


    /**
     * Prompts the user to input information for a specified number of items.
     * The user can choose the category and dimensions of each item, as well as specify whether the item is food, a laptop, or other.
     * If the item is food, the user can specify whether it's pizza or chicken nuggets and input its weight.
     * If the item is a laptop, the user can input its warranty duration.
     * After each item is created, if it's edible, it prints out its kilocalories and price with discount.
     *
     * @param scanner    A Scanner object for user input.
     * @param categories An array of {@code Category} objects to choose from when creating an item.
     * @return An array of Item objects.
     */
    private static Item[] inputItems(Scanner scanner, Category[] categories) {
        Item[] items = new Item[NUM_ITEMS];
        for (int i = 0; i < items.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". item: ");

            System.out.print("Enter the item name: ");
            String name = scanner.nextLine();

            System.out.println("Pick the item category:");
            for (int j = 0; j < categories.length; j++)
                System.out.println((j + 1) + ". " + categories[j].getName());

            int categoryChoice = numInputHandlerEx(scanner, "Choice >> ", 1, categories.length);

            System.out.println("Enter the item dimensions:");

            BigDecimal width = numInputHandlerEx(scanner, "Enter the item width: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal height = numInputHandlerEx(scanner, "Enter the item height: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal length = numInputHandlerEx(scanner, "Enter the item length: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal productionCost = numInputHandlerEx(scanner, "Enter the item production cost: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal sellingPrice = numInputHandlerEx(scanner, "Enter the item selling price: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal discountAmount = numInputHandlerEx(scanner, "Enter the discount percentage for this item: ", BigDecimal.ZERO, BigDecimal.valueOf(100));
            Discount discount = new Discount(discountAmount);

            int itemSubclassChoice = numInputHandlerEx(scanner, "Is this item food, a laptop, or other:\n1. Food\n2. Laptop\n3. Other\nChoice >> ", 1, 3);
            if (itemSubclassChoice == FOOD) {
                Integer foodChoice = numInputHandlerEx(scanner, "Pick an available food product:\n1. Pizza\n2. Chicken nuggets\nChoice >> ", PIZZA, CHICKEN_NUGGETS);
                BigDecimal weightInKG = numInputHandlerEx(scanner, "Enter the weight (in kg) of the food packet: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));

                if (foodChoice.equals(PIZZA))
                    items[i] = new Pizza(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount, weightInKG);
                else if (foodChoice.equals(CHICKEN_NUGGETS))
                    items[i] = new ChickenNuggets(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount, weightInKG);
            } else if (itemSubclassChoice == LAPTOP) {
                Integer warrantyYears = numInputHandlerEx(scanner, "Enter the warranty duration (in years) of the laptop: ", 0, 100);
                items[i] = new Laptop(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount, warrantyYears);
            } else {
                items[i] = new Item(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount);
            }

            if (items[i] instanceof Edible e) {
                System.out.println("Kilocalories in " + items[i].getName() + ": " + e.calculateKilocalories());
                System.out.println("Price (with " + items[i].getDiscount().discountAmount() + "% discount) for " + items[i].getName() + ": " + e.calculatePrice());
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
     * @param items   An array of {@code Item} objects to choose from when creating a factory.
     * @return An array of {@code Factory} objects.
     */
    private static Factory[] inputFactories(Scanner scanner, Item[] items) {
        Factory[] factories = new Factory[NUM_FACTORIES];
        Item[] addedItems = new Item[items.length];
        for (int i = 0; i < factories.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". factory: ");
            System.out.print("Enter the factory name: ");
            String name = scanner.nextLine();
            System.out.println("Enter the factory address information: ");
            Address address = inputAddress(scanner);
            System.out.println("Pick which items the factory produces: ");

            Item[] factoryItems = chooseItems(scanner, items, addedItems, itemsSize(addedItems));
            factories[i] = new Factory(name, address, factoryItems);
        }
        return factories;
    }


    /**
     * Counts the number of non-null items in an array.
     *
     * @param items An array of {@code Item} objects.
     * @return The count of non-null items in the array.
     */
    public static int itemsSize(Item[] items) {
        int count = 0;
        for (Item i : items) if (i != null) count++;
        return count;
    }


    /**
     * Allows the user to choose items from a given array.
     * The user can't choose an item that has already been added.
     * Offers an option to finish choosing after the first run.
     *
     * @param scanner         A {@code Scanner} object for user input.
     * @param items           An array of {@code Item} objects to choose from.
     * @param addedItems      An array of {@code Item} objects that have already been added.
     * @param addedItemsCount The count of items that have already been added.
     * @return An array of chosen {@code Item} objects.
     */
    private static Item[] chooseItems(Scanner scanner, Item[] items, Item[] addedItems, int addedItemsCount) {
        Item[] factoryItems = new Item[items.length];
        int storeItemsCount = 0;
        boolean finishedChoosing = false, isFirstRun = true;
        while (!finishedChoosing) {
            if (items.length == addedItemsCount) {
                logger.warn("All available items have been added. Possibly returning empty array.");
                System.out.println("All available items have been added.");
                break;
            }
            printAvailableItems(items, isFirstRun);
            int itemChoice;
            if (isFirstRun) itemChoice = numInputHandlerEx(scanner, "Choice >> ", 1, items.length);
            else itemChoice = numInputHandlerEx(scanner, "Choice >> ", 1, items.length + 1);

            if (itemChoice != items.length + 1) {
                try {
                    checkForIdenticalItems(items[itemChoice - 1], addedItems, addedItemsCount);
                } catch (IdenticalItemChoiceException e) {
                    logger.warn(e.getMessage());
                    System.out.println("This item [" + items[itemChoice - 1].getName() + "] has already been added.");
                    continue;
                }
                factoryItems[storeItemsCount++] = items[itemChoice - 1];
                //addedItems mijenja se i van metode
                addedItems[addedItemsCount++] = items[itemChoice - 1];
            } else finishedChoosing = true;
            isFirstRun = false;
        }
        return Arrays.copyOf(factoryItems, storeItemsCount);
    }

    /**
     * Checks if an item has already been added to a array.
     * Throws an {@code IdenticalItemChoiceException} if the item has already been added.
     *
     * @param itemChoice      The {@code Item} object to check.
     * @param addedItems      An array of {@code Item} objects that have already been added.
     * @param addedItemsCount The count of items that have already been added.
     * @throws IdenticalItemChoiceException If the item has already been added to the array.
     */
    private static void checkForIdenticalItems(Item itemChoice, Item[] addedItems, int addedItemsCount) throws IdenticalItemChoiceException {
        for (int i = 0; i < addedItemsCount; i++) {
            if (itemChoice.equals(addedItems[i])) {
                throw new IdenticalItemChoiceException("Chosen item [" + itemChoice + "] has already been added. Added Items: " + Arrays.toString(Arrays.copyOf(addedItems, addedItemsCount)));
            }
        }
    }

    /**
     * Prompts the user to input information for a specified number of stores.
     * User can choose the name, web address, and items sold by each store.
     * Ensures that no two stores sell the same item.
     *
     * @param scanner A {@code Scanner} object for user input.
     * @param items   An array of {@code Item} objects to choose from when creating a store.
     * @return An array of {@code Store} objects.
     */
    private static Store[] inputStores(Scanner scanner, Item[] items) {
        Store[] stores = new Store[NUM_STORES];
        Item[] addedItems = new Item[items.length];
        for (int i = 0; i < stores.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". store: ");
            System.out.print("Enter the store name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the store web address: ");
            String webAddress = scanner.nextLine();

            System.out.println("Pick which items the store sells: ");

            Item[] storeItems = chooseItems(scanner, items, addedItems, itemsSize(addedItems));
            stores[i] = new Store(name, webAddress, storeItems);
        }
        return stores;
    }

    /**
     * Prompts the user to input information for an address.
     * The user can input the street name, house number, city, and postal code.
     *
     * @param scanner A Scanner object for user input.
     * @return An Address object built with the provided information.
     */
    private static Address inputAddress(Scanner scanner) {
        System.out.print("Enter the street name: ");
        String street = scanner.nextLine();

        System.out.print("Enter the house number: ");
        String houseNumber = scanner.nextLine();

        System.out.print("Enter the city: ");
        String city = scanner.nextLine();

        System.out.print("Enter the postal code: ");
        String postalCode = scanner.nextLine();

        return new Address.Builder().atStreet(street).atHouseNumber(houseNumber).atCity(city).atPostalCode(postalCode).build();
    }


    /**
     * Prints all available items for selection.
     * <p>
     * This method iterates over all items and prints them for the user to choose from. If at least one item has already been chosen,
     * it also provides an option to finish choosing.
     *
     * @param items      An array of {@code Item} objects to be printed.
     * @param isFirstRun A boolean flag indicating whether this is the first run of item selection. If it's not the first run,
     *                   an option to finish choosing is printed.
     */
    private static void printAvailableItems(Item[] items, boolean isFirstRun) {
        System.out.println("Choose an item:");
        for (int i = 0; i < items.length; i++)
            System.out.println((i + 1) + ". " + items[i].getName());
        if (!isFirstRun) System.out.println(items.length + 1 + ". " + "Finished choosing.");
    }


    /**
     * Handles the input of an integer number from the user.
     * <p>
     * This method prompts the user to enter an integer number within a specified range. If the user enters a string instead of a number,
     * or a number outside the specified range, they are asked to enter the number again
     * and an error is logged.
     *
     * @param scanner  The {@code Scanner} object used for user input.
     * @param message  The prompt message displayed to the user.
     * @param minValue The minimum acceptable value for the input number (including).
     * @param maxValue The maximum acceptable value for the input number (including).
     * @return The valid integer number entered by the user.
     */
    private static int numInputHandlerEx(Scanner scanner, String message, int minValue, int maxValue) {
        int enteredNumber = 0;
        boolean badFormat;
        do {
            try {
                System.out.print(message);
                enteredNumber = scanner.nextInt();
                isNumInRangeEx(enteredNumber, minValue, maxValue);
                badFormat = false;
            } catch (InputMismatchException e) {
                logger.error("Entered a string instead of a number " + e);
                System.out.println("Entered a string instead of a number. Please enter a number:");
                badFormat = true;
            } catch (InvalidRangeException e) {
                logger.error(e.getMessage() + e);
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } finally {
                scanner.nextLine();
            }
        } while (badFormat);
        return enteredNumber;
    }

    /**
     * Checks if an entered integer number is within a specified range.
     * <p>
     * This method throws an {@code InvalidRangeException} if the entered number is not within the specified range.
     *
     * @param enteredNumber The integer number to check.
     * @param minValue      The minimum acceptable value for the entered number (including).
     * @param maxValue      The maximum acceptable value for the entered number (including).
     * @throws InvalidRangeException If the entered number is not within the specified range.
     */
    private static void isNumInRangeEx(int enteredNumber, int minValue, int maxValue) throws InvalidRangeException {
        if (enteredNumber < minValue || enteredNumber > maxValue) {
            throw new InvalidRangeException("Entered a number outside of specified range [" + minValue + "," + maxValue + "]." + " Input: " + enteredNumber);
        }
    }


    /**
     * Handles the input of a BigDecimal number from the user.
     * <p>
     * This method prompts the user to enter a BigDecimal number within a specified range. If the user enters a string instead of a number,
     * or a number outside the specified range, they are asked to enter the number again
     * and an error is logged.
     *
     * @param scanner  The {@code Scanner} object used for user input.
     * @param message  The prompt message displayed to the user.
     * @param minValue The minimum acceptable value for the input number (including).
     * @param maxValue The maximum acceptable value for the input number (including).
     * @return The valid BigDecimal number entered by the user.
     */
    private static BigDecimal numInputHandlerEx(Scanner scanner, String message, BigDecimal minValue, BigDecimal maxValue) {
        BigDecimal enteredNumber = BigDecimal.ZERO;
        boolean badFormat;

        do {
            try {
                System.out.print(message);
                enteredNumber = scanner.nextBigDecimal();
                isNumInRangeEx(enteredNumber, minValue, maxValue);
                badFormat = false;
            } catch (InputMismatchException e) {
                logger.error("Entered a string instead of a number " + e);
                System.out.println("Entered a string instead of a number. Please enter a number:");
                badFormat = true;
            } catch (InvalidRangeException e) {
                logger.error(e.getMessage() + e);
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } finally {
                scanner.nextLine();
            }
        } while (badFormat);
        return enteredNumber;
    }

    /**
     * Checks if an entered BigDecimal number is within a specified range.
     * <p>
     * This method throws an {@code InvalidRangeException} if the entered number is not within the specified range.
     *
     * @param enteredNumber The BigDecimal number to check.
     * @param minValue      The minimum acceptable value for the entered number (including).
     * @param maxValue      The maximum acceptable value for the entered number (including).
     * @throws InvalidRangeException If the entered number is not within the specified range.
     */
    private static void isNumInRangeEx(BigDecimal enteredNumber, BigDecimal minValue, BigDecimal maxValue) throws InvalidRangeException {
        if (enteredNumber.compareTo(minValue) < 0 || enteredNumber.compareTo(maxValue) > 0) {
            throw new InvalidRangeException("Entered a number outside of specified range [" + minValue + "," + maxValue + "]." + " Input: " + enteredNumber);
        }
    }


    /**
     * Finds the factory that produces the item with the largest volume.
     * <p>
     * This method iterates over all factories and their items, and keeps track of the factory that produces the item with the largest volume.
     * The volume of an item is calculated using the {@code calculateVolume()} method of the {@code Item} class.
     *
     * @param factories An array of {@code Factory} objects to search through.
     * @return The {@code Factory} object that produces the item with the largest volume. If multiple factories produce items with the same largest volume,
     * it returns the first one encountered.
     */
    private static Factory findFactoryWithLargestVolumeOfAnItem(Factory[] factories) {
        Factory bestFactory = factories[0];
        BigDecimal largestVolume = BigDecimal.valueOf(0);
        for (Factory f : factories)
            for (int i = 0; i < f.getItems().length; i++)
                if (f.getItems()[i].calculateVolume().compareTo(largestVolume) > 0) {
                    bestFactory = f;
                    largestVolume = f.getItems()[i].calculateVolume();
                }
        return bestFactory;
    }


    /**
     * Finds the store that sells the cheapest item.
     * <p>
     * This method iterates over all stores and their items, and keeps track of the store that sells the item with the cheapest selling price.
     * The selling price of an item is obtained using the {@code getSellingPrice()} method of the {@code Item} class which means the discount isn't applied.
     *
     * @param stores An array of {@code Store} objects to search through.
     * @return The {@code Store} object that sells the item with the cheapest price. If multiple stores sell items at the same cheapest price,
     * it returns the first one encountered.
     */
    private static Store findStoreWithCheapestItem(Store[] stores) {
        Store bestStore = stores[0];
        BigDecimal cheapestSellingPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        for (Store s : stores)
            for (int i = 0; i < s.getItems().length; i++)
                if (s.getItems()[i].getSellingPrice().compareTo(cheapestSellingPrice) < 0) {
                    bestStore = s;
                    cheapestSellingPrice = s.getItems()[i].getSellingPrice();
                }
        return bestStore;
    }
}
