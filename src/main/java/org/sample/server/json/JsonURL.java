package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * An object that represent a Json format of the URLs read from the service input
 */
public class JsonURL {
    private final String url;

    @JsonCreator
    public JsonURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
