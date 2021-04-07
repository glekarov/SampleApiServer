package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;

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
