package org.sample.server.auth;

import org.sample.server.constants.Constants;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.LinkedList;

public class UsersList {
    private static final LinkedList<AuthUser> AUTH_USERS = new LinkedList<>();

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

    public static void addUser(String username, String password) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new NullPointerException("Username is invalid or null");
        }

        if (findByUsername(username) != null) {
            throw new UsernameNotFoundException("Username already exists");
        }

        AUTH_USERS.add(new AuthUser(username, password));
    }

    public static void addToken(AuthUser authUser, String token) throws BadCredentialsException {
        AuthUser thisAuthUser = getValidLogin(authUser.getUser(), authUser.getPasswd());
        if (thisAuthUser == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        thisAuthUser.setToken(token);
    }

    public static AuthUser getValidLogin(String username, String password) throws UsernameNotFoundException {
        AuthUser authUser = findByUsername(username);

        if (authUser != null) {
            return authUser.getPasswd().equals(password) ? authUser : null;
        }

        throw new UsernameNotFoundException("Username not found");
    }

    public static boolean isValidToken(String token) throws UsernameNotFoundException {
        AuthUser authUser = findByToken(token);

        if (authUser != null) {
            return (authUser.getToken().equals(token));
        }

        throw new UsernameNotFoundException("Username not found");
    }

    @SuppressWarnings("unchecked")
    public static synchronized void readUsersFromFile() throws IOException, ClassNotFoundException {
        Object object = Constants.readObjectFromFile(Constants.getUsersFile());
        if (object != null) {
            AUTH_USERS.addAll((LinkedList<AuthUser>) object);
        }
    }

    public static synchronized void writeUsersToFile() throws IOException {
        Constants.writeObjectToFile(AUTH_USERS, Constants.getUsersFile());
    }

}
