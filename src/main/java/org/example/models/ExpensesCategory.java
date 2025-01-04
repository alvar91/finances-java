package org.example.models;

/**
 * Represents a category for expenses, with tracking of expenses and a budget limit.
 * <p>
 * This class extends {@link AbstractCategory} and provides functionality for managing expenses,
 * setting and checking budget limits, and registering new expenses.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class ExpensesCategory extends AbstractCategory {

    private Double expenses = 0d;
    private Double limit = null;

    /**
     * Constructs a new {@code ExpensesCategory} with the specified name and initial expenses.
     *
     * @param name the name of the expense category
     * @param expenses the initial amount of expenses
     */
    public ExpensesCategory(String name, Double expenses){
        super(name);
        this.expenses += expenses;
    }

    /**
     * Constructs a new {@code ExpensesCategory} with the specified name, initial expenses, and a budget limit.
     *
     * @param name the name of the expense category
     * @param expenses the initial amount of expenses
     * @param limit the budget limit for the category
     */
    public ExpensesCategory(String name, Double expenses, Double limit){
        super(name);
        this.expenses += expenses;
        this.limit = limit;
    }

    /**
     * Returns the total expenses for this category.
     *
     * @return the total expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Sets a budget limit for this expense category.
     *
     * @param limit the new budget limit
     */
    public void setLimit(Double limit){
        this.limit = limit;
    }

    /**
     * Registers additional expenses for this category.
     *
     * @param expenses the amount of expenses to register
     */
    public void registerExpenses(Double expenses){
        this.expenses += expenses;
    }

    /**
     * Checks if the expenses have exceeded the limit for this category.
     *
     * @return true if the expenses exceed the limit, false otherwise
     */
    public boolean checkLimitExceeded(){
        if(limit == null) return false;
        return expenses > limit;
    }

    /**
     * Returns a string representation of the {@code ExpensesCategory} object, including the total expenses
     * and the remaining budget if a limit is set.
     *
     * @return a string representing the expenses category
     */
    public String toString(){
        if(limit != null){
            return String.format("%s: %.2f, Remaining budget: %.2f%n", this.getName(), expenses, limit-expenses);
        }
        return String.format("%s: %.2f%n", this.getName(), expenses);
    }
}
