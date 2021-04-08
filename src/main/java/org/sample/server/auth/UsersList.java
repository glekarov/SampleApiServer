package org.sample.server.auth;

import org.sample.server.constants.Constants;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.LinkedList;

/**
 * A class that stores a list of Users and process Users information
 */
public class UsersList {
    private static final LinkedList<AuthUser> AUTH_USERS = new LinkedList<>();

    /**
     * By given username gets an object to the existing users.
     *
     * @param username a username of the desired user
     * @return an object to the existing users, otherwise null
     */
    private static AuthUser findByUsername(String username) {
        if (username != null) {
            for (AuthUser authUser : AUTH_USERS) {
                if (authUser.getUser().equals(username)) {
                    return authUser;
                }
            }
        }

        return null;
    }

    /**
     * By given token gets an object to the existing users.
     *
     * @param token a token of the desired user
     * @return an object to the existing users, otherwise null
     */
    public static AuthUser findByToken(String token) {
        if (token != null) {
            for (AuthUser authUser : AUTH_USERS) {
                System.out.println(authUser.getUser() + " --> " + authUser.getToken());
            }
            for (AuthUser authUser : AUTH_USERS) {
                if (authUser.getToken().equals(token)) {
                    return authUser;
                }
            }
        }

        return null;
    }

    /**
     * Adds a new user with the given credentials. Add user in memory for fast access.
     *
     * @param username a username of the user to be created
     * @param password a passowrd of the user to be created
     * @throws UsernameNotFoundException
     */
    public static void addUser(String username, String password) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new NullPointerException("Username is empty or null");
        }

        if (password == null || password.isEmpty()) {
            throw new NullPointerException("Password is empty or null");
        }

        if (findByUsername(username) != null) {
            throw new UsernameNotFoundException("Username already exists");
        }

        AUTH_USERS.add(new AuthUser(username, password));
    }

    /**
     * Add a token to the given user, if user credentials are valid
     * @param authUser a User object containing credentials for validation
     * @param token a token to be stored for particular user
     * @throws BadCredentialsException in case of invalid credentials
     */
    public static void addToken(AuthUser authUser, String token) throws BadCredentialsException {
        AuthUser thisAuthUser = getValidLogin(authUser.getUser(), authUser.getPasswd());
        if (thisAuthUser == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        thisAuthUser.setToken(token);
    }

    /**
     * By given credentials returns a reference to a stored user if exists
     * @param username username
     * @param password password
     * @return a reference to an existing user
     * @throws UsernameNotFoundException
     */
    public static AuthUser getValidLogin(String username, String password) throws BadCredentialsException {
        AuthUser authUser = findByUsername(username);

        if (authUser != null) {
            return authUser.getPasswd().equals(password) ? authUser : null;
        }

        throw new BadCredentialsException("Invalid username or password");
    }

    /**
     * Validates if a token belongs to an existing user
     *
     * @param token a token to validate
     * @return true if the token belongs to a user, otherwise - false
     */
    public static boolean isValidToken(String token) {
        AuthUser authUser = findByToken(token);

        if (authUser != null) {
            return (authUser.getToken().equals(token));
        }

        return false;
    }

    /**
     * Reads already stored Users from data file.
     *
     * @throws IOException An error has occurred during the data file reading
     * @throws ClassNotFoundException The class of the stored data is missing
     */
    @SuppressWarnings("unchecked")
    public static synchronized void readUsersFromFile() throws IOException, ClassNotFoundException {
        Object object = Constants.readObjectFromFile(Constants.getUsersFile());
        if (object != null) {
            AUTH_USERS.addAll((LinkedList<AuthUser>) object);
        }
    }

    /**
     * Persist in memory Users into a data file.
     *
     * @throws IOException An error has occurred during the data file write
     */
    public static synchronized void writeUsersToFile() throws IOException {
        Constants.writeObjectToFile(AUTH_USERS, Constants.getUsersFile());
    }

}
