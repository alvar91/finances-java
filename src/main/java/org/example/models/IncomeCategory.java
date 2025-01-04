package org.example.models;

/**
 * Represents a category for income, with tracking of total income for that category.
 * <p>
 * This class extends {@link AbstractCategory} and provides functionality for managing and registering income.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class IncomeCategory extends AbstractCategory {
    private Double income = 0d;

    /**
     * Constructs a new {@code IncomeCategory} with the specified name and initial income.
     *
     * @param name the name of the income category
     * @param income the initial amount of income for this category
     */
    public IncomeCategory(String name, double income){
        super(name);
        this.income = income;
    }

    /**
     * Returns the total income for this category.
     *
     * @return the total income
     */
    public double getIncome(){
        return income;
    }

    /**
     * Registers additional income for this category.
     *
     * @param income the amount of income to register
     */
    public void registerIncome(Double income){
        this.income += income;
    }

    /**
     * Returns a string representation of the {@code IncomeCategory} object, including the total income for the category.
     *
     * @return a string representing the income category
     */
    public String toString(){
        return String.format("%s: %.2f%n", this.getName(), income);
    }
}
