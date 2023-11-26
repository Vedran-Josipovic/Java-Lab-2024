package hr.java.production.utility;

import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for finding objects with specific properties.
 * <p>
 * This class provides static methods for finding objects such as factories, stores, and items with specific properties.
 * It uses a {@code Logger} object to log errors and information.
 */
public class InventoryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(InventoryAnalyzer.class);





    public static BigDecimal calculateAverageItemPrice(List<Item> items) {
        return items.stream()
                .map(Item::getSellingPrice)
                .reduce(BigDecimal::add)
                .map(total -> total.divide(BigDecimal.valueOf(items.size()), RoundingMode.CEILING))
                .orElse(BigDecimal.ZERO);
    }

    public static BigDecimal calculateAverageItemVolume(List<Item> items) {
        return items.stream()
                .map(Item::calculateVolume)
                .reduce(BigDecimal::add)
                .map(total -> total.divide(BigDecimal.valueOf(items.size()), RoundingMode.CEILING))
                .orElse(BigDecimal.ZERO);
    }

    public static BigDecimal calculateAveragePriceForAboveAverageVolumeItems(List<Item> items) {
        BigDecimal averageVolume = calculateAverageItemVolume(items);

        List<Item> aboveAverageVolumeItems = items.stream()
                .filter(item -> item.calculateVolume().compareTo(averageVolume) > 0)
                .collect(Collectors.toList());

        return calculateAverageItemPrice(aboveAverageVolumeItems);
    }


    public static Map<Category, List<Item>> mapItemsByCategory(List<Item> items) {
        Map<Category, List<Item>> itemsPerCategoryMap = new HashMap<>();
        for (var i : items) {
            itemsPerCategoryMap.computeIfAbsent(i.getCategory(), k -> new ArrayList<>()).add(i);
        }
        return itemsPerCategoryMap;
    }

    public static Map<String, List<Item>> mapItemsByInterfaceType(List<Item> items) {
        Map<String, List<Item>> itemsPerInterfaceMap = new HashMap<>();
        for (Item i : items) {
            String key;
            if (i instanceof Edible) key = "Edible";
            else if (i instanceof Technical) key = "Technical";
            else continue;
            itemsPerInterfaceMap.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
        }
        return itemsPerInterfaceMap;
    }


    /**
     * Finds the factory with the largest volume of an item.
     * <p>
     * Iterates over a list of factories and finds the one with the largest volume of an item.
     *
     * @param factories A list of factories to search.
     * @return The factory with the largest volume of an item.
     */
    public static Factory findFactoryWithLargestVolumeOfAnItem(List<Factory> factories) {
        Factory bestFactory = factories.getFirst();
        BigDecimal largestVolume = BigDecimal.valueOf(0);
        for (Factory f : factories) {
            for (Item i : f.getItems()) {
                if (i.calculateVolume().compareTo(largestVolume) > 0) {
                    bestFactory = f;
                    largestVolume = i.calculateVolume();
                }
            }
        }
        return bestFactory;
    }

    /**
     * Finds the store with the cheapest item.
     * <p>
     * Iterates over a list of stores and finds the one with the cheapest item with discount applied.
     *
     * @param stores A list of stores to search.
     * @return The store with the cheapest item.
     * @deprecated For edible items the price is calculated per kilo ({@code getDiscountedSellingPrice}),
     * and not by taking into account the weight like the {@code calculatePrice} method
     * defined in the {@code Edible} interface does. Might want to change that in the future.
     */
    public static Store findStoreWithCheapestItem(List<Store> stores) {
        Store bestStore = stores.getFirst();
        BigDecimal cheapestSellingPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        for (Store s : stores) {
            for (Item i : s.getItems()) {
                if (i.getDiscountedSellingPrice().compareTo(cheapestSellingPrice) < 0) {
                    bestStore = s;
                    cheapestSellingPrice = i.getDiscountedSellingPrice();
                }
            }
        }
        return bestStore;
    }

    /**
     * Finds the most caloric food item.
     * <p>
     * Iterates over a list of items and finds the one with the most calories.
     * If there are no food products among the items, it returns the first item in the list and logs an error.
     *
     * @param items A list of items to search.
     * @return The most caloric food item.
     */
    public static Item findMostCaloricFood(List<Item> items) {
        Item mostCaloric = items.getFirst();
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
     * Finds the highest priced food item.
     * <p>
     * Iterates over a list of items and finds the one with the highest price.
     * If there are no food products among the items, it returns the first item in the list and logs an error.
     *
     * @param items A list of items to search.
     * @return The highest priced food item.
     */
    public static Item findHighestPricedFood(List<Item> items) {
        Item mostExpensive = items.getFirst();
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
     * Finds the laptop with the shortest warranty.
     * <p>
     * Iterates over a list of items and finds the laptop with the shortest warranty.
     * If there are no laptops among the items, it returns the first item in the list and logs an error.
     *
     * @param items A list of items to search.
     * @return The laptop with the shortest warranty.
     */
    public static Item findLaptopWithShortestWarranty(List<Item> items) {
        Item shortestWarrantyLaptop = items.getFirst();
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



}
