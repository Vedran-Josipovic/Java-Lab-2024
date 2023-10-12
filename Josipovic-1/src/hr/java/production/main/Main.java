package hr.java.production.main;

import hr.java.production.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final Integer NUM_CATEGORIES = 3;
    private static final Integer NUM_ITEMS = 5;
    private static final Integer NUM_FACTORIES = 2;
    private static final Integer NUM_STORES = 2;


    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        //File file = new File("Josipovic-1/src/hr/java/production/files/currentInput");
        //Scanner scanner = new Scanner(file);

        Category[] categories;
        Item[] items;

        Address[] addresses;
        Factory[] factories;
        Store[] stores;

        categories = inputCategories(scanner);
        items = inputItems(scanner, categories);


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
            for (int j = 0; j < categories.length; j++) {
                System.out.println((j + 1) + ". " + categories[j].getName());
            }
            //Exception will happen if the choice is not in range
            System.out.print("Choice >> ");
            int categoryChoice = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the item dimensions:");

            System.out.print("Enter the item width: ");
            BigDecimal width = scanner.nextBigDecimal();
            scanner.nextLine();

            System.out.print("Enter the item height: ");
            BigDecimal height = scanner.nextBigDecimal();
            scanner.nextLine();

            System.out.print("Enter the item length: ");
            BigDecimal length = scanner.nextBigDecimal();
            scanner.nextLine();

            System.out.println();

            System.out.print("Enter the item production cost: ");
            BigDecimal productionCost = scanner.nextBigDecimal();
            scanner.nextLine();

            System.out.print("Enter the item selling price: ");
            BigDecimal sellingPrice = scanner.nextBigDecimal();
            scanner.nextLine();

            items[i] = new Item(name, categories[categoryChoice - 1], width, height, length, productionCost, sellingPrice);
        }

        return items;
    }

    //The assignment is asking me to input the Factory before the address, but the address is the part of the factory.
    //I think I'm going to add addresses while adding the factories.
    private static Factory[] inputFactories(Scanner scanner){
        Factory[] factories = new Factory[NUM_FACTORIES];



        return factories;
    }


}
