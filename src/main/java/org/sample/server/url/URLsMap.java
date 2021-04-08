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

/**
 * A class that keeps and manage the list (map) of stored URLs
 */
public class URLsMap {
    private static final Logger logger = LoggerFactory.getLogger(URLsMap.class);

    // In memory map which keeps the URLs sent to the server. If URL already exists it not overwritten.
    private static final ConcurrentHashMap<URL, URLStatus> urlsHashMap = new ConcurrentHashMap<>();

    /**
     * Stores an URL. This method also prepares the URL so it can be handled by the download thread.
     *
     * @param url an absolute URL giving the base location of the image
     */
    public static void addURL(URL url) {
        logger.debug("Adding URL to the map: " + url.toString());
        urlsHashMap.putIfAbsent(url, URLStatus.STORED);
    }

    /**
     * Update URL's map status
     *
     * @param url       an absolute URL which status to be changes
     * @param urlStatus a status to be set for the given URL
     */
    public static void updateEntry(URL url, URLStatus urlStatus) {
        if (urlsHashMap.get(url) != null) {
            logger.debug("Update URL state. URL: " + url.toString() + ", status" + urlStatus);
            urlsHashMap.put(url, urlStatus);
        }
    }

    /**
     * Reads already stored URLs from data file.
     *
     * @throws IOException An error has occurred during the data file reading
     * @throws ClassNotFoundException The class of the stored data is missing
     */
    @SuppressWarnings("unchecked")
    public static synchronized void readURLsFromFile() throws IOException, ClassNotFoundException {
        logger.info("Read URLs file and process its data");

        Object object = Constants.readObjectFromFile(Constants.getUrlsFile());
        if (object != null) {
            urlsHashMap.putAll((Map<? extends URL, ? extends URLStatus>) object);
        }
    }

    /**
     * Persist in memory URLs into a data file.
     *
     * @throws IOException An error has occurred during the data file write
     */
    public static synchronized void writeURLsToFile() throws IOException {
        logger.debug("Write URLs map into a file");

        Constants.writeObjectToFile(urlsHashMap, Constants.getUrlsFile());
    }

    /**
     * Get in memory stored URLs as Json list which contains the URL and the current status of the URL
      *
     * @return a list of URL objects which contains URL and status
     */
    public static List<JsonURLWithStatus> asJsonList() {
        List<JsonURLWithStatus> jsonURLWithStatusList = new ArrayList<>();
        for (Map.Entry<URL, URLStatus> url : urlsHashMap.entrySet()) {
            jsonURLWithStatusList.add(new JsonURLWithStatus(url.getKey().toString(), url.getValue()));
        }
        return jsonURLWithStatusList;
    }

    /**
     * Loop over the URLs list and download URLs that are not already downloaded
     */
    public static void processURLs() {
        for (ConcurrentHashMap.Entry<URL, URLStatus> element : urlsHashMap.entrySet()) {
            if (element.getValue() == URLStatus.STORED) {
                logger.debug("Download " + element.toString());
                downloadURLs(element);
            }
        }
    }

    /**
     * Download URL from specified URL entry
     * @param urlEntry an entry from the map of the URLs
     */
    private static void downloadURLs(ConcurrentHashMap.Entry<URL, URLStatus> urlEntry) {
        Thread t = new DownloadUrlEntryImageThread(urlEntry);
        t.start();
    }

}
