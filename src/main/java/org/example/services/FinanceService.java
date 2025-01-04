package org.example.services;

import org.example.models.ExpensesCategory;
import org.example.models.IncomeCategory;
import org.example.models.User;
import org.example.models.Wallet;
import org.example.repositories.UserDataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing the financial operations for users.
 * <p>
 * The {@code FinanceService} class provides methods for user registration, login, managing wallets, recording income and expenses,
 * transferring funds between wallets and users, setting category limits, and displaying financial data.
 * It handles all operations regarding the management of wallets and categories for a specific user.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class FinanceService {
    private final ArrayList<User> users;
    private User currentUser;
    private Wallet currentWallet;
    private final UserDataStorage userDataStorage;

    /**
     * Constructs a new {@code FinanceService} instance, loading persisted user data.
     */
    public FinanceService() {
        userDataStorage = new UserDataStorage("src/main/resources/persistedData.txt");
        users = userDataStorage.getPersistedUserData();
    }

    /**
     * Saves the current state of user data to persistent storage.
     */
    public void persistChanges() {
        userDataStorage.persistUserData(users);
    }

    /**
     * Registers a new user if a user with the given login does not already exist.
     *
     * @param login    the login of the new user
     * @param password the password for the new user
     * @return true if the user was successfully registered, false if the user already exists
     */
    public boolean registerUserIfNotExists(String login, String password) {
        if (users.stream().anyMatch(u -> u.getLogin().equals(login))) {
            return false;
        }
        User user = new User(login, password);
        users.add(user);
        currentUser = user;
        return true;
    }

    /**
     * Logs in the user if the login and password are valid.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return true if login is successful, false if invalid credentials
     */
    public boolean logInUserIfValid(String login, String password) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findAny()
                .map(user -> {
                    currentUser = user;
                    return true;
                })
                .orElse(false);
    }

    /**
     * Adds a new wallet to the current user if the wallet does not already exist.
     *
     * @param walletName the name of the wallet to add
     * @return true if the wallet was successfully added, false if the wallet already exists
     */
    public boolean addWalletToUser(String walletName) {
        verifyCurrentUser();
        if (currentUser.getAllWallets().stream().anyMatch(w -> w.getName().equals(walletName))) {
            return false;
        }
        currentUser.addWallet(walletName);
        return true;
    }

    /**
     * Displays the names of all wallets belonging to the current user.
     */
    public void displayUserWalletNames() {
        verifyCurrentUser();
        List<String> walletNames = currentUser.getAllWallets().stream().map(Wallet::getName).toList();
        System.out.print("Your wallets: ");
        walletNames.forEach(wallet -> System.out.print(wallet + " "));
        System.out.println();
    }

    /**
     * Sets the active wallet for the current user.
     *
     * @param walletName the name of the wallet to set as active
     * @return true if the wallet was successfully set as active, false if the wallet does not exist
     */
    public boolean setActiveWallet(String walletName) {
        verifyCurrentUser();
        return currentUser.getWallet(walletName)
                .map(wallet -> {
                    currentWallet = wallet;
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("A wallet with this name does not exist.");
                    return false;
                });
    }

    /**
     * Displays the expense categories of the current wallet.
     */
    public void displayExpenseCategories() {
        verifyCurrentWallet();
        displayCategoryNames("Your expense categories: ", currentWallet.getExpensesCategories());
    }

    /**
     * Displays the income categories of the current wallet.
     */
    public void displayIncomeCategories() {
        verifyCurrentWallet();
        displayCategoryNames("Your income categories: ", currentWallet.getIncomeCategories());
    }

    /**
     * Displays the expenses of the current wallet.
     */
    public void displayExpenses() {
        verifyCurrentWallet();
        displayExpensesByCategories(currentWallet.getExpensesCategories());
    }

    /**
     * Displays the expenses of specific categories from the current wallet.
     *
     * @param names the names of the categories to display
     */
    public void displayExpenses(List<String> names) {
        verifyCurrentWallet();
        List<ExpensesCategory> targetCategories = findCategoriesByName(names, currentWallet.getExpensesCategories(), "Category %s not found!");
        displayExpensesByCategories(targetCategories);
    }

    /**
     * Displays the income of the current wallet.
     */
    public void displayIncome() {
        verifyCurrentWallet();
        displayIncomeByCategories(currentWallet.getIncomeCategories());
    }

    /**
     * Displays the income of specific categories from the current wallet.
     *
     * @param names the names of the categories to display
     */
    public void displayIncome(List<String> names) {
        verifyCurrentWallet();
        List<IncomeCategory> targetCategories = findCategoriesByName(names, currentWallet.getIncomeCategories(), "Category %s not found!");
        displayIncomeByCategories(targetCategories);
    }

    /**
     * Displays the balance of the current wallet.
     */
    public void displayBalance() {
        verifyCurrentWallet();
        System.out.printf("Balance: %.2f%n", currentWallet.getBalance());
    }

    /**
     * Sets a limit for a specific expense category in the current wallet.
     *
     * @param categoryName the name of the category
     * @param limit        the limit to set for the category
     */
    public void setCategoryLimit(String categoryName, double limit) {
        verifyCurrentWallet();
        currentWallet.setLimitForCategory(categoryName, limit);
    }

    /**
     * Records an expense for a specific category in the current wallet.
     *
     * @param categoryName the name of the category
     * @param expenses     the amount of the expense
     */
    public void recordExpenses(String categoryName, double expenses) {
        verifyCurrentWallet();
        currentWallet.registerExpenses(categoryName, expenses);
    }

    /**
     * Records an income for a specific category in the current wallet.
     *
     * @param categoryName the name of the category
     * @param income       the amount of income
     */
    public void recordIncome(String categoryName, double income) {
        verifyCurrentWallet();
        currentWallet.registerIncome(categoryName, income);
    }

    /**
     * Transfers funds between wallets.
     *
     * @param walletName the name of the target wallet
     * @param sum        the amount to transfer
     * @return true if the transfer was successful, false otherwise
     */
    public boolean executeTransferBetweenWallets(String walletName, double sum) {
        verifyCurrentUser();
        verifyCurrentWallet();

        if (walletName.equals(currentWallet.getName())) {
            System.out.println("The wallet must not match the current one.");
            return false;
        }

        if (currentWallet.getBalance() < sum) {
            System.out.println("Insufficient funds!");

            return false;
        }

        return currentUser.getWallet(walletName)
                .map(targetWallet -> {
                    targetWallet.registerIncome("Transfers", sum);
                    currentWallet.registerExpenses("Transfers", sum);
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Invalid wallet name!");
                    return false;
                });
    }

    /**
     * Transfers funds between users' wallets.
     *
     * @param userLogin  the login of the target user
     * @param walletName the name of the target wallet
     * @param sum        the amount to transfer
     * @return true if the transfer was successful, false otherwise
     */
    public boolean executeTransferBetweenUsers(String userLogin, String walletName, double sum) {
        verifyCurrentUser();
        verifyCurrentWallet();

        if (currentWallet.getBalance() < sum) {
            System.out.println("Insufficient funds!");
            return false;
        }

        if (walletName.equals(currentWallet.getName()) && userLogin.equals(currentUser.getLogin())) {
            System.out.println("The wallet must not match the current one!");
            return false;
        }

        return users.stream()
                .filter(user -> user.getLogin().equals(userLogin))
                .findFirst()
                .flatMap(targetUser -> targetUser.getWallet(walletName))
                .map(targetWallet -> {
                    targetWallet.registerIncome("Transfers", sum);
                    currentWallet.registerExpenses("Transfers", sum);
                    return true;
                })
                .orElseGet(() -> {
                    System.out.printf("User %s or wallet %s not found!%n", userLogin, walletName);
                    return false;
                });
    }

    /**
     * Deletes a wallet for the current user.
     *
     * @param walletName the name of the wallet to delete
     * @return true if the wallet was successfully deleted, false otherwise
     */
    public boolean deleteWallet(String walletName) {
        verifyCurrentUser();
        verifyCurrentWallet();

        if (walletName.equals(currentWallet.getName())) {
            System.out.println("Cannot delete current wallet!");
            return false;
        }

        return currentUser.getWallet(walletName)
                .map(wallet -> {
                    currentUser.deleteWallet(wallet);
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Wallet not found: " + walletName);
                    return false;
                });
    }

    /**
     * Verifies that a current user is defined.
     * <p>
     * If the {@code currentUser} is {@code null}, an error message is printed and a {@code RuntimeException} is thrown.
     * This method ensures that user-related operations are only performed when a user is logged in.
     * </p>
     */
    private void verifyCurrentUser() {
        if (currentUser == null) {
            System.out.println("User not defined!");
            throw new RuntimeException();
        }
    }

    /**
     * Verifies that a current wallet is specified.
     * <p>
     * If the {@code currentWallet} is {@code null}, an error message is printed and a {@code RuntimeException} is thrown.
     * This method ensures that wallet-related operations are only performed when a wallet is set.
     * </p>
     */
    private void verifyCurrentWallet() {
        if (currentWallet == null) {
            System.out.println("Wallet not specified!");
            throw new RuntimeException();
        }
    }

    /**
     * Finds categories by their names from a list of categories.
     * <p>
     * This method searches for categories whose names contain any of the specified {@code names}.
     * If a category name is found, it is added to the result list. If no matching category is found, an error message is printed.
     * </p>
     *
     * @param names      the list of category names to search for
     * @param categories the list of categories to search within
     * @param errorMessage the error message to print if a category is not found
     * @param <T>        the type of category (could be {@link ExpensesCategory} or {@link IncomeCategory})
     * @return a list of categories that match the specified names
     */
    private <T> List<T> findCategoriesByName(List<String> names, List<T> categories, String errorMessage) {
        List<T> result = new ArrayList<>();
        for (String name : names) {
            categories.stream()
                    .filter(category -> category.toString().contains(name))
                    .findFirst()
                    .ifPresentOrElse(result::add, () -> System.out.printf(errorMessage + "%n", name));
        }
        return result;
    }

    /**
     * Displays the names of the categories.
     * <p>
     * This method prints the specified {@code message}, followed by the string representations of the given list of categories.
     * </p>
     *
     * @param message   the message to display before the categories
     * @param categories the list of categories to display
     */
    private void displayCategoryNames(String message, List<?> categories) {
        System.out.println(message);
        categories.forEach(category -> System.out.println(category.toString()));
    }

    /**
     * Displays the income categorized by the categories in the current wallet.
     * <p>
     * This method prints the income for each category followed by the total income across all categories.
     * </p>
     *
     * @param categories the list of income categories to display
     */
    private void displayIncomeByCategories(List<IncomeCategory> categories) {
        System.out.println("\n" +
                "Income by category:");
        categories.forEach(category -> System.out.print(category));
        System.out.printf("Total: %.2f%n", categories.stream().mapToDouble(IncomeCategory::getIncome).sum());
    }

    /**
     * Displays the expenses categorized by the categories in the current wallet.
     * <p>
     * This method prints the expenses for each category followed by the total expenses across all categories.
     * </p>
     *
     * @param categories the list of expense categories to display
     */
    private void displayExpensesByCategories(List<ExpensesCategory> categories) {
        System.out.println("Expenses by category:");
        categories.forEach(category -> System.out.print(category));
        System.out.printf("Total: %.2f%n", categories.stream().mapToDouble(ExpensesCategory::getExpenses).sum());
    }
}
