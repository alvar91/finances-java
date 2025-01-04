package org.example.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wallet that contains income and expense categories and keeps track of the balance.
 * <p>
 * A wallet allows registering expenses and income for different categories, setting limits for expense categories,
 * and checking if the balance is negative or if budget limits for expenses are exceeded.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class Wallet implements Serializable {
    private final String walletName;
    private final List<ExpensesCategory> expensesCategories;
    private final List<IncomeCategory> incomeCategories;
    private double balance = 0;

    /**
     * Constructs a new {@code Wallet} with the specified wallet name.
     *
     * @param walletName the name of the wallet
     */
    public Wallet(String walletName) {
        this.walletName = walletName;
        this.expensesCategories = new ArrayList<>();
        this.incomeCategories = new ArrayList<>();
    }

    /**
     * Returns the name of the wallet.
     *
     * @return the name of the wallet
     */
    public String getName() {
        return walletName;
    }

    /**
     * Returns the current balance of the wallet.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Registers an expense for the specified category in the wallet.
     * If the category already exists, the expense is added to it; otherwise, a new category is created.
     * If the balance becomes negative, a message is printed to indicate the issue.
     *
     * @param categoryName the name of the expense category
     * @param expenses the amount of the expense
     */
    public void registerExpenses(String categoryName, double expenses) {
        ExpensesCategory category = findExpensesCategory(categoryName);
        if (category != null) {
            category.registerExpenses(expenses);
            notifyIfBudgetExceeded(categoryName, category);
        } else {
            expensesCategories.add(new ExpensesCategory(categoryName, expenses));
        }

        balance -= expenses;
        if (balance < 0) {
            System.out.println("Expenses exceed income!");
        }
    }

    /**
     * Registers income for the specified category in the wallet.
     * If the category already exists, the income is added to it; otherwise, a new category is created.
     *
     * @param category the name of the income category
     * @param income the amount of income
     */
    public void registerIncome(String category, double income) {
        IncomeCategory categoryObj = findIncomeCategory(category);
        if (categoryObj != null) {
            categoryObj.registerIncome(income);
        } else {
            incomeCategories.add(new IncomeCategory(category, income));
        }

        balance += income;
    }

    /**
     * Sets a limit for the specified expense category.
     * If the category already exists, the limit is updated; otherwise, a new category is created.
     *
     * @param categoryName the name of the expense category
     * @param limit the new budget limit for the category
     */
    public void setLimitForCategory(String categoryName, double limit) {
        ExpensesCategory category = findExpensesCategory(categoryName);
        if (category != null) {
            category.setLimit(limit);
            notifyIfBudgetExceeded(categoryName, category);
        } else {
            expensesCategories.add(new ExpensesCategory(categoryName, 0d, limit));
        }
    }

    /**
     * Returns a list of all expense categories in the wallet.
     *
     * @return a list of all expense categories
     */
    public List<ExpensesCategory> getExpensesCategories() {
        return expensesCategories;
    }

    /**
     * Returns a list of all income categories in the wallet.
     *
     * @return a list of all income categories
     */
    public List<IncomeCategory> getIncomeCategories() {
        return incomeCategories;
    }

    /**
     * Searches for an expense category by its name.
     *
     * @param categoryName the name of the expense category
     * @return the found expense category, or {@code null} if no category was found
     */
    private ExpensesCategory findExpensesCategory(String categoryName) {
        return expensesCategories.stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches for an income category by its name.
     *
     * @param categoryName the name of the income category
     * @return the found income category, or {@code null} if no category was found
     */
    private IncomeCategory findIncomeCategory(String categoryName) {
        return incomeCategories.stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Notifies if the budget for a specific expense category is exceeded.
     *
     * @param categoryName the name of the category
     * @param category the expense category to check
     */
    private void notifyIfBudgetExceeded(String categoryName, ExpensesCategory category) {
        if (category.checkLimitExceeded()) {
            System.out.println("Budget exceeded for category: " + categoryName);
        }
    }
}
