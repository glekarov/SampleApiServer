package org.sample.server.threads;

import org.sample.server.url.URLsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A timer thread that loops over the stored URLs and download the ones that wasn't already downloaded
 */
public class ProcessURLsTimerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ProcessURLsTimerThread.class);

    @Override
    public void run() {
        logger.info("Running timer task");

        try {
            while (true) {
                Thread.sleep(10000);
                // Persist in memory object to file
                URLsMap.writeURLsToFile();

                // Check for newly added URLs and download the files
                URLsMap.processURLs();
            }
        } catch (InterruptedException e) {
            logger.error("InterruptedException has been caught The thread is going " +
                    "to shutdown: ", e);
        } catch (IOException ioException) {
            logger.error("Failed to write URLs to file: ", ioException);
        }
    }
}
