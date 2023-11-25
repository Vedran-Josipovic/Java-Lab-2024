package hr.java.production.utility;

import hr.java.production.enumeration.CategoryTypeChoice;
import hr.java.production.enumeration.Cities;
import hr.java.production.enumeration.FoodType;
import hr.java.production.enumeration.StoreType;
import hr.java.production.exception.CityNotSupportedException;
import hr.java.production.exception.InvalidStoreTypeException;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String CATEGORIES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/categories.txt";
    private static final String ITEMS_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/items.txt";
    private static final String ADDRESSES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/addresses.txt";
    private static final String FACTORIES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/factories.txt";
    private static final String STORES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/stores.txt";

    /**
     * Za razliku od ScannerInputProcessor.inputCategories ne provjeravaju se duplikati.
     * Možda uvesti to po ID-u idk.
     */
    public static List<Category> inputCategories() {
        List<Category> categories = new ArrayList<>();
        File file = new File(CATEGORIES_TEXT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Optional<Category> newCategoryOptional;

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();
                String description = reader.readLine();

                newCategoryOptional = Optional.of(new Category(id, name, description));
                newCategoryOptional.ifPresent(categories::add);
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + CATEGORIES_TEXT_FILE_NAME + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + CATEGORIES_TEXT_FILE_NAME + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return categories;
    }


    public static List<Item> inputItems(List<Category> categories) {
        List<Item> items = new ArrayList<>();
        File file = new File(ITEMS_TEXT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Optional<Item> newItemOptional = Optional.empty();

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();

                Long categoryId = Long.parseLong(reader.readLine());
                Optional<Category> categoryOptional = categories.stream().filter(c -> c.getId().equals(categoryId)).findFirst();
                if (categoryOptional.isEmpty()) {
                    logger.warn("No category found for ID: {" + categoryId + "}");
                    continue;
                }

                BigDecimal width = new BigDecimal(reader.readLine());
                BigDecimal height = new BigDecimal(reader.readLine());
                BigDecimal length = new BigDecimal(reader.readLine());
                BigDecimal productionCost = new BigDecimal(reader.readLine());
                BigDecimal sellingPrice = new BigDecimal(reader.readLine());
                Discount discount = new Discount(new BigDecimal(reader.readLine()));

                Integer itemSubclassChoice = Integer.parseInt(reader.readLine());
                if (CategoryTypeChoice.FOOD.getChoice().equals(itemSubclassChoice)) {
                    Integer foodChoice = Integer.parseInt(reader.readLine());
                    BigDecimal weightInKG = new BigDecimal(reader.readLine());

                    if (FoodType.PIZZA.getChoice().equals(foodChoice)) {
                        newItemOptional = Optional.of(new Pizza(id, name, categoryOptional.get(), width, height, length, productionCost, sellingPrice, discount, weightInKG));
                    } else if (FoodType.CHICKEN_NUGGETS.getChoice().equals(foodChoice)) {
                        newItemOptional = Optional.of(new ChickenNuggets(id, name, categoryOptional.get(), width, height, length, productionCost, sellingPrice, discount, weightInKG));
                    }
                } else if (CategoryTypeChoice.LAPTOP.getChoice().equals(itemSubclassChoice)) {
                    Integer warrantyYears = Integer.parseInt(reader.readLine());
                    newItemOptional = Optional.of(new Laptop(id, name, categoryOptional.get(), width, height, length, productionCost, sellingPrice, discount, warrantyYears));
                } else {
                    newItemOptional = Optional.of(new Item(id, name, categoryOptional.get(), width, height, length, productionCost, sellingPrice, discount));
                }
                newItemOptional.ifPresent(items::add);
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + ITEMS_TEXT_FILE_NAME + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + ITEMS_TEXT_FILE_NAME + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }


        return items;
    }


    public static List<Factory> inputFactories(List<Item> items) {
        List<Factory> factories = new ArrayList<>();
        File file = new File(FACTORIES_TEXT_FILE_NAME);

        List<Address> addresses = inputAddresses();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Optional<Factory> newFactoryOptional;

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();

                int addressChoice = Integer.parseInt(reader.readLine());
                Address address = addresses.get(addressChoice - 1);


                Optional<String> itemChoicesOptional = Optional.ofNullable(reader.readLine());

                Set<Item> factoryItems = itemChoicesOptional.map(itemChoices -> processItemChoices(itemChoices, items)).orElse(new HashSet<>());

                newFactoryOptional = Optional.of(new Factory(id, name, address, factoryItems));
                newFactoryOptional.ifPresent(factories::add);
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + FACTORIES_TEXT_FILE_NAME + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FACTORIES_TEXT_FILE_NAME + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return factories;
    }

    private static Set<Item> processItemChoices(String itemChoices, List<Item> items) {
        Set<Item> chosenItems = new HashSet<>();

        Arrays.stream(itemChoices.split(",")).map(String::trim).filter(str -> !str.isEmpty()).forEach(itemIdStr -> {
            try {
                Long itemId = Long.parseLong(itemIdStr);
                items.stream().filter(item -> item.getId().equals(itemId)).findFirst().ifPresent(chosenItems::add);
            } catch (NumberFormatException e) {
                logger.error("Invalid item ID format: {}", itemIdStr, e);
            }
        });

        return chosenItems;
    }


    public static List<Store> inputStores(List<Item> items) {
        List<Store> stores = new ArrayList<>();
        File file = new File(STORES_TEXT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Store newStore;

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();
                String webAddress = reader.readLine();

                Optional<String> itemChoicesOptional = Optional.ofNullable(reader.readLine());

                Set<Item> storeItems = itemChoicesOptional.map(itemChoices -> processItemChoices(itemChoices, items)).orElse(new HashSet<>());

                try{
                    Integer storeType = Integer.parseInt(reader.readLine());
                    newStore = createStoreBasedOnType(storeType,id, name, webAddress, storeItems);
                    if (newStore instanceof TechnicalStore) {
                        storeItems.stream().filter(item -> item instanceof Technical).forEach(item -> ((TechnicalStore<Technical>) newStore).addTechnicalStoreItem((Technical) item));
                    } else if (newStore instanceof FoodStore) {
                        storeItems.stream().filter(item -> item instanceof Edible).forEach(item -> ((FoodStore<Edible>) newStore).addFoodStoreItem((Edible) item));
                    }
                    stores.add(newStore);
                } catch (InvalidStoreTypeException e) {
                    logger.warn(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + STORES_TEXT_FILE_NAME + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + STORES_TEXT_FILE_NAME + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return stores;
    }

    private static Store createStoreBasedOnType(Integer storeType, Long id, String name, String webAddress, Set<Item> storeItems) throws InvalidStoreTypeException {
        StoreType type = StoreType.values()[storeType - 1];

        switch (type) {
            case TECHNICAL_STORE:
                if (storeItems.stream().noneMatch(item -> item instanceof Technical)) {
                    throw new InvalidStoreTypeException("Cannot choose Technical Store if there are no Technical items.");
                }
                return (new TechnicalStore<>(id, name, webAddress, storeItems, new ArrayList<>()));

            case FOOD_STORE:
                if (storeItems.stream().noneMatch(item -> item instanceof Edible)) {
                    throw new InvalidStoreTypeException("Cannot choose Food Store if there are no Edible items.");
                }
                return (new FoodStore<>(id, name, webAddress, storeItems, new ArrayList<>()));

            default:
                return (new Store(id, name, webAddress, storeItems));
        }
    }




    private static List<Address> inputAddresses() {
        List<Address> addresses = new ArrayList<>();
        File file = new File(ADDRESSES_TEXT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> streetOptional;
            while ((streetOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Optional<Address> newAddressOptional;

                String street = streetOptional.get();
                String houseNumber = reader.readLine();
                String cityName = reader.readLine();

                try {
                    Cities city = convertStringToCity(cityName);
                    newAddressOptional = Optional.of(new Address.Builder().atStreet(street).atHouseNumber(houseNumber).atCity(city).build());
                    newAddressOptional.ifPresent(addresses::add);
                } catch (CityNotSupportedException e) {
                    logger.error("City not supported: {}", cityName, e);
                }
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + ADDRESSES_TEXT_FILE_NAME;
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + ADDRESSES_TEXT_FILE_NAME;
            logger.error(msg, e);
        }

        return addresses;
    }

    private static Cities convertStringToCity(String cityName) throws CityNotSupportedException {
        return switch (cityName) {
            case "Zagreb" -> Cities.ZAGREB;
            case "Split" -> Cities.SPLIT;
            case "Rijeka" -> Cities.RIJEKA;
            case "Osijek" -> Cities.OSIJEK;
            case "Zadar" -> Cities.ZADAR;
            case "Slavonski Brod" -> Cities.SLAVONSKI_BROD;
            case "Velika Gorica" -> Cities.VELIKA_GORICA;
            default ->
                    throw new CityNotSupportedException("City not in the database: [" + cityName + "]. Cities in enums: [" + Arrays.stream(Cities.values()).map(Cities::getName).collect(Collectors.joining(", ")) + "]");
        };
    }


}
