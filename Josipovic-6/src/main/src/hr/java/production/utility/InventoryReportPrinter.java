package hr.java.production.utility;

import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.ItemContainer;
import hr.java.production.model.NamedEntity;
import hr.java.production.sort.ProductionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryReportPrinter {
    private static final Logger logger = LoggerFactory.getLogger(InventoryReportPrinter.class);

    public static void printDiscountedItems(List<Item> items) {
        System.out.print("Discounted items: ");
        List<Item> discountedItems = items.stream()
                .filter(i -> i.getDiscount().discountAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        if (discountedItems.isEmpty()) {
            System.out.println("No discounted items found");
        } else {
            String itemNames = discountedItems.stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            System.out.println(itemNames);
        }
    }


    public static void printItemNamesInContainers(List<? extends ItemContainer> containers) {
        containers.stream()
                .map(container -> container.getName() + " has " + container.getItems().size() + " items: "
                        + container.getItems().stream().map(Item::getName).collect(Collectors.joining(", ")))
                .forEach(System.out::println);
    }


    public static <T extends ItemContainer> void printContainersWithAboveAverageItemCount(List<T> containers) {
        double averageNumberOfItemsInContainers = containers.stream()
                .mapToInt(store -> store.getItems().size())
                .average()
                .orElse(0);

        System.out.println("Average number of items per entity: " + averageNumberOfItemsInContainers);

        List<T> containersWithAboveAverageItems = containers.stream()
                .filter(store -> store.getItems().size() > averageNumberOfItemsInContainers)
                .collect(Collectors.toList());

        System.out.println("Entities in production chain with above-average number of items:");
        if (containersWithAboveAverageItems.isEmpty()) {
            System.out.println("There are no entities with an above-average number of items.");
            logger.info("There are no entities with an above-average number of items.");
        } else {
            containersWithAboveAverageItems.forEach(store -> System.out.println(store.getName() + " - Number of Items: " + store.getItems().size()));
        }

    }


    public static <T extends ItemContainer> void printContainersWithItemVolumes(List<T> containers) {
        containers.forEach(container -> {
            System.out.println(container.getName() + ": ");
            container.getItems().forEach(item ->
                    System.out.println(" - " + item.getName() + " [Volume: [" + item.calculateVolume() + "]]"));
        });
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
    public static void printCheapestAndPriciestItemsByKey(Map<?, List<Item>> itemsPerKeyMap) {
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
