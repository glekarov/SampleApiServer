package org.sample.server.threads;

import org.sample.server.url.URLsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProcessURLsTimerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ProcessURLsTimerThread.class);

    @Override
    public void run() {
        logger.info("Running timer task");

        try {
            while (true) {
                Thread.sleep(10000);
                URLsMap.writeURLsToFile();
                URLsMap.processURLs();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            logger.error("Failed to write URLs to file: ", ioException);
        }
    }
}
