package org.example.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a user in the system, with a login, password, and associated wallets.
 * <p>
 * This class provides methods for managing wallets and user information.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class User implements Serializable {
    private final String login;
    private final String password;
    private final List<Wallet> wallets;

    /**
     * Constructs a new {@code User} with the specified login and password.
     *
     * @param login the login of the user
     * @param password the password of the user
     */
    public User(String login, String password){
        this.login = login;
        this.password = password;
        wallets = new ArrayList<>();
    }

    /**
     * Returns the login of the user.
     *
     * @return the login of the user
     */
    public String getLogin(){
        return login;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword(){
        return password;
    }

    /**
     * Adds a new wallet to the user.
     *
     * @param walletName the name of the new wallet to be added
     */
    public void addWallet(String walletName){
        wallets.add(new Wallet(walletName));
    }

    /**
     * Retrieves a wallet by its name.
     *
     * @param walletName the name of the wallet to be retrieved
     * @return an {@link Optional} containing the wallet if found, otherwise an empty {@link Optional}
     */
    public Optional<Wallet> getWallet(String walletName){
        return wallets.stream().filter(w -> w.getName().equals(walletName)).findAny();
    }

    /**
     * Returns a list of all wallets associated with this user.
     *
     * @return a list of all wallets
     */
    public List<Wallet> getAllWallets(){
        return wallets;
    }

    /**
     * Deletes a specific wallet from the user's wallet list.
     *
     * @param wallet the wallet to be deleted
     */
    public void deleteWallet(Wallet wallet){
        wallets.remove(wallet);
    }
}
