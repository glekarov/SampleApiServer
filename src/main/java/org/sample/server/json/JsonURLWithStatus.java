package org.sample.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.sample.server.url.URLStatus;

/**
 * An object that represent a Json format of the URLs with their status
 */
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
