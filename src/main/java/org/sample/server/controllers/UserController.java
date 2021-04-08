package org.sample.server.controllers;

import org.sample.server.auth.AuthUser;
import org.sample.server.auth.UsersList;
import org.sample.server.json.JsonUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

/**
 * Controller class which manage the server's users
 */
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    /**
     * Generates and store a token for particular user. This method authenticates the user and generates
     * token only for user who has passed the correct credentials
     *
     * @param username a username of the authenticated user
     * @param passwd a password of the authenticated user
     * @return a Json object containing the username and the token
     */
    @PostMapping("/user/token")
    public JsonUser getToken(@RequestParam(USERNAME) String username, @RequestParam(PASSWORD) String passwd) {
        logger.info("Add token for : " + username);
        try {
            String token = UUID.randomUUID().toString();

            UsersList.addToken(new AuthUser(username, passwd), token);
            UsersList.writeUsersToFile();

            logger.debug("token " + token + " saved for user " + username);

            return new JsonUser(username, token);
        } catch (IOException ioException) {
            logger.error("Failed to store username: " + username);
        }

        return null;
    }

    /**
     * Add a new user into the server. The user will be persisted and it'll be available after a restart
     *
     * @param username a username of the user to create
     * @param passwd a password of the user to create
     */
    @PostMapping("/user/add_user")
    public void addUser(@RequestParam(USERNAME) String username, @RequestParam(PASSWORD) String passwd) {
        logger.info("Add user: " + username);
        try {
            UsersList.addUser(username, passwd);
            UsersList.writeUsersToFile();
        } catch (IOException ioException) {
            logger.error("Failed to store username: " + username);
        }
    }
}
