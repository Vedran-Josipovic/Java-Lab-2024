package hr.java.production.utility;

import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.ItemContainer;
import hr.java.production.model.NamedEntity;
import hr.java.production.sort.ProductionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryReportPrinter {
    private static final Logger logger = LoggerFactory.getLogger(InventoryReportPrinter.class);

    public static <T extends NamedEntity> void printNames(Collection<T> entityCollection){
        System.out.println(entityCollection.stream().map(NamedEntity::getName).collect(Collectors.joining(", ")));
    }


    public static void printDiscountedItems(List<Item> items) {
        System.out.print("Discounted items: ");
        List<Item> discountedItems = items.stream()
                .filter(i -> i.getDiscount().discountAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        if (discountedItems.isEmpty()) {
            System.out.println("No discounted items found");
        } else {
            printNames(discountedItems);
        }
    }



    public static void printItemNamesInContainers(List<? extends ItemContainer> containers) {
        containers.forEach(container -> {
            System.out.print(container.getName() + " has " + container.getItems().size() + " items: ");
            printNames(container.getItems());
        });
    }



    public static <T extends ItemContainer> void printContainersWithAboveAverageItemCount(List<T> containers) {
        double averageNumberOfItemsInContainers = containers.stream()
                .mapToInt(store -> store.getItems().size())
                .average()
                .orElse(0);

        System.out.println("Average number of items per entity: " + averageNumberOfItemsInContainers);

        List<T> containersWithAboveAverageItems = containers.stream()
                .filter(store -> store.getItems().size() > averageNumberOfItemsInContainers)
                .toList();

        System.out.print("Entities in production chain with above-average number of items: ");
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



    public static void printCheapestAndPriciestItemsByKey(Map<?, List<Item>> itemsPerKeyMap) {
        itemsPerKeyMap.forEach((key, valueItems) -> {
            valueItems.sort(new ProductionSorter());
            Item mostExpensive = valueItems.getLast(), leastExpensive = valueItems.getFirst();

            String mostExpensiveString = mostExpensive.getName() + " [" + mostExpensive.getDiscountedSellingPrice() + "]";
            String leastExpensiveString = leastExpensive.getName() + " [" + leastExpensive.getDiscountedSellingPrice() + "]";

            String keyName = key instanceof Category ? ((Category) key).getName() : key.toString();

            System.out.print("Key = [" + keyName + "]: Values = ");
            printNames(valueItems);

            String msg = "Key = [" + keyName + "]: Most expensive: " + mostExpensiveString + ", Least expensive: " + leastExpensiveString;
            System.out.println(msg);
            logger.debug(msg);
        });
    }

}
