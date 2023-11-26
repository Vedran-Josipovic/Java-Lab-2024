package hr.java.production.utility;

import hr.java.production.enumeration.*;
import hr.java.production.exception.CityNotSupportedException;
import hr.java.production.exception.IdenticalItemChoiceException;
import hr.java.production.exception.InvalidStoreTypeException;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import hr.java.production.sort.VolumeSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);


    public static List<Category> inputCategories() {
        List<Category> categories = new ArrayList<>();
        File file = new File(FilePath.CATEGORIES.getPath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {


                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();
                String description = reader.readLine();

                getValidCategory(new Category(id, name, description), categories).ifPresent(categories::add);
            }
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + FilePath.CATEGORIES.getPath() + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FilePath.CATEGORIES.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return categories;
    }

    public static List<Item> inputItems(List<Category> categories) {
        List<Item> items = new ArrayList<>();
        File file = new File(FilePath.ITEMS.getPath());

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
            String msg = "File not found at the specified location: " + FilePath.ITEMS.getPath() + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FilePath.ITEMS.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        items.forEach(item -> {
            if (item instanceof Edible e) {
                logger.info("Kilocalories in " + item.getName() + ": " + e.calculateKilocalories());
                logger.info("Price (with " + item.getDiscount().discountAmount() + "% discount) for " + item.getName() + ": " + e.calculatePrice());
            }
        });


        return items;
    }

    public static List<Factory> inputFactories(List<Item> items) {
        List<Factory> factories = new ArrayList<>();
        File file = new File(FilePath.FACTORIES.getPath());

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
            String msg = "File not found at the specified location: " + FilePath.FACTORIES.getPath() + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FilePath.FACTORIES.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return factories;
    }

    public static List<Store> inputStores(List<Item> items) {
        List<Store> stores = new ArrayList<>();
        File file = new File(FilePath.STORES.getPath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Store newStore;

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();
                String webAddress = reader.readLine();

                Optional<String> itemChoicesOptional = Optional.ofNullable(reader.readLine());

                Set<Item> storeItems = new TreeSet<>(new VolumeSorter());
                storeItems.addAll(itemChoicesOptional.map(itemChoices -> processItemChoices(itemChoices, items)).orElse(new TreeSet<>()));

                try {
                    Integer storeType = Integer.parseInt(reader.readLine());
                    newStore = createStoreBasedOnType(storeType, id, name, webAddress, storeItems);
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
            String msg = "File not found at the specified location: " + FilePath.STORES.getPath() + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FilePath.STORES.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }

        return stores;
    }


    public static <T extends Serializable> void serializeList(List<T> objectsList, FilePath path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.getPath()))) {
            oos.writeObject(objectsList);
        } catch (IOException e) {
            String msg = "SERIALIZATION ERROR: An IO Exception occurred while writing to the file: " + path.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        }
    }

    public static <T extends Serializable> List<T> deserializeList(FilePath path) {
        List<T> deserializedList = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.getPath()))) {
            deserializedList.addAll((List<T>) ois.readObject());
        } catch (FileNotFoundException e) {
            String msg = "File not found at the specified location: " + path.getPath() + ". Please check the file path and ensure the file exists.";
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "SERIALIZATION ERROR: An IO Exception occurred while reading from the file: " + path.getPath() + ". This might be due to issues with file permissions, file being in use, or other IO related problems.";
            logger.error(msg, e);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found during deserialization", e);
        }
        return deserializedList;
    }


    private static Set<Item> processItemChoices(String itemChoices, List<Item> items) {
        Set<Item> chosenItems = new HashSet<>();
        Set<Long> addedItemIds = new HashSet<>();

        Arrays.stream(itemChoices.split(",")).map(String::trim).filter(str -> !str.isEmpty()).forEach(itemIdStr -> {
            try {
                Long itemId = Long.parseLong(itemIdStr);
                checkForIdenticalItemIds(itemId, addedItemIds);  // Check for duplicates
                items.stream().filter(item -> item.getId().equals(itemId)).findFirst().ifPresent(item -> {
                    chosenItems.add(item);
                    addedItemIds.add(itemId);
                });
            } catch (NumberFormatException e) {
                logger.error("Invalid item ID format: {}", itemIdStr, e);
            } catch (IdenticalItemChoiceException e) {
                logger.warn(e.getMessage());
            }
        });

        return chosenItems;
    }

    private static void checkForIdenticalItemIds(Long itemId, Set<Long> addedItemIds) throws IdenticalItemChoiceException {
        if (addedItemIds.contains(itemId)) {
            throw new IdenticalItemChoiceException("Chosen item ID [" + itemId + "] has already been added. Input ignored.");
        }
    }

    private static Store createStoreBasedOnType(Integer storeType, Long id, String name, String webAddress, Set<Item> storeItems) throws InvalidStoreTypeException {
        StoreType type = StoreType.values()[storeType - 1];

        return switch (type) {
            case TECHNICAL_STORE -> {
                if (storeItems.stream().noneMatch(item -> item instanceof Technical)) {
                    throw new InvalidStoreTypeException("Cannot choose Technical Store if there are no Technical items.");
                }
                yield (new TechnicalStore<>(id, name, webAddress, storeItems, new ArrayList<>()));
            }
            case FOOD_STORE -> {
                if (storeItems.stream().noneMatch(item -> item instanceof Edible)) {
                    throw new InvalidStoreTypeException("Cannot choose Food Store if there are no Edible items.");
                }
                yield (new FoodStore<>(id, name, webAddress, storeItems, new ArrayList<>()));
            }
            default -> (new Store(id, name, webAddress, storeItems));  //Ovo je potrebno, ne micati
        };
    }

    private static List<Address> inputAddresses() {
        List<Address> addresses = new ArrayList<>();
        File file = new File(FilePath.ADDRESSES.getPath());

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
            String msg = "File not found at the specified location: " + FilePath.ADDRESSES.getPath();
            logger.error(msg, e);
        } catch (IOException e) {
            String msg = "An IO Exception occurred while reading the file: " + FilePath.ADDRESSES.getPath();
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

    private static Optional<Category> getValidCategory(Category categoryInput, List<Category> categories) {
        boolean isDuplicate = categories.stream().anyMatch(c -> c.equals(categoryInput));

        if (isDuplicate) {
            logger.warn("Entered category [" + categoryInput.getName() + "] has already been added. Input ignored.");
            return Optional.empty();
        } else {
            return Optional.of(categoryInput);
        }
    }


}
