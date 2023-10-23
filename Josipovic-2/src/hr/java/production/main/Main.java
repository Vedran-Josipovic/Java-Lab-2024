package hr.java.production.main;

import hr.java.production.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;
    private static final Integer PIZZA = 1, CHICKEN_NUGGETS = 2;
    private static final Integer FOOD = 1, LAPTOP = 2, OTHER = 3;
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("Josipovic-2/src/hr/java/production/files/currentInput");
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);

        Category[] categories = inputCategories(scanner);
        Item[] items = inputItems(scanner1, categories);
        Factory[] factories = inputFactories(scanner, items);
        Store[] stores = inputStores(scanner, items);

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


        System.out.println("\n\n");

    }

    //Nisam još testirao kod za ovu metodu
    private static Item findHighestPricedFood(Item[] items){
        Item mostExpensive = items[0];
        BigDecimal highestPrice = BigDecimal.valueOf(-1);
        for (Item i : items) {
            if (i instanceof Edible edible){
                BigDecimal price = edible.calculatePrice();
                if(price.compareTo(highestPrice) > 0){
                    highestPrice = price; mostExpensive = i;
                }
            }
        }
        if(highestPrice.equals(BigDecimal.valueOf(-1))) System.out.println("[ERROR] There are no food products among items. Returning the first item in array.");
        return mostExpensive;
    }

    //Nisam još testirao kod za ovu metodu
    private static Item findMostCaloricFood(Item[] items){
        Item mostCaloric = items[0];
        int maxCalories = -1;
        for (Item i : items) {
            if (i instanceof Edible edible){
                int calories = edible.calculateKilocalories();
                if(calories > maxCalories){
                    maxCalories = calories; mostCaloric = i;
                }
            }
        }
        if(maxCalories == -1) System.out.println("[ERROR] There are no food products among items. Returning the first item in array.");
        return mostCaloric;
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

            int categoryChoice = numInputHandler(scanner, "Choice >> ", 1, categories.length);

            System.out.println("Enter the item dimensions:");

            BigDecimal width = numInputHandler(scanner, "Enter the item width: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal height = numInputHandler(scanner, "Enter the item height: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal length = numInputHandler(scanner, "Enter the item length: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal productionCost = numInputHandler(scanner, "Enter the item production cost: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
            BigDecimal sellingPrice = numInputHandler(scanner, "Enter the item selling price: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));

            //Novo, testirati
            BigDecimal discountAmount = numInputHandler(scanner, "Enter the discount percentage for this item: ", BigDecimal.ZERO, BigDecimal.valueOf(100));
            Discount discount = new Discount(discountAmount);

            Boolean isEdible = isItemEdible(scanner);
            if(isEdible){
                System.out.println("Pick an available food product:");
                System.out.println("1. Pizza\n2. Chicken nuggets");
                Integer foodChoice = numInputHandler(scanner, "Choice >> ", PIZZA, CHICKEN_NUGGETS);

                BigDecimal weightInKG = numInputHandler(scanner, "Enter the weight (in kg) of the food packet: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
                if(foodChoice.equals(PIZZA)){
                    items[i] = new Pizza(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount, weightInKG);
                }
                else if(foodChoice.equals(CHICKEN_NUGGETS)){
                    items[i] = new ChickenNuggets(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount, weightInKG);
                }
            }
            else {
                items[i] = new Item(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice, discount);
            }

            if(items[i] instanceof Edible e){
                System.out.println("Kilocalories in " + items[i].getName() + ": " + e.calculateKilocalories());
                System.out.println("Price (with " + items[i].getDiscount().discountAmount() + "% discount) for " + items[i].getName() +": " + e.calculatePrice());
            }

        }
        return items;
    }

    /**
     * Unosi tvornice. Nakon što se odabere item, uklanja se it liste za odabir.
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

            ///chooseFactoryItems
            Item[] storeItems = new Item[1];
            boolean finishedChoosing = false, isFirstRun = true;
            while (!finishedChoosing){
                printAvailableItems(items, isFirstRun);

                int itemChoice;
                if(isFirstRun) itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length);
                else itemChoice = numInputHandler(scanner, "Choice >> ", 1, items.length + 1);

                if(itemChoice != items.length + 1) {
                    storeItems[storeItems.length - 1] = items[itemChoice - 1]; //Dodaje se na zadnje mjesto storeItems-a
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



    /**
     * Ispisuje sve dostupne artikle. Ukoliko je već odabran jedan artikl,
     * nudi se opcija za završavanje odabira.
     */
    private static void printAvailableItems(Item[] items, boolean isFirstRun) {
        System.out.println("Choose an item:");
        for (int i = 0; i < items.length; i++)
            System.out.println((i + 1) + ". " + items[i].getName());
        if (!isFirstRun)
            System.out.println(items.length + 1 + ". " + "Finished choosing.");
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
        boolean badFormat;
        do {
            System.out.print(message);
            while (!scanner.hasNextInt()) {
                System.out.println("Entered a string instead of a number. Please enter a number:");
                System.out.print(message);
                scanner.nextLine();
            }
            enteredNumber = scanner.nextInt(); scanner.nextLine();

            if(!isNumInRange(enteredNumber, minValue, maxValue)){
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } else badFormat = false;
        }while (badFormat);
        return enteredNumber;
    }

    /**
     * Obrađuje korisnikov numerički unos. Provjerava je li unos {@code BigDecimal} i nalazi li se unutar određenog raspona.
     * Ako unos nije {@code BigDecimal} ili je izvan raspona, traži od korisnika da unese valjani broj.
     *
     * @param scanner  Scanner objekt koji se koristi za dobivanje korisnikovog unosa.
     * @param message  Poruka koja se prikazuje korisniku prilikom traženja unosa.
     * @param minValue Minimalna prihvatljiva vrijednost za unos.
     * @param maxValue Maksimalna prihvatljiva vrijednost za unos.
     * @return         Valjani broj koji je unio korisnik.
     */
    private static BigDecimal numInputHandler(Scanner scanner, String message, BigDecimal minValue, BigDecimal maxValue){
        BigDecimal enteredNumber;
        boolean badFormat;
        do {
            System.out.print(message);
            while (!scanner.hasNextBigDecimal()) {
                System.out.println("Invalid input. Please enter a valid number:");
                System.out.print(message);
                scanner.nextLine();
            }
            enteredNumber = scanner.nextBigDecimal(); scanner.nextLine();

            if(!isNumInRange(enteredNumber, minValue, maxValue)){
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
        return enteredNumber >= minValue && enteredNumber <= maxValue;
    }

    /**
     * Provjerava je li uneseni broj unutar zadanih granica.
     *
     * @param enteredNumber Broj koji se provjerava.
     * @param minValue Minimalna dopuštena vrijednost (Uključujući).
     * @param maxValue Maksimalna dopuštena vrijednost (Uključujući)
     * @return True ako je broj unutar raspona, false ako nije.
     */
    private static boolean isNumInRange(BigDecimal enteredNumber, BigDecimal minValue, BigDecimal maxValue) {
        return enteredNumber.compareTo(minValue) >= 0 && enteredNumber.compareTo(maxValue) <= 0;
    }



    /**
     * Uklanja objekt klase {@code Item} koji smo odabrali iz niza.
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
     * Povećava niz za jedan element. (koji poprima vrijednost null)
     */
    private static Item[] expandItemArray(Item[] oldArray) {
        Item[] newArray = new Item[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }

    /**
     * Smanjuje veličinu niza za jedan.
     */
    private static Item[] trimItemArray(Item[] oldArray) {
        Item[] newArray = new Item[oldArray.length - 1];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);
        return newArray;
    }



    private static Factory findFactoryWithLargestVolumeOfAnItem(Factory[] factories){
        Factory bestFactory = factories[0];
        BigDecimal largestVolume = BigDecimal.valueOf(0);
        for (Factory f : factories)
            for (int i = 0; i < f.getItems().length; i++)
                if (f.getItems()[i].calculateVolume().compareTo(largestVolume) > 0) {
                    bestFactory = f;
                    largestVolume = f.getItems()[i].calculateVolume();
                }
        return bestFactory;
    }
    private static Store findStoreWithCheapestItem(Store[] stores){
        Store bestStore = stores[0];
        BigDecimal cheapestSellingPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        for (Store s : stores)
            for (int i = 0; i < s.getItems().length; i++)
                if (s.getItems()[i].getSellingPrice().compareTo(cheapestSellingPrice) < 0) {
                    bestStore = s;
                    cheapestSellingPrice = s.getItems()[i].getSellingPrice();
                }
        return bestStore;
    }

    private static Boolean isItemEdible(Scanner scanner){
        System.out.println("Is this item edible?\n1. Yes\n2. No");
        return numInputHandler(scanner, "Choice >> ", 1, 2) == 1;
    }
}
