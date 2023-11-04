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

        File file = new File("Josipovic-4/src/main/files/lab-4-input");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        List<Category> categories = inputCategories(scanner);
        List<Item> items = inputItems(scanner, categories);
        List<Factory> factories = inputFactories(scanner, items);
        List<Store> stores = inputStores(scanner, items);

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
     * IThis sorts the lists so it changes the map. Maybe pull sorting out of method and require a sorted set.
     *
     * @param itemsPerKeyMap
     */
    private static void printCheapestAndPriciestItemsByKey(Map<?, List<Item>> itemsPerKeyMap) {
        System.out.println();
        itemsPerKeyMap.forEach((key, value) -> {
            String listNames = value.stream().map(NamedEntity::getName).collect(Collectors.joining(", "));
            logger.debug("Key = [" + key.toString() + "]: Values = [" + listNames + "]");
        });

        for (var i : itemsPerKeyMap.entrySet()) {
            List<Item> categoryItems = i.getValue();
            categoryItems.sort(new ProductionSorter());
            Item mostExpensive = categoryItems.getLast(), leastExpensive = categoryItems.getFirst();

            String mostExpensiveString = mostExpensive.getName() + " [" + mostExpensive.getDiscountedSellingPrice() + "]";
            String leastExpensiveString = leastExpensive.getName() + " [" + leastExpensive.getDiscountedSellingPrice() + "]";

            String keyName;
            if (i.getKey() instanceof Category category) keyName = category.getName();
            else keyName = i.getKey().toString();

            String msg = "Key: " + keyName + " -> Most expensive: " + mostExpensiveString + ", Least expensive: " + leastExpensiveString;
            System.out.println(msg); logger.debug(msg);
        }
    }


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

    private static void checkForIdenticalCategories(Category categoryInput, List<Category> categories) throws IdenticalCategoryInputException {
        for (Category c : categories) {
            if (c.equals(categoryInput))
                throw new IdenticalCategoryInputException("Entered category [" + categoryInput.getName() + "] has already been added. Added Categories: " + categories.stream().map(Category::getName).collect(Collectors.joining(", ")));
        }
    }

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

    private static Set<Item> chooseItems(Scanner scanner, List<Item> items, List<Item> addedItems) {
        Set<Item> addedItemSet = new HashSet<>();
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
                addedItemSet.add(items.get(itemChoice - 1));
                //addedItems mijenja se i van metode
                addedItems.add(items.get(itemChoice - 1));
            } else finishedChoosing = true;
            isFirstRun = false;
        }
        return addedItemSet;
    }

    private static void printAvailableItems(List<Item> items, boolean isFirstRun) {
        System.out.println("Choose an item:");
        for (int i = 0; i < items.size(); i++)
            System.out.println((i + 1) + ". " + items.get(i).getName());
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
