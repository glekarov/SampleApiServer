package org.sample.server.controllers;

import org.sample.server.json.JsonURL;
import org.sample.server.json.JsonURLWithStatus;
import org.sample.server.url.URLsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
public class ServerController {
    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    @RequestMapping("/urls/send")
    public ResponseEntity<List<JsonURL>> readURLs(@RequestBody List<JsonURL> jsonURLS) {
        jsonURLS.forEach(this::storeURLs);
        return ResponseEntity.ok(jsonURLS);
    }

    @RequestMapping("/urls/get")
    public ResponseEntity<List<JsonURLWithStatus>> getURLs() {
        return ResponseEntity.ok(URLsMap.asJsonList());
    }

    private void storeURLs(JsonURL jsonURL) {
        logger.info("Received new URL: " + jsonURL.getUrl());
        try {
            URLsMap.addURL(new URL(jsonURL.getUrl()));
            URLsMap.writeURLsToFile();
        } catch (MalformedURLException e) {
            logger.error("Skipping processing of URL: " + jsonURL.getUrl() + ". Reason: Malformed URL.");
        } catch (IOException ioException) {
            logger.error("Failed to write URLs to file.");
        }
    }
}
