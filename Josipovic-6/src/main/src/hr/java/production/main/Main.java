package hr.java.production.main;

import hr.java.production.enumeration.FilePath;
import hr.java.production.model.*;
import hr.java.production.utility.FileUtils;
import hr.java.production.utility.InventoryAnalyzer;
import hr.java.production.utility.InventoryReportPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Aplikacija započela s radom.");

        List<Category> categories = FileUtils.inputCategories();
        List<Item> items = FileUtils.inputItems(categories);
        List<Factory> factories = FileUtils.inputFactories(items);
        List<Store> stores = FileUtils.inputStores(items);

        Factory bestFactory = InventoryAnalyzer.findFactoryWithLargestVolumeOfAnItem(factories);
        System.out.println("The factory that produces an item with the greatest volume is: '" + bestFactory.getName() + "'.");

        Store bestStore = InventoryAnalyzer.findStoreWithCheapestItem(stores);
        System.out.println("The store that sells an item with the cheapest price is: '" + bestStore.getName() + "'.");

        Item mostCaloricFood = InventoryAnalyzer.findMostCaloricFood(items);
        if (mostCaloricFood instanceof Edible e) {
            System.out.println("The food product with the most calories is " + mostCaloricFood.getName() + " [" + e.calculateKilocalories() + "]");
        }
        Item highestPricedFood = InventoryAnalyzer.findHighestPricedFood(items);
        if (highestPricedFood instanceof Edible e) {
            System.out.println("The food product with the highest price (with discount and weight) is " + highestPricedFood.getName() + " [" + e.calculatePrice() + "]");
        }
        Item shortestWarrantyLaptop = InventoryAnalyzer.findLaptopWithShortestWarranty(items);
        if (shortestWarrantyLaptop instanceof Technical t) {
            System.out.println("The laptop with the shortest warranty is " + shortestWarrantyLaptop.getName() + " [" + t.getRemainingWarrantyInMonths() + "]");
        }


        Map<Category, List<Item>> itemsPerCategoryMap = InventoryAnalyzer.mapItemsByCategory(items);
        System.out.println("Cheapest and most expensive items by category:");
        InventoryReportPrinter.printCheapestAndPriciestItemsByKey(itemsPerCategoryMap);

        Map<String, List<Item>> itemsPerInterfaceMap = InventoryAnalyzer.mapItemsByInterfaceType(items);
        System.out.println("Cheapest and most expensive items by interface:");
        InventoryReportPrinter.printCheapestAndPriciestItemsByKey(itemsPerInterfaceMap);

        //Sortiranje store artikala ScannerInputProcessor.chooseItems
        System.out.println("\n\nStore item volumes [Sorted with TreeSet - Descending]:");
        InventoryReportPrinter.printContainersWithItemVolumes(stores);

        System.out.println("\n\nFactory item volumes [Not sorted]:");
        InventoryReportPrinter.printContainersWithItemVolumes(factories);
        //Sortiranje store artikala ScannerInputProcessor.chooseItems

        //Srednja cijena svih artikala koji imaju natprosječni volumen
        System.out.println("Average price of all items: " + InventoryAnalyzer.calculateAverageItemPrice(items));
        System.out.println("Average volume of all items: " + InventoryAnalyzer.calculateAverageItemVolume(items));
        System.out.println("Average price of all items with above average volume: " + InventoryAnalyzer.calculateAveragePriceForAboveAverageVolumeItems(items));
        //Srednja cijena svih artikala koji imaju natprosječni volumen


        //Trgovine s natprosječnim brojem artikala
        System.out.println();
        InventoryReportPrinter.printContainersWithAboveAverageItemCount(stores);
        //Trgovine s natprosječnim brojem artikala


        //Filtriranje itemova po tome koji ima popust veći od nula
        InventoryReportPrinter.printDiscountedItems(items);
        //Filtriranje itemova po tome koji ima popust veći od nula

        //Korištenjem .map ispisati broj artikala u svakoj od trgovina i ispisati itemove sa tostringom
        System.out.println("Store Items Information:");
        InventoryReportPrinter.printItemNamesInContainers(stores);
        //Korištenjem .map ispisati broj artikala u svakoj od trgovina i ispisati itemove sa tostringom


        // LAB-6 Na kraju programa serijalizirati sve objekte klasa „Factory“i „Store“ koja imaju barem pet artikala.
        List<Factory> filteredFactories = factories.stream().filter(f -> f.getItems().size() >= 5).collect(Collectors.toList());
        FileUtils.serializeList(filteredFactories, FilePath.SERIALIZED_FACTORIES);

        List<Store> filteredStores = stores.stream().filter(s -> s.getItems().size() >= 5).collect(Collectors.toList());
        FileUtils.serializeList(filteredStores, FilePath.SERIALIZED_STORES);

        List<Factory> deserializedFactories = FileUtils.deserializeList(FilePath.SERIALIZED_FACTORIES);
        System.out.print("Deserialized factories: ");
        InventoryReportPrinter.printNames(deserializedFactories);

        List<Store> deserializedStores = FileUtils.deserializeList(FilePath.SERIALIZED_STORES);
        System.out.print("Deserialized stores: ");
        InventoryReportPrinter.printNames(deserializedStores);
        // LAB-6 Na kraju programa serijalizirati sve objekte klasa „Factory“i „Store“ koja imaju barem pet artikala.


        logger.info("Aplikacija završila.");
    }


}
