package org.example.controllers;

import org.example.services.FinanceService;
import org.example.utils.ConsoleCommand;

import java.util.Arrays;
import java.util.Scanner;

/**
 * The controller responsible for handling user interactions related to financial operations.
 * <p>
 * This class interacts with the {@link FinanceService} to manage wallets, record income and expenses,
 * and generate financial reports. It also processes user authentication and manages different financial
 * operations based on console commands.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class FinanceController {
    private final Scanner input;

    private final FinanceService financeService;

    /**
     * Constructs a new {@link FinanceController} object, initializing the input scanner and the {@link FinanceService}.
     */
    public FinanceController() {
        input = new Scanner(System.in);
        financeService = new FinanceService();
    }

    /**
     * Starts the application and processes user authentication followed by command execution.
     * If authentication succeeds, the user is presented with available commands.
     */
    public void run() {
        showWelcomeMessage();

        try{
            if (!processUserAuthentication()) run();
            else {
                showCommandMenu();
                processConsoleCommands();
            }
        }
        catch(Exception e){
            financeService.persistChanges();
            throw e;
        }
    }

    /**
     * Displays the welcome message with instructions for new users and available commands.
     */
    private void showWelcomeMessage() {
        System.out.printf("If you don't have an account, enter %s to register or enter the command %s to log in to an existing account.%n",
                ConsoleCommand.SIGN_UP, ConsoleCommand.SIGN_IN);

        System.out.printf("To exit the application, enter %s.%n",
                ConsoleCommand.EXIT);

        System.out.printf("The list of available commands is accessible at: %s.%n",
                ConsoleCommand.DISPLAY_MENU);
    }

    /**
     * Displays the list of available commands for the user.
     */
    private void showCommandMenu() {
        System.out.println("List of available commands: ");
        System.out.printf("%s - create a new wallet;%n", ConsoleCommand.CREATE_WALLET);
        System.out.printf("%s - switch wallet;%n", ConsoleCommand.CHANGE_WALLET);
        System.out.printf("%s - delete a wallet;%n", ConsoleCommand.DELETE_WALLET);
        System.out.printf("%s - record income;%n", ConsoleCommand.ADD_INCOME);
        System.out.printf("%s - record expenses;%n", ConsoleCommand.ADD_EXPENSE);
        System.out.printf("%s - get current balance information;%n", ConsoleCommand.GET_BALANCE);
        System.out.printf("%s - get income report;%n", ConsoleCommand.GET_INCOME_REPORT);
        System.out.printf("%s - get expenses report;%n", ConsoleCommand.GET_EXPENSES_REPORT);
        System.out.printf("%s - get full report;%n", ConsoleCommand.GET_FULL_REPORT);
        System.out.printf("%s - set budget for a category;%n", ConsoleCommand.SET_CATEGORY_BUDGET);
        System.out.printf("%s - transfer funds to another wallet;%n", ConsoleCommand.WALLET_TRANSFER);
        System.out.printf("%s - transfer funds to another user;%n", ConsoleCommand.USER_TRANSFER);
        System.out.printf("%s - log out of the account.%n", ConsoleCommand.LOG_OFF);
        System.out.println();
    }

    /**
     * Processes user commands by repeatedly prompting the user for input and executing the corresponding actions.
     */
    private void processConsoleCommands() {
        while (true) {
            System.out.println("Enter command: ");
            String command = parseInput();
            if (!executeCommand(command)) {
                System.out.println("Invalid command! Please try again.");
            } else {
                System.out.println("The operation was successful.");
            }
        }
    }

    /**
     * Executes the given command by matching it to a predefined set of commands.
     *
     * @param command The user input command.
     * @return {@code true} if the command was valid and executed, {@code false} otherwise.
     */
    private boolean executeCommand(String command) {
        switch (command) {
            case ConsoleCommand.GET_BALANCE -> financeService.displayBalance();
            case ConsoleCommand.ADD_INCOME -> recordUserIncome();
            case ConsoleCommand.ADD_EXPENSE -> recordUserExpense();
            case ConsoleCommand.GET_FULL_REPORT -> generateFullReport();
            case ConsoleCommand.GET_INCOME_REPORT -> generateIncomeReport();
            case ConsoleCommand.GET_EXPENSES_REPORT -> generateExpensesReport();
            case ConsoleCommand.SET_CATEGORY_BUDGET -> setExpenseCategoryLimit();
            case ConsoleCommand.CHANGE_WALLET -> switchWallet();
            case ConsoleCommand.CREATE_WALLET -> createWalletForUser(false);
            case ConsoleCommand.WALLET_TRANSFER -> transferFundsBetweenWallets();
            case ConsoleCommand.USER_TRANSFER -> transferFundsBetweenUsers();
            case ConsoleCommand.DELETE_WALLET -> deleteWallet();
            case ConsoleCommand.LOG_OFF -> {
                financeService.persistChanges();
                run();
            }
            case ConsoleCommand.EXIT -> {
                financeService.persistChanges();
                System.exit(0);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles user authentication by processing sign-up or sign-in commands.
     *
     * @return {@code true} if authentication was successful, {@code false} otherwise.
     */
    private boolean processUserAuthentication(){
        var command = parseInput();
        System.out.println(command);
        switch (command){
            case ConsoleCommand.SIGN_UP:
                if (!attemptUserRegistration()) return false;
                createWalletForUser(true);
                return true;
            case ConsoleCommand.SIGN_IN:
                if(!attemptUserLogIn()) return false;
                selectOrCreateWallet();
                return true;
            case ConsoleCommand.DISPLAY_MENU:
                showCommandMenu();
                return false;

            default:
                System.out.printf("You entered an invalid command! Please enter %s or %s.",
                        ConsoleCommand.SIGN_IN, ConsoleCommand.SIGN_UP);

                return false;
        }
    }

    /**
     * Prompts the user to select or create a wallet.
     */
    private void selectOrCreateWallet(){
        System.out.println("Please select a wallet.");
        financeService.displayUserWalletNames();
        System.out.printf("%nIf you want to create a new wallet, enter %s%n", ConsoleCommand.CREATE_WALLET);
        var command = parseInput();
        if(command.equals(ConsoleCommand.CREATE_WALLET)){
            createWalletForUser(false);
            selectOrCreateWallet();
        }
        else if(!financeService.setActiveWallet(command))
        {
            System.out.println("Please try again.");
            selectOrCreateWallet();
        }
    }

    /**
     * Attempts to register a new user.
     *
     * @return {@code true} if registration is successful, {@code false} otherwise.
     */
    private boolean attemptUserRegistration(){
        int attempts = 5;

        while(attempts > 0) {
            System.out.println("Enter login: ");
            String login = parseInput();
            System.out.println("Enter password: ");
            String password = parseInput();
            if (financeService.registerUserIfNotExists(login, password))
            {
                System.out.println("Registration completed successfully.");
                return true;
            }
            else{
                attempts--;
                System.out.println("Registration attempt failed: a user with this login already exists." +
                        "Attempts remaining: " + attempts);
            }
        }
        return false;
    }

    /**
     * Attempts to log in an existing user.
     *
     * @return {@code true} if login is successful, {@code false} otherwise.
     */
    private boolean attemptUserLogIn(){
        int attempts = 5;

        while(attempts > 0) {
            System.out.println("Enter login: ");
            String login = parseInput();
            System.out.println("Enter password: ");
            String password = parseInput();

            if(financeService.logInUserIfValid(login, password)){
                System.out.printf("Hello, %s!%n", login);
                return true;
            }
            else {
                attempts--;
                System.out.println("Login attempt failed. Attempts remaining: " + attempts);
            }
        }

        return false;
    }

    /**
     * Creates a new wallet for the user.
     *
     * @param newUser {@code true} if this is a new user, {@code false} otherwise.
     */
    private void createWalletForUser(boolean newUser){
        System.out.println("Enter the name of the new wallet: ");
        String walletName = parseInput();
        if (financeService.addWalletToUser(walletName))
        {
            System.out.println("Wallet successfully created.");
            if(newUser){
                financeService.setActiveWallet(walletName);
            }
            return;
        }

        System.out.println("A wallet with this name already exists! Please try again.");
        createWalletForUser(newUser);
    }

    /**
     * Records user income by prompting the user for the income category and amount.
     * If the amount is valid, it is recorded using the finance service.
     * If an error occurs while reading the amount, the process is retried.
     */
    private void recordUserIncome(){
        System.out.println("Enter income category: ");
        String category = parseInput();
        System.out.println("Enter amount: ");
        double income;
        try {
            income = Double.parseDouble(parseInput());
            financeService.recordIncome(category, income);
        }
        catch(Exception  ex){
            System.out.println("Failed to read the amount. Please try again.");
            recordUserIncome();
        }
    }

    /**
     * Records user expenses by prompting the user for the expense category and amount.
     * If the amount is valid, it is recorded using the finance service.
     * If an error occurs while reading the amount, the process is retried.
     */
    private void recordUserExpense(){
        System.out.println("Enter expense category: ");
        String category = parseInput();
        System.out.println("Enter amount: ");
        double income;
        try {
            income = Double.parseDouble(parseInput());
            financeService.recordExpenses(category, income);
        }
        catch(Exception  ex){
            System.out.println("Failed to read the amount. Please try again.");
            recordUserExpense();
        }
    }

    /**
     * Generates a full financial report, which includes displaying the current balance, income, and expenses.
     */
    private void generateFullReport(){
        financeService.displayBalance();
        System.out.println();
        financeService.displayIncome();
        System.out.println();
        financeService.displayExpenses();
    }

    /**
     * Generates an income report based on selected income categories.
     * If the user enters the command to display all categories, a full income report is displayed.
     */
    private void generateIncomeReport(){
        System.out.printf("Select income categories for the report (enter names separated by spaces).%n" +
                "If you want a report for all categories, enter the command %s%n", ConsoleCommand.ALL_CATEGORIES);
        financeService.displayIncomeCategories();
        var command = parseInput().split(" ");

        if(command[0].equals(ConsoleCommand.ALL_CATEGORIES)){
            financeService.displayIncome();
            return;
        }

        financeService.displayIncome(Arrays.stream(command).toList());
    }

    /**
     * Generates an expenses report based on selected expense categories.
     * If the user enters the command to display all categories, a full expenses report is displayed.
     */
    private void generateExpensesReport(){
        System.out.printf("Select expense categories for the report (enter names separated by spaces).%n" +
                "If you want a report for all categories, enter the command %s%n", ConsoleCommand.ALL_CATEGORIES);
        financeService.displayExpenseCategories();
        var command = parseInput().split(" ");

        if(command[0].equals(ConsoleCommand.ALL_CATEGORIES)){
            financeService.displayExpenses();
            return;
        }

        financeService.displayExpenses(Arrays.stream(command).toList());
    }

    /**
     * Sets a budget limit for a specific expense category.
     * The user is prompted for the category and budget limit.
     * If an invalid amount is entered, the process is retried.
     */
    private void setExpenseCategoryLimit(){
        System.out.println("Enter the expense category: ");
        String category = parseInput();
        System.out.println("Enter the budget: ");
        double limit;
        try {
            limit = Double.parseDouble(parseInput());
        }
        catch(Exception  ex){
            System.out.println("Failed to read the budget. Please try again.");
            setExpenseCategoryLimit();
            return;
        }
        financeService.setCategoryLimit(category, limit);
    }

    /**
     * Switches to a different wallet based on user input.
     * If the wallet does not exist or is invalid, the process is retried.
     */
    private void switchWallet(){
        System.out.println("Enter the name of the wallet you want to switch to: ");
        financeService.displayUserWalletNames();
        String wallet = parseInput();
        if(!financeService.setActiveWallet(wallet)){
            System.out.println("Please try again");
            switchWallet();
        }
    }

    /**
     * Transfers funds between wallets.
     * The user is prompted to select a wallet and enter the amount to transfer.
     * If the transaction fails, the process is retried.
     */
    private void transferFundsBetweenWallets(){
        System.out.println("Choose the wallet to which you want to transfer funds");
        financeService.displayUserWalletNames();
        String wallet = parseInput();
        System.out.println("Enter the amount: ");;
        double sum;
        try{
            sum = Double.parseDouble(parseInput());
        }
        catch(Exception ex){
            System.out.println("Failed to read the amount. Please try again.");
            transferFundsBetweenWallets();
            return;
        }
        if(!financeService.executeTransferBetweenWallets(wallet, sum)){
            System.out.println("The operation failed. Please try again.");
            transferFundsBetweenWallets();
        }
    }

    /**
     * Transfers funds between users.
     * The user is prompted to select a recipient user, a wallet, and the amount to transfer.
     * If the transaction fails, the process is retried.
     */
    private void transferFundsBetweenUsers(){
        System.out.println("Select the user to whom you want to transfer funds.");
        String user = parseInput();
        System.out.println("Enter the name of the user's wallet to which you want to transfer funds.");
        String wallet = parseInput();
        System.out.println("Enter the amount: ");;
        double sum;
        try{
            sum = Double.parseDouble(parseInput());
        }
        catch(Exception ex){
            System.out.println("Failed to read the amount. Please try again.");
            transferFundsBetweenUsers();
            return;
        }
        if(!financeService.executeTransferBetweenUsers(user, wallet, sum)){
            System.out.println("The operation failed. Please try again.");
            transferFundsBetweenUsers();
        }
    }

    /**
     * Deletes a user's wallet.
     * The user is prompted to enter the name of the wallet to be deleted.
     * If the operation fails, the process is retried.
     */
    private void deleteWallet(){
        System.out.println("Enter the name of the wallet you want to delete.");
        financeService.displayUserWalletNames();

        String wallet = parseInput();
        if(!financeService.deleteWallet(wallet)){
            System.out.println("The operation failed. Please try again.");
            deleteWallet();
        }
    }

    /**
     * Reads the user input from the console.
     *
     * @return The processed user input.
     */
    private String parseInput() {
        String input = this.input.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Empty input. Please try again.");
            return parseInput();
        }
        processGlobalCommands(input);
        return input;
    }

    /**
     * Processes global commands like logging off or exiting the application.
     *
     * @param input The user input.
     */
    private void processGlobalCommands(String input) {
        switch (input) {
            case ConsoleCommand.DISPLAY_MENU -> {
                showCommandMenu();
            }
            case ConsoleCommand.LOG_OFF -> {
                financeService.persistChanges();
                run();
            }
            case ConsoleCommand.EXIT -> {
                financeService.persistChanges();
                System.exit(0);
            }
        }
    }
}
