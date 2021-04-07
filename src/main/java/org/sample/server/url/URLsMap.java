package org.sample.server.url;

import org.sample.server.constants.Constants;
import org.sample.server.json.JsonURLWithStatus;
import org.sample.server.threads.DownloadUrlEntryImageThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URLsMap {
    private static final Logger logger = LoggerFactory.getLogger(URLsMap.class);

    private static final ConcurrentHashMap<URL, URLStatus> urlsHashMap = new ConcurrentHashMap<>();

    public static void addURL(URL url) {
        logger.debug("Adding URL to the map: " + url.toString());
        urlsHashMap.putIfAbsent(url, URLStatus.STORED);
    }

    public static void updateEntry(URL url, URLStatus urlStatus) {
        if (urlsHashMap.get(url) != null) {
            logger.debug("Update URL state. URL: " + url.toString() + ", status" + urlStatus);
            urlsHashMap.put(url, urlStatus);
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized void readURLsFromFile() throws IOException, ClassNotFoundException {
        logger.info("Read URLs file and process its data");

        Object object = Constants.readObjectFromFile(Constants.getUrlsFile());
        if (object != null) {
            urlsHashMap.putAll((Map<? extends URL, ? extends URLStatus>) object);
        }
    }

    public static synchronized void writeURLsToFile() throws IOException {
        logger.debug("Write URLs map into a file");

        Constants.writeObjectToFile(urlsHashMap, Constants.getUrlsFile());
    }

    public static List<JsonURLWithStatus> asJsonList() {
        List<JsonURLWithStatus> jsonURLWithStatusList = new ArrayList<>();
        for (Map.Entry<URL, URLStatus> url : urlsHashMap.entrySet()) {
            jsonURLWithStatusList.add(new JsonURLWithStatus(url.getKey().toString(), url.getValue()));
        }
        return jsonURLWithStatusList;
    }

    public static void processURLs() {
        for (ConcurrentHashMap.Entry<URL, URLStatus> element : urlsHashMap.entrySet()) {
            if (element.getValue() == URLStatus.STORED) {
                logger.debug("Download " + element.toString());
                downloadURLs(element);
            }
        }
    }

    private static void downloadURLs(ConcurrentHashMap.Entry<URL, URLStatus> urlEntry) {
        Thread t = new DownloadUrlEntryImageThread(urlEntry);
        t.start();
    }

}
