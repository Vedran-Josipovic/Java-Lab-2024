package hr.java.production.main;

import hr.java.production.model.*;
import hr.java.production.sort.ProductionSorter;
import hr.java.production.utility.FileUtils;
import hr.java.production.utility.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Initializes and manages the production of different items in various factories and stores.
 * Contains the logic for the main method as well as all other methods used in it.
 * A Logger instance is used for logging application events.
 */
public class Main {
    //private static final InputProcessor inputProcessor = new ScannerInputProcessor();
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
    public static void main(String[] args) throws FileNotFoundException {
        logger.info("Aplikacija započela s radom.");

        File file = new File("josipovic-6/src/main/dat/lab-5-input");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        List<Category> categories = FileUtils.inputCategories();
        List<Item> items = FileUtils.inputItems(categories);
        List<Factory> factories = FileUtils.inputFactories(items);
        List<Store> stores = FileUtils.inputStores(items);

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

        //Srednja cijena svih artikala koji imaju natprosječni volumen
        System.out.println();
        BigDecimal averageItemPrice = items.stream()
                .map(Item::getSellingPrice)
                .reduce(BigDecimal::add)
                .map(total -> total.divide(BigDecimal.valueOf(items.size()), 2))
                .orElse(BigDecimal.ZERO);
        System.out.println("Average price of all items: " + averageItemPrice);

        BigDecimal averageItemVolume = items.stream()
                .map(Item::calculateVolume)
                .reduce(BigDecimal::add)
                .map(total -> total.divide(BigDecimal.valueOf(items.size()), 2))
                .orElse(BigDecimal.ZERO);
        System.out.println("Average volume of all items: " + averageItemVolume);

        BigDecimal averagePriceForItemsWithAboveAverageVolume =
                items.stream()
                        .filter(item -> item.calculateVolume().compareTo(averageItemVolume) > 0)
                        .map(Item::getSellingPrice)
                        .reduce(BigDecimal::add)
                        .map(total -> total.divide(BigDecimal.valueOf(
                                items.stream()
                                        .filter(item -> item.calculateVolume().compareTo(averageItemVolume) > 0)
                                        .count()), 2))
                        .orElse(BigDecimal.ZERO);

        System.out.println("Average price of all items with above average volume: " + averagePriceForItemsWithAboveAverageVolume);
        //Srednja cijena svih artikala koji imaju natprosječni volumen


        //Trgovine s natprosječnim brojem artikala
        System.out.println();
        findStoresWithAboveAverageItems(stores);
        //Trgovine s natprosječnim brojem artikala


        //Mjerenje koliko treba da se sortiraju dućani bez lambdi
        Instant startWithoutLambda = Instant.now();
        TreeSet<Item> sortedWithoutLambda = new TreeSet<>(new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                int volumeComparison = i1.calculateVolume().compareTo(i2.calculateVolume());
                if (volumeComparison != 0) {
                    return volumeComparison;
                }
                return i1.getName().compareTo(i2.getName());
            }
        });
        sortedWithoutLambda.addAll(items);
        Instant endWithoutLambda = Instant.now();
        Duration durationWithoutLambda = Duration.between(startWithoutLambda, endWithoutLambda);
        logger.info("Sorting without lambdas: " + durationWithoutLambda.toNanos());
        //Mjerenje koliko treba da se sortiraju dućani bez lambdi


        //Filtriranje itemova po tome koji ima popust veći od nula
        System.out.print("\nDiscounted items: ");
        Optional<List<Item>> discountedItemsOptional = Optional.of(
                        items.stream()
                                .filter(i -> i.getDiscount().discountAmount().compareTo(BigDecimal.ZERO) > 0)
                                .collect(Collectors.toList()))
                .filter(list -> !list.isEmpty());
        if (discountedItemsOptional.isPresent()) {
            String itemNames = discountedItemsOptional.get().stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            System.out.println(itemNames);
        } else {
            System.out.println("No discounted items found");
        }
        //Filtriranje itemova po tome koji ima popust veći od nula

        //Korištenjem .map ispisati broj artikala u svakoj od trgovina i ispisati itemove sa tostringom
        System.out.println();
        stores.stream()
                .map(store -> store.getName() + " has " + store.getItems().size() + " items: " + store.getItems())
                .forEach(System.out::println);
        //Korištenjem .map ispisati broj artikala u svakoj od trgovina i ispisati itemove sa tostringom

        logger.info("Aplikacija završila.");
    }


    private static void findStoresWithAboveAverageItems(List<Store> stores) {
        double averageNumberOfItems = stores.stream()
                .mapToInt(store -> store.getItems().size())
                .average()
                .orElse(0);

        System.out.println("Average number of items per store: " + averageNumberOfItems);

        List<Store> storesWithAboveAverageItems = stores.stream()
                .filter(store -> store.getItems().size() > averageNumberOfItems)
                .collect(Collectors.toList());

        System.out.println("Stores with above-average number of items:");
        if (storesWithAboveAverageItems.isEmpty()) {
            System.out.println("There are no stores with an above-average number of items.");
            logger.info("There are no stores with an above-average number of items.");
        } else {
            storesWithAboveAverageItems.forEach(store ->
                    System.out.println(store.getName() + " - Number of Items: " + store.getItems().size()));
        }

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
