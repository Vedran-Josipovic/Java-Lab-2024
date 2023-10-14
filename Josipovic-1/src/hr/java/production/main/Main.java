package hr.java.production.main;

import hr.java.production.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner1 = new Scanner(System.in);

        File file = new File("Josipovic-1/src/hr/java/production/files/currentInput");
        Scanner scanner = new Scanner(file);

        Category[] categories;
        Item[] items;
        Factory[] factories;

        Store[] stores;

        categories = inputCategories(scanner);
        items = inputItems(scanner, categories);

        factories = inputFactories(scanner, items);

        for (Factory f : factories) {
            System.out.println(f.toString());
        }

    }

    private static Category[] inputCategories(Scanner scanner){
        Category[] categories = new Category[NUM_CATEGORIES];
        for (int i = 0; i < categories.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". category: ");

            System.out.print("Enter the category name: ");
            String name = scanner.nextLine();

            System.out.print("Enter the category description: ");
            String description = scanner.nextLine();

            categories[i] = new Category(name, description);
        }
        return categories;
    }
    private static Item[] inputItems(Scanner scanner, Category[] categories){
        Item[] items = new Item[NUM_ITEMS];

        for (int i = 0; i < items.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". item: ");

            System.out.print("Enter the item name: ");
            String name = scanner.nextLine();

            System.out.println("Pick the item category:");
            for (int j = 0; j < categories.length; j++)
                System.out.println((j + 1) + ". " + categories[j].getName());
            System.out.print("Choice >> ");
            int categoryChoice = scanner.nextInt(); scanner.nextLine();

            System.out.println("Enter the item dimensions:");

            System.out.print("Enter the item width: ");
            BigDecimal width = scanner.nextBigDecimal(); scanner.nextLine();

            System.out.print("Enter the item height: ");
            BigDecimal height = scanner.nextBigDecimal(); scanner.nextLine();

            System.out.print("Enter the item length: ");
            BigDecimal length = scanner.nextBigDecimal(); scanner.nextLine();

            System.out.print("Enter the item production cost: ");
            BigDecimal productionCost = scanner.nextBigDecimal(); scanner.nextLine();

            System.out.print("Enter the item selling price: ");
            BigDecimal sellingPrice = scanner.nextBigDecimal(); scanner.nextLine();

            items[i] = new Item(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice);
        }
        return items;
    }

    /**
     * Nakon što se odabere item, miče se it liste za odabir.
     */
    private static Factory[] inputFactories(Scanner scanner, Item[] items){
        Factory[] factories = new Factory[NUM_FACTORIES];

        for (int i = 0; i < factories.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". factory: ");
            System.out.print("Enter the factory name: ");
            String name = scanner.nextLine();
            System.out.println("Enter the factory address information: ");
            Address address = inputAddress(scanner);
            System.out.println("Pick which items the factory produces: ");

            ///chooseFactoryItems
            Item[] factoryItems = new Item[1];
            boolean finishedChoosing = false, isFirstRun = true;
            while (!finishedChoosing){
                printAvailableItems(items, isFirstRun);
                System.out.print("Choice >> ");
                int itemChoice = scanner.nextInt(); scanner.nextLine();

                if(itemChoice != items.length + 1) { //Ovo breaka kod ako se unese u prvoj iteraciji items.length + 1 - handle with exceptions
                    factoryItems[factoryItems.length - 1] = items[itemChoice - 1]; //Dodaje se na zadnje mjesto factoryItems-a
                    if(items.length > 1){
                        items = removeChosenItem(items, itemChoice);
                        factoryItems = expandItemArray(factoryItems);
                    }
                    else finishedChoosing = true;
                } else {
                    finishedChoosing = true;
                    factoryItems = trimItemArray(factoryItems);
                }
                isFirstRun = false;
            }
            ///chooseFactoryItems

            factories[i] = new Factory(name, address, factoryItems);
        }
        return factories;
    }

    /**
     * Ispisuje sve dostupne artikle. Ukoliko je već odabran jedan artikl,
     * nudi se opcija za završavanje odabira.
     */
    private static void printAvailableItems(Item[] items, boolean isFirstRun) {
        System.out.println("Choose an item:");
        for (int j = 0; j < items.length; j++)
            System.out.println((j + 1) + ". " + items[j].getName());
        if (!isFirstRun)
            System.out.println(items.length + 1 + ". " + "Finished choosing.");
    }

    /**
     * Miče objekt klase {@code Item} koji smo odabrali iz niza.
     * Prolazi kroz niz {@code items} i ako dođe do mjesta na kojem se nalazi odabrani objekt,
     * ne sprema ga u novi niz koji ima veličinu manju za jedan.
     */
    private static Item[] removeChosenItem(Item[] items, int itemChoice) {
        Item[] clonedItems = new Item[items.length - 1];
        for (int i = 0, j = 0; i < items.length; i++)
            if (i != itemChoice - 1)
                clonedItems[j++] = items[i];
        return clonedItems;
    }

    /**
     * Smanjuje veličinu niza za jedan.
     */
    private static Item[] trimItemArray(Item[] oldArray) {
        Item[] newArray = new Item[oldArray.length - 1];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);
        return newArray;
    }
    /**
     * Povećava niz za jedan element. (koji poprima vrijednost null)
     */
    private static Item[] expandItemArray(Item[] oldArray) {
        Item[] newArray = new Item[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }
    private static Address inputAddress(Scanner scanner){
        System.out.print("Enter the street name: ");
        String street = scanner.nextLine();

        System.out.print("Enter the house number: ");
        String houseNumber = scanner.nextLine();

        System.out.print("Enter the city: ");
        String city = scanner.nextLine();

        System.out.print("Enter the postal code: ");
        String postalCode = scanner.nextLine();

        return new Address(street, houseNumber, city, postalCode);
    }

}
