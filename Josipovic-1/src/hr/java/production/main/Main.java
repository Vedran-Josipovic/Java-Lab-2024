package hr.java.production.main;

import hr.java.production.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("Josipovic-1/src/hr/java/production/files/currentInput");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        Category[] categories = inputCategories(scanner);
        Item[] items = inputItems(scanner, categories);
        Factory[] factories = inputFactories(scanner1, items);
        Store[] stores = inputStores(scanner1, items);

        Factory bestFactory = findFactoryWithLargestVolumeOfAnItem(factories);
        System.out.println("The factory that produces an item with the greatest volume is: '" + bestFactory.getName() + "'.");

        Store bestStore = findStoreWithCheapestItem(stores);
        System.out.println("The store that sells an item with the cheapest price is: '" + bestStore.getName() + "'.");
    }

    private static Factory findFactoryWithLargestVolumeOfAnItem(Factory[] factories){
        Factory bestFactory = factories[0];
        BigDecimal largestVolume = BigDecimal.valueOf(0);
        for (int i = 0; i < factories.length; i++)
            for (int j = 0; j < factories[i].getItems().length; j++)
                if (factories[i].getItems()[j].calculateVolume().compareTo(largestVolume) == 1) {
                    bestFactory = factories[i]; largestVolume = factories[i].getItems()[j].calculateVolume();
                }
        //System.out.println(largestVolume);
        return bestFactory;
    }
    private static Store findStoreWithCheapestItem(Store[] stores){
        Store bestStore = stores[0];
        BigDecimal cheapestSellingPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        for (int i = 0; i < stores.length; i++)
            for (int j = 0; j < stores[i].getItems().length; j++)
                if(stores[i].getItems()[j].getSellingPrice().compareTo(cheapestSellingPrice) == -1){
                    bestStore = stores[i]; cheapestSellingPrice = stores[i].getItems()[j].getSellingPrice();
                }
        //System.out.println("Cheapest Price: " + cheapestSellingPrice);
        return bestStore;
    }



    /**
     * Obrađuje korisnikov numerički unos. Provjerava je li unos cijeli broj i nalazi li se unutar određenog raspona.
     * Ako unos nije cijeli broj ili je izvan raspona, traži od korisnika da unese valjani broj.
     *
     * @param scanner  Scanner objekt koji se koristi za dobivanje korisnikovog unosa.
     * @param message  Poruka koja se prikazuje korisniku prilikom traženja unosa.
     * @param minValue Minimalna prihvatljiva vrijednost za unos.
     * @param maxValue Maksimalna prihvatljiva vrijednost za unos.
     * @return         Valjani broj koji je unio korisnik.
     */
    private static int numInputHandler(Scanner scanner, String message, int minValue, int maxValue){
        int enteredNumber;
        boolean badFormat = false;
        do {
            System.out.print(message);
            while (!scanner.hasNextInt()) {
                System.out.println("Entered a string instead of a number. Please enter a number:");
                System.out.print(message);
                scanner.nextLine();
            }
            enteredNumber = scanner.nextInt();
            scanner.nextLine();

            if(isNumInRange(enteredNumber, minValue, maxValue) == false){
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } else badFormat = false;
        }while (badFormat);
        return enteredNumber;
    }

    /**
     * Provjerava je li uneseni broj unutar zadanih granica.
     *
     * @param enteredNumber Broj koji se provjerava.
     * @param minValue Minimalna dopuštena vrijednost (Uključujući).
     * @param maxValue Maksimalna dopuštena vrijednost (Uključujući)
     * @return True ako je broj unutar raspona, false ako nije.
     */
    private static boolean isNumInRange(int enteredNumber, int minValue, int maxValue) {
        if (enteredNumber < minValue || enteredNumber > maxValue) return false;
        return true;
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


            //error handling
//            System.out.print("Choice >> ");
//            int categoryChoice = scanner.nextInt(); scanner.nextLine();
            int categoryChoice = numInputHandler(scanner, "Choice >> ", 1, categories.length);
            //error handling


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

            ///chooseFactoryItems - Kod se ponavlja i s dućanima, ali pošto za funkciju mi treba i factoryItems i items, treba mi mapa, pa cu poslije u 4.lab
            Item[] factoryItems = new Item[1];
            boolean finishedChoosing = false, isFirstRun = true;
            while (!finishedChoosing){
                printAvailableItems(items, isFirstRun);




                //error handling
//                System.out.print("Choice >> ");
//                int itemChoice = scanner.nextInt(); scanner.nextLine();
                //error handling

                int itemChoice;
                if(isFirstRun) itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length);
                else itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length + 1);


                if(itemChoice != items.length + 1) {
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
    private static Store[] inputStores(Scanner scanner, Item[] items){
        Store[] stores = new Store[NUM_STORES];

        for (int i = 0; i < stores.length; i++) {
            System.out.println("Enter the information about the " + (i + 1) + ". store: ");
            System.out.print("Enter the store name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the store web address: ");
            String webAddress = scanner.nextLine();

            System.out.println("Pick which items the store sells: ");

            ///chooseFactoryItems - Kod se ponavlja i s dućanima, ali pošto za funkciju mi treba i factoryItems i items, treba mi mapa, pa cu poslije u 4.lab
            Item[] storeItems = new Item[1];
            boolean finishedChoosing = false, isFirstRun = true;
            while (!finishedChoosing){
                printAvailableItems(items, isFirstRun);
//                System.out.print("Choice >> ");
//                int itemChoice = scanner.nextInt(); scanner.nextLine();

                int itemChoice;
                if(isFirstRun) itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length);
                else itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length + 1);

                if(itemChoice != items.length + 1) {
                    storeItems[storeItems.length - 1] = items[itemChoice - 1]; //Dodaje se na zadnje mjesto factoryItems-a
                    if(items.length > 1){
                        items = removeChosenItem(items, itemChoice);
                        storeItems = expandItemArray(storeItems);
                    }
                    else finishedChoosing = true;
                } else {
                    finishedChoosing = true;
                    storeItems = trimItemArray(storeItems);
                }
                isFirstRun = false;
            }
            ///chooseFactoryItems
            stores[i] = new Store(name, webAddress, storeItems);
        }
        return stores;
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
