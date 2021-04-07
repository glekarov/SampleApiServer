package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.sample.server.url.URLStatus;

public class JsonURLWithStatus extends JsonURL {
    private final URLStatus urlStatus;

    @JsonCreator
    public JsonURLWithStatus(String url, URLStatus urlStatus) {
        super(url);
        this.urlStatus = urlStatus;
    }

    public URLStatus getUrlStatus() {
        return urlStatus;
    }
}
