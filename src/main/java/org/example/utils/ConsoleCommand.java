package org.example.utils;

/**
 * This class contains a collection of constants representing the available commands in the console application.
 * <p>
 * The commands are mapped to specific actions or operations that can be triggered by the user during interaction with the system.
 * These commands are typically used in the user interface to interpret user input and perform corresponding operations.
 * </p>
 */
public class ConsoleCommand {

   /**
    * Command for user sign-up.
    */
   public static final String SIGN_UP = "su";

   /**
    * Command for user sign-in.
    */
   public static final String SIGN_IN = "si";

   /**
    * Command for creating a new wallet.
    */
   public static final String CREATE_WALLET = "cw";

   /**
    * Command for deleting a wallet.
    */
   public static final String DELETE_WALLET = "dw";

   /**
    * Command for changing the current wallet.
    */
   public static final String CHANGE_WALLET = "chgw";

   /**
    * Command for adding income to the current wallet.
    */
   public static final String ADD_INCOME = "ainc";

   /**
    * Command for adding expense to the current wallet.
    */
   public static final String ADD_EXPENSE = "aexp";

   /**
    * Command for generating a full financial report.
    */
   public static final String GET_FULL_REPORT = "frep";

   /**
    * Command for generating an expenses report.
    */
   public static final String GET_EXPENSES_REPORT = "exprep";

   /**
    * Command for generating an income report.
    */
   public static final String GET_INCOME_REPORT = "increp";

   /**
    * Command for displaying all available categories.
    */
   public static final String ALL_CATEGORIES = "allcat";

   /**
    * Command for checking the balance of the current wallet.
    */
   public static final String GET_BALANCE = "bal";

   /**
    * Command for setting a budget limit for an expense category.
    */
   public static final String SET_CATEGORY_BUDGET = "cb";

   /**
    * Command for transferring funds between wallets.
    */
   public static final String WALLET_TRANSFER = "wtrans";

   /**
    * Command for transferring funds between users.
    */
   public static final String USER_TRANSFER = "utrans";

   /**
    * Command for logging off the current user.
    */
   public static final String LOG_OFF = "lo";

   /**
    * Command for displaying the menu of available commands.
    */
   public static final String DISPLAY_MENU = "menu";

   /**
    * Command for exiting the application.
    */
   public static final String EXIT = "x";
}
