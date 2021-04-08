package org.sample.server.threads;

import org.sample.server.constants.Constants;
import org.sample.server.url.URLStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread that downloads URL from an URL entry
 */
public class DownloadUrlEntryImageThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DownloadUrlEntryImageThread.class);

    ConcurrentHashMap.Entry<URL, URLStatus> urlEntry;

    public DownloadUrlEntryImageThread(ConcurrentHashMap.Entry<URL, URLStatus> urlEntry) {
        this.urlEntry = urlEntry;
    }

    @Override
    public void run() {
        if (urlEntry.getValue() == URLStatus.STORED) {
            URL url = urlEntry.getKey();

            final File file = Constants.getDownloadUrlFile(url);

            // Download the file from the given URL and update the element status according to steps
            try (FileOutputStream fos = new FileOutputStream(file)) {
                urlEntry.setValue(URLStatus.PROCESSING);

                logger.info("Downloading from " + url.toString() + "into file: " + file.toString());

                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                urlEntry.setValue(URLStatus.PROCESSED);
            } catch (IOException e) {
                logger.error("Failed to download file: " + file.toString());
                logger.error(e.getMessage(), e);

                urlEntry.setValue(URLStatus.STORED);
                return;
            }

            // Validate whether the downloaded file is an image. If not - delete the file and update
            // the URL element status
            try {
                logger.debug("Get downloaded file MIME type");
                String fileMimeType = Files.probeContentType(file.toPath());

                logger.debug(fileMimeType + " : " + file.toString());

                if (fileMimeType == null || !fileMimeType.startsWith("image/")) {
                    logger.info("Downloaded file " + file.getName() + " is not an image. Deleting...");
                    file.delete();

                    urlEntry.setValue(URLStatus.REMOVED);
                }
            } catch (IOException ioException) {
                logger.error("Failed to delete file " + file.toString());
                logger.error(ioException.getMessage(), ioException);
            }
        }
    }

}
