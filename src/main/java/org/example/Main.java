package org.example;

import org.example.controllers.FinanceController;

/**
 * The entry point of the application.
 * <p>
 * This class initializes and runs the {@link FinanceController}, which handles
 * the application logic and user interactions related to finance management.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class Main {

    /**
     * The main method which runs the application.
     * <p>
     * It creates an instance of {@link FinanceController} and calls its run method to start the process.
     * </p>
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create an instance of the FinanceController
        FinanceController financeController = new FinanceController();

        // Run the finance controller to start the application logic
        financeController.run();
    }
}
