package hr.java.production.utility;

import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ObjectFinder {
    private static final Logger logger = LoggerFactory.getLogger(ObjectFinder.class);

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

    public static Store findStoreWithCheapestItem(List<Store> stores) {
        Store bestStore = stores.getFirst();
        BigDecimal cheapestSellingPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        for (Store s : stores) {
            for (Item i : s.getItems()) {
                if (i.getSellingPrice().compareTo(cheapestSellingPrice) < 0) {
                    bestStore = s;
                    cheapestSellingPrice = i.getSellingPrice();
                }
            }
        }
        return bestStore;
    }

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
