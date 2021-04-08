package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * An object representing Json format for User and its token
 */
public class JsonUser {
    private final String username;
    private final String token;

    @JsonCreator
    public JsonUser(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
