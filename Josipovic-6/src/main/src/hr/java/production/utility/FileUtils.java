package hr.java.production.utility;

import hr.java.production.enumeration.CategoryTypeChoice;
import hr.java.production.enumeration.Cities;
import hr.java.production.enumeration.FoodType;
import hr.java.production.exception.CityNotSupportedException;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String CATEGORIES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/categories.txt";
    private static final String ITEMS_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/items.txt";
    private static final String ADDRESSES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/addresses.txt";

    /**
     * Za razliku od ScannerInputProcessor.inputCategories ne provjeravaju se duplikati.
     * Možda uvesti to po ID-u idk.
     */
    public List<Category> inputCategories() {
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


    public List<Item> inputItems(List<Category> categories) {
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







    private List<Address> inputAddresses() {
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

    private Cities convertStringToCity(String cityName) throws CityNotSupportedException {
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
