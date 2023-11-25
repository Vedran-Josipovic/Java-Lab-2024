//package hr.java.production.utility;
//
//import hr.java.production.enumeration.Cities;
//import hr.java.production.exception.CityNotSupportedException;
//import hr.java.production.exception.IdenticalCategoryInputException;
//import hr.java.production.exception.IdenticalItemChoiceException;
//import hr.java.production.exception.InvalidStoreTypeException;
//import hr.java.production.genericsi.FoodStore;
//import hr.java.production.genericsi.TechnicalStore;
//import hr.java.production.model.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * Processes input from a Scanner object for organizing industrial production of different items.
// */
//public class ScannerInputProcessor implements InputProcessor {
//    private static final Integer NUM_CATEGORIES = 3, NUM_ITEMS = 5, NUM_FACTORIES = 2, NUM_STORES = 2;
//    private static final Integer PIZZA = 1, CHICKEN_NUGGETS = 2;
//    private static final Integer FOOD = 1, LAPTOP = 2;
//    private static final Integer TECHNICAL_STORE = 1, FOOD_STORE = 2;
//    private static final Logger logger = LoggerFactory.getLogger(ScannerInputProcessor.class);
//
//
//    /**
//     * Collects user input to create a list of categories.
//     * <p>
//     * Prompts the user to enter the name and description for each category.
//     * It ensures that no two categories have the same name and description.
//     * If a category already exists, the user is asked to enter a different category.
//     *
//     * @param scanner A Scanner object for reading user input.
//     * @return A list of categories created based on user input.
//     */
//    @Override
//    public List<Category> inputCategories(Scanner scanner) {
//        ArrayList<Category> categories = new ArrayList<>();
//        for (int i = 0; i < NUM_CATEGORIES; i++) {
//            while (true) {
//                System.out.println("Enter the information about the " + (i + 1) + ". category: ");
//
//                System.out.print("Enter the category name: ");
//                String name = scanner.nextLine();
//
//                System.out.print("Enter the category description: ");
//                String description = scanner.nextLine();
//
//                try {
//                    checkForIdenticalCategories(new Category(name, description), categories);
//                } catch (IdenticalCategoryInputException e) {
//                    logger.warn(e.getMessage());
//                    System.out.println("This category [" + name + "] has already been added. Please choose a category that hasn't been added.");
//                    continue;
//                }
//                categories.add(new Category(name, description));
//                break;
//            }
//        }
//        return categories;
//    }
//
//
//    /**
//     * Collects user input to create a list of items. <p>
//     * <p>
//     * Prompts the user to enter the details for each item, including its name, category, dimensions, production cost, selling price, and discount.
//     * If the item is food, the user can specify whether it's pizza or chicken nuggets and input its weight.
//     * If the item is a laptop, the user can input its warranty duration.
//     * After each item is created, if it's edible, it prints out its kilocalories and price with discount.
//     * The created items are added to a list which is returned at the end.
//     *
//     * @param scanner    A Scanner object for reading user input.
//     * @param categories The list of categories to choose from when creating an item.
//     * @return A list of items created based on user input.
//     */
//    @Override
//    public List<Item> inputItems(Scanner scanner, List<Category> categories) {
//        List<Item> items = new ArrayList<>();
//        for (int i = 0; i < NUM_ITEMS; i++) {
//            System.out.println("Enter the information about the " + (i + 1) + ". item: ");
//
//            System.out.print("Enter the item name: ");
//            String name = scanner.nextLine();
//
//            System.out.println("Pick the item category:");
//
//            for (int j = 0; j < categories.size(); j++)
//                System.out.println((j + 1) + ". " + categories.get(j).getName());
//            int categoryChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, categories.size());
//
//            System.out.println("Enter the item dimensions:");
//            BigDecimal width = InputHandler.numInputHandler(scanner, "Enter the item width: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//            BigDecimal height = InputHandler.numInputHandler(scanner, "Enter the item height: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//            BigDecimal length = InputHandler.numInputHandler(scanner, "Enter the item length: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//            BigDecimal productionCost = InputHandler.numInputHandler(scanner, "Enter the item production cost: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//            BigDecimal sellingPrice = InputHandler.numInputHandler(scanner, "Enter the item selling price: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//            BigDecimal discountAmount = InputHandler.numInputHandler(scanner, "Enter the discount percentage for this item: ", BigDecimal.ZERO, BigDecimal.valueOf(100));
//            Discount discount = new Discount(discountAmount);
//
//            int itemSubclassChoice = InputHandler.numInputHandler(scanner, "Is this item food, a laptop, or other:\n1. Food\n2. Laptop\n3. Other\nChoice >> ", 1, 3);
//            if (itemSubclassChoice == FOOD) {
//                Integer foodChoice = InputHandler.numInputHandler(scanner, "Pick an available food product:\n1. Pizza\n2. Chicken nuggets\nChoice >> ", PIZZA, CHICKEN_NUGGETS);
//                BigDecimal weightInKG = InputHandler.numInputHandler(scanner, "Enter the weight (in kg) of the food packet: ", BigDecimal.valueOf(1E-99), BigDecimal.valueOf(1E+99));
//
//                if (foodChoice.equals(PIZZA))
//                    items.add(new Pizza(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, weightInKG));
//                else if (foodChoice.equals(CHICKEN_NUGGETS))
//                    items.add(new ChickenNuggets(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, weightInKG));
//            } else if (itemSubclassChoice == LAPTOP) {
//                Integer warrantyYears = InputHandler.numInputHandler(scanner, "Enter the warranty duration (in years) of the laptop: ", 0, 100);
//                items.add(new Laptop(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount, warrantyYears));
//            } else {
//                items.add(new Item(name, categories.get(categoryChoice - 1), width, height, length, productionCost, sellingPrice, discount));
//            }
//
//            if (items.getLast() instanceof Edible e) {
//                System.out.println("Kilocalories in " + items.getLast().getName() + ": " + e.calculateKilocalories());
//                System.out.println("Price (with " + items.getLast().getDiscount().discountAmount() + "% discount) for " + items.getLast().getName() + ": " + e.calculatePrice());
//            }
//        }
//        return items;
//    }
//
//
//    /**
//     * Prompts the user to input information for a specified number of factories.
//     * The user can choose the name, address, and items produced by each factory.
//     * Ensures that each factory's item list is unique and not shared with other factories.
//     *
//     * @param scanner A Scanner object for user input.
//     * @param items   The list of items to choose from when creating a factory.
//     * @return A list of Factory objects created based on user input.
//     */
//    @Override
//    public List<Factory> inputFactories(Scanner scanner, List<Item> items) {
//        List<Factory> factories = new ArrayList<>();
//        List<Item> addedItems = new ArrayList<>();
//        for (int i = 0; i < NUM_FACTORIES; i++) {
//            System.out.println("Enter the information about the " + (i + 1) + ". factory: ");
//            System.out.print("Enter the factory name: ");
//            String name = scanner.nextLine();
//            System.out.println("Enter the factory address information: ");
//            Address address = inputAddress(scanner);
//            System.out.println("Pick which items the factory produces: ");
//
//            Set<Item> factoryItems = chooseItems(scanner, items, addedItems, Factory.class);
//            factories.add(new Factory(name, address, factoryItems));
//        }
//        return factories;
//    }
//
//
//    /**
//     * Prompts the user to input information for a specified number of stores and select items that each store will sell.
//     * The user chooses the name, web address, and the items to be sold by each store.
//     * After selecting items, the user is prompted to choose the type of store: Technical, Food, or General.
//     * If a Technical or Food store is selected, only the relevant items (Technical or Edible) are added to the store.
//     * The method offers a retry mechanism if the user selects a store type that doesn't match the chosen items.
//     *
//     * @param scanner A {@code Scanner} object for user input.
//     * @param items   The list of items to choose from when creating a store.
//     * @return A list of stores created based on user input. Each store is populated with the appropriate items based on its type.
//     */
//    @Override
//    public List<Store> inputStores(Scanner scanner, List<Item> items) {
//        List<Store> stores = new ArrayList<>();
//        List<Item> addedItems = new ArrayList<>();
//
//        for (int i = 0; i < NUM_STORES; i++) {
//            System.out.println("Enter the information for store " + (i + 1) + ":");
//            System.out.print("Enter the store name: ");
//            String name = scanner.nextLine();
//            System.out.print("Enter the store web address: ");
//            String webAddress = scanner.nextLine();
//
//            System.out.println("Pick which items the store sells: ");
//            Set<Item> storeItems = chooseItems(scanner, items, addedItems, Store.class);
//
//            boolean validStoreType = false;
//            while (!validStoreType) {
//                try {
//                    System.out.println("Choose the type of store: \n1. Technical Store \n2. Food Store \n3. General Store");
//                    int storeType = InputHandler.numInputHandler(scanner, "Enter your choice (1-3): ", 1, 3);
//
//                    Store store = createStoreBasedOnType(storeType, name, webAddress, storeItems);
//                    if (store instanceof TechnicalStore) {
//                        storeItems.stream().filter(item -> item instanceof Technical).forEach(item -> ((TechnicalStore<Technical>) store).addTechnicalStoreItem((Technical) item));
//                    } else if (store instanceof FoodStore) {
//                        storeItems.stream().filter(item -> item instanceof Edible).forEach(item -> ((FoodStore<Edible>) store).addFoodStoreItem((Edible) item));
//                    }
//                    stores.add(store);
//                    validStoreType = true;
//                } catch (InvalidStoreTypeException e) {
//                    logger.error(e.getMessage());
//                    System.out.println(e.getMessage() + " Please try again.");
//                }
//            }
//        }
//        return stores;
//    }
//
//    /**
//     * Creates a store based on the specified type.
//     * This method validates whether the chosen items match the store type.
//     * If the items don't match the store type, it throws an InvalidStoreTypeException.
//     *
//     * @param storeType  The type of the store to create (1 for Technical, 2 for Food, others for General).
//     * @param name       The name of the store.
//     * @param webAddress The web address of the store.
//     * @param storeItems The set of items to be added to the store.
//     * @return A new store instance of the specified type with the given items.
//     * @throws InvalidStoreTypeException If the chosen items don't match the specified store type.
//     */
//    private Store createStoreBasedOnType(int storeType, String name, String webAddress, Set<Item> storeItems) throws InvalidStoreTypeException {
//        switch (storeType) {
//            case TECHNICAL_STORE:
//                if (storeItems.stream().noneMatch(item -> item instanceof Technical))
//                    throw new InvalidStoreTypeException("Cannot choose Technical Store if there are no Technical items.");
//                return new TechnicalStore<>(name, webAddress, storeItems, new ArrayList<>());
//            case FOOD_STORE:
//                if (storeItems.stream().noneMatch(item -> item instanceof Edible))
//                    throw new InvalidStoreTypeException("Cannot choose Food Store if there are no Edible items.");
//                return new FoodStore<>(name, webAddress, storeItems, new ArrayList<>());
//            default:
//                return new Store(name, webAddress, storeItems);
//        }
//    }
//
//
//    /**
//     * Checks if a category already exists in a list of categories.
//     * <p>
//     * Compares the input category with each category in the list.
//     * If a category with the same name and description is found, an {@code IdenticalCategoryInputException} is thrown.
//     *
//     * @param categoryInput The category to check.
//     * @param categories    The list of categories to check against.
//     * @throws IdenticalCategoryInputException If the input category already exists in the list.
//     */
//    private void checkForIdenticalCategories(Category categoryInput, List<Category> categories) throws IdenticalCategoryInputException {
//        for (Category c : categories) {
//            if (c.equals(categoryInput))
//                throw new IdenticalCategoryInputException("Entered category [" + categoryInput.getName() + "] has already been added. Added Categories: " + categories.stream().map(Category::getName).collect(Collectors.joining(", ")));
//        }
//    }
//
//
//    /**
//     * Collects user input to create an {@code Address} object.
//     * <p>
//     * Prompts the user to enter the street name, house number, and city.
//     * It ensures that the entered city is valid. If the city is not valid, the user is asked to enter a different city.
//     *
//     * @param scanner A Scanner object for reading user input.
//     * @return An {@code Address} object created based on user input.
//     */
//    private Address inputAddress(Scanner scanner) {
//        System.out.print("Enter the street name: ");
//        String street = scanner.nextLine();
//
//        System.out.print("Enter the house number: ");
//        String houseNumber = scanner.nextLine();
//
//        Cities city;
//        while (true) {
//            try {
//                city = inputCity(scanner);
//                break;
//            } catch (CityNotSupportedException e) {
//                logger.error(e.getMessage());
//                System.out.println("Entered city is not in our database. Please enter a valid city.");
//            }
//        }
//
//        return new Address.Builder().atStreet(street).atHouseNumber(houseNumber).atCity(city).build();
//    }
//
//
//    /**
//     * Collects user input to determine the city.
//     * <p>
//     * Prompts the user to enter the name of a city. It checks if the entered city is in the list of supported cities.
//     * If the city is not supported, a {@code CityNotSupportedException} is thrown.
//     *
//     * @param scanner A Scanner object for reading user input.
//     * @return A {@code Cities} enum value representing the entered city.
//     * @throws CityNotSupportedException If the entered city is not supported.
//     */
//    private Cities inputCity(Scanner scanner) throws CityNotSupportedException {
//        System.out.print("Enter the city name: ");
//        String name = scanner.nextLine();
//
//        return switch (name) {
//            case "Zagreb" -> Cities.ZAGREB;
//            case "Split" -> Cities.SPLIT;
//            case "Rijeka" -> Cities.RIJEKA;
//            case "Osijek" -> Cities.OSIJEK;
//            case "Zadar" -> Cities.ZADAR;
//            case "Slavonski Brod" -> Cities.SLAVONSKI_BROD;
//            case "Velika Gorica" -> Cities.VELIKA_GORICA;
//            default ->
//                    throw new CityNotSupportedException("City not in the database: [" + name + "]. Cities in enums: [" + Arrays.stream(Cities.values()).map(Cities::getName).collect(Collectors.joining(", ")) + "]");
//        };
//    }
//
//
//    /**
//     * Allows the user to select items from a given list, ensuring that no item is selected more than once.
//     * The method behavior changes based on the class type passed to it. For Store classes, it uses a TreeSet
//     * sorted by item volume. For other classes, it uses a HashSet.
//     *
//     * @param scanner      A Scanner object for reading user input.
//     * @param items        The list of items to choose from.
//     * @param addedItems   The list of items that have already been chosen.
//     * @param callingClass The class of the caller, which determines the type of Set used for chosen items.
//     * @return A set of items chosen by the user, either sorted by volume or unordered.
//     */
//    private Set<Item> chooseItems(Scanner scanner, List<Item> items, List<Item> addedItems, Class<?> callingClass) {
//        Set<Item> chosenItemSet;
//        if (callingClass.equals(Store.class)) {
//            //Mjerenje koliko treba da se sortira jedan dućan
//            Instant startWithLambda = Instant.now();
//            chosenItemSet = new TreeSet<>((i2, i1) -> {
//                int volumeComparison = i1.calculateVolume().compareTo(i2.calculateVolume());
//                if (volumeComparison != 0) {
//                    return volumeComparison;
//                }
//                return i1.getName().compareTo(i2.getName());
//            });
//            Instant endWithLambda = Instant.now();
//            Duration durationWithLambda = Duration.between(startWithLambda, endWithLambda);
//            logger.info("Sorting with lambdas: " + durationWithLambda.toNanos());
//        } else {
//            chosenItemSet = new HashSet<>();
//        }
//
//        boolean finishedChoosing = false, isFirstRun = true;
//        while (!finishedChoosing) {
//            if (items.size() == addedItems.size()) {
//                logger.warn("All available items have been added. Possibly returning empty array.");
//                System.out.println("All available items have been added.");
//                break;
//            }
//            printAvailableItems(items, isFirstRun);
//            int itemChoice;
//            if (isFirstRun) itemChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, items.size());
//            else itemChoice = InputHandler.numInputHandler(scanner, "Choice >> ", 1, items.size() + 1);
//
//            if (itemChoice != items.size() + 1) {
//                try {
//                    checkForIdenticalItems(items.get(itemChoice - 1), addedItems);
//                } catch (IdenticalItemChoiceException e) {
//                    logger.warn(e.getMessage());
//                    System.out.println("This item [" + items.get(itemChoice - 1).getName() + "] has already been added. Please choose an item that isn't in this list: " + addedItems.stream().map(Item::getName).collect(Collectors.joining(", ")));
//                    continue;
//                }
//                chosenItemSet.add(items.get(itemChoice - 1));
//                //addedItems mijenja se i van metode
//                addedItems.add(items.get(itemChoice - 1));
//            } else finishedChoosing = true;
//            isFirstRun = false;
//        }
//        return chosenItemSet;
//    }
//
//
//    /**
//     * Displays a list of items for the user to choose from.
//     * If this is not the first run, it also prints an option for the user to finish choosing.
//     *
//     * @param items      The list of items to choose from.
//     * @param isFirstRun A boolean indicating whether this is the first run of the method.
//     */
//    private void printAvailableItems(List<Item> items, boolean isFirstRun) {
//        System.out.println("Choose an item:");
//        for (int i = 0; i < items.size(); i++) System.out.println((i + 1) + ". " + items.get(i).getName());
//        if (!isFirstRun) System.out.println(items.size() + 1 + ". " + "Finished choosing.");
//    }
//
//    /**
//     * Checks if an item has already been added to a list.
//     * It throws an IdenticalItemChoiceException if the item has already been added.
//     *
//     * @param itemChoice The Item object to check.
//     * @param addedItems A list of Item objects that have already been added.
//     * @throws IdenticalItemChoiceException If the item has already been added to the list.
//     */
//    private void checkForIdenticalItems(Item itemChoice, List<Item> addedItems) throws IdenticalItemChoiceException {
//        if (addedItems.contains(itemChoice))
//            throw new IdenticalItemChoiceException("Chosen item [" + itemChoice.getName() + "] has already been added. Added Items: " + addedItems.stream().map(Item::getName).collect(Collectors.joining(", ")));
//    }
//
//
//}
