package hr.java.production.main;

import hr.java.production.model.*;
import hr.java.production.sort.ProductionSorter;
import hr.java.production.utility.InputProcessor;
import hr.java.production.utility.ObjectFinder;
import hr.java.production.utility.ScannerInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Initializes and manages the production of different items in various factories and stores.
 * Contains the logic for the main method as well as all other methods used in it.
 * A Logger instance is used for logging application events.
 */
public class Main {
    private static final InputProcessor inputProcessor = new ScannerInputProcessor();
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
     * @deprecated All prices shown have discount applied.
     */
    public static void main(String[] args) {
        logger.info("Aplikacija započela s radom.");

        File file = new File("Josipovic-5/src/main/files/lab-5-input");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            logger.error("File not found. Exiting program. Entered file location: [" + file.getAbsolutePath() + "] Message: " + ex.getMessage());
            System.out.println("File not found. Exiting program.");
            System.exit(-1);
        }
        Scanner scanner1 = new Scanner(System.in);

        List<Category> categories = inputProcessor.inputCategories(scanner);
        List<Item> items = inputProcessor.inputItems(scanner, categories);
        List<Factory> factories = inputProcessor.inputFactories(scanner, items);
        List<Store> stores = inputProcessor.inputStores(scanner1, items);

        Factory bestFactory = ObjectFinder.findFactoryWithLargestVolumeOfAnItem(factories);
        System.out.println("The factory that produces an item with the greatest volume is: '" + bestFactory.getName() + "'.");

        Store bestStore = ObjectFinder.findStoreWithCheapestItem(stores);
        System.out.println("The store that sells an item with the cheapest price is: '" + bestStore.getName() + "'.");

        Item mostCaloricFood = ObjectFinder.findMostCaloricFood(items);
        if (mostCaloricFood instanceof Edible e) {
            System.out.println("The food product with the most calories is " + mostCaloricFood.getName() + " [" + e.calculateKilocalories() + "]");
        }
        Item highestPricedFood = ObjectFinder.findHighestPricedFood(items);
        if (highestPricedFood instanceof Edible e) {
            System.out.println("The food product with the highest price (with discount and weight) is " + highestPricedFood.getName() + " [" + e.calculatePrice() + "]");
        }
        Item shortestWarrantyLaptop = ObjectFinder.findLaptopWithShortestWarranty(items);
        if (shortestWarrantyLaptop instanceof Technical t) {
            System.out.println("The laptop with the shortest warranty is " + shortestWarrantyLaptop.getName() + " [" + t.getRemainingWarrantyInMonths() + "]");
        }


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

        //Sortiranje store artikala ScannerInputProcessor.chooseItems
        System.out.println("\n\nStore item volumes [Sorted with TreeSet - Descending]:");
        stores.forEach(store -> {
            System.out.println(store.getName() + ": ");
            store.getItems().forEach(item -> System.out.println(" - " + item.getName() + " [Volume: [" + item.calculateVolume() + "]]"));
        });

        System.out.println("\n\nFactory item volumes [Not sorted]:");
        factories.forEach(factory -> {
            System.out.println(factory.getName() + ": ");
            factory.getItems().forEach(item -> System.out.println(" - " + item.getName() + " [Volume: [" + item.calculateVolume() + "]]"));
        });
        //Sortiranje store artikala ScannerInputProcessor.chooseItems

        

        logger.info("Aplikacija završila.");
    }

    /**
     * Sorts and displays the most and least expensive items for each key in the provided map.
     * The items are sorted using the {@code ProductionSorter} comparator.
     * Logs the names of all items associated with each key and prints the most and least expensive items for each key.
     *
     * @param itemsPerKeyMap A map where each key is associated with a list of {@code Item} objects.
     * @deprecated For edible items the price is calculated per kilo ({@code getDiscountedSellingPrice}),
     * and not by taking into account the weight like the {@code calculatePrice} method
     * defined in the {@code Edible} interface does. Might want to change that in the future.
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


}
