package hr.java.production.utility;

import hr.java.production.exception.InvalidRangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class for handling user input.
 * <p>
 * This class provides methods for reading and validating user input of different types.
 * It uses a {@code Scanner} object to read the input and throws exceptions if the input is not valid.
 * All methods in this class are static and belong to the class itself.
 *
 * Change to an interface instead of a class if necessary.
 */
public class InputHandler {
    private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);

    /**
     * Handles the input of an integer number from the user.
     * <p>
     * This method prompts the user to enter an integer number within a specified range. If the user enters a string instead of a number,
     * or a number outside the specified range, they are asked to enter the number again
     * and an error is logged.
     *
     * @param scanner  The {@code Scanner} object used for user input.
     * @param message  The prompt message displayed to the user.
     * @param minValue The minimum acceptable value for the input number (including).
     * @param maxValue The maximum acceptable value for the input number (including).
     * @return The valid integer number entered by the user.
     */
    public static int numInputHandler(Scanner scanner, String message, int minValue, int maxValue) {
        int enteredNumber = 0;
        boolean badFormat;
        do {
            try {
                System.out.print(message);
                enteredNumber = scanner.nextInt();
                isNumInRangeEx(enteredNumber, minValue, maxValue);
                badFormat = false;
            } catch (InputMismatchException e) {
                logger.error("Entered a string instead of a number " + e);
                System.out.println("Entered a string instead of a number. Please enter a number:");
                badFormat = true;
            } catch (InvalidRangeException e) {
                logger.error(e.getMessage() + e);
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } finally {
                scanner.nextLine();
            }
        } while (badFormat);
        return enteredNumber;
    }

    /**
     * Checks if an entered integer number is within a specified range.
     * <p>
     * This method throws an {@code InvalidRangeException} if the entered number is not within the specified range.
     *
     * @param enteredNumber The integer number to check.
     * @param minValue      The minimum acceptable value for the entered number (including).
     * @param maxValue      The maximum acceptable value for the entered number (including).
     * @throws InvalidRangeException If the entered number is not within the specified range.
     */
    private static void isNumInRangeEx(int enteredNumber, int minValue, int maxValue) throws InvalidRangeException {
        if (enteredNumber < minValue || enteredNumber > maxValue) {
            throw new InvalidRangeException("Entered a number outside of specified range [" + minValue + "," + maxValue + "]." + " Input: " + enteredNumber);
        }
    }


    /**
     * Handles the input of a BigDecimal number from the user.
     * <p>
     * This method prompts the user to enter a BigDecimal number within a specified range. If the user enters a string instead of a number,
     * or a number outside the specified range, they are asked to enter the number again
     * and an error is logged.
     *
     * @param scanner  The {@code Scanner} object used for user input.
     * @param message  The prompt message displayed to the user.
     * @param minValue The minimum acceptable value for the input number (including).
     * @param maxValue The maximum acceptable value for the input number (including).
     * @return The valid BigDecimal number entered by the user.
     */
    public static BigDecimal numInputHandler(Scanner scanner, String message, BigDecimal minValue, BigDecimal maxValue) {
        BigDecimal enteredNumber = BigDecimal.ZERO;
        boolean badFormat;

        do {
            try {
                System.out.print(message);
                enteredNumber = scanner.nextBigDecimal();
                isNumInRangeEx(enteredNumber, minValue, maxValue);
                badFormat = false;
            } catch (InputMismatchException e) {
                logger.error("Entered a string instead of a number " + e);
                System.out.println("Entered a string instead of a number. Please enter a number:");
                badFormat = true;
            } catch (InvalidRangeException e) {
                logger.error(e.getMessage() + e);
                System.out.println("Please enter a number in range: [" + minValue + "," + maxValue + "].");
                badFormat = true;
            } finally {
                scanner.nextLine();
            }
        } while (badFormat);
        return enteredNumber;
    }

    /**
     * Checks if an entered BigDecimal number is within a specified range.
     * <p>
     * This method throws an {@code InvalidRangeException} if the entered number is not within the specified range.
     *
     * @param enteredNumber The BigDecimal number to check.
     * @param minValue      The minimum acceptable value for the entered number (including).
     * @param maxValue      The maximum acceptable value for the entered number (including).
     * @throws InvalidRangeException If the entered number is not within the specified range.
     */
    private static void isNumInRangeEx(BigDecimal enteredNumber, BigDecimal minValue, BigDecimal maxValue) throws InvalidRangeException {
        if (enteredNumber.compareTo(minValue) < 0 || enteredNumber.compareTo(maxValue) > 0) {
            throw new InvalidRangeException("Entered a number outside of specified range [" + minValue + "," + maxValue + "]." + " Input: " + enteredNumber);
        }
    }

}
