package org.example.models;

import java.io.Serializable;

/**
 * Represents a general category with a name.
 * <p>
 * This class serves as a base for different category types in the system.
 * It is serializable, allowing it to be saved and loaded from storage.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public abstract class AbstractCategory implements Serializable {
    private final String name;

    /**
     * Constructs a new {@code AbstractCategory} with the specified name.
     *
     * @param name the name of the category
     */
    public AbstractCategory(String name){
        this.name = name;
    }

    /**
     * Returns the name of this category.
     *
     * @return the name of the category
     */
    public String getName(){
        return name;
    }
}
