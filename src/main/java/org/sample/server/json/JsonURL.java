package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;

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
