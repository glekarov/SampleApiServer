package org.sample.server.url;

/**
 * Status of already stored URLs
 */
public enum URLStatus {
    /**
     * URL is stored and waiting for download
     */
    STORED,
    /**
     * URL is already downloading
     */
    PROCESSING,
    /**
     * URL is processed and the data is downloaded
     */
    PROCESSED,
    /**
     * Downloaded URL is not an image and the file was deleted
     */
    REMOVED
}
