package org.sample.server.url;

public enum URLStatus {
    STORED, // URL is stored and waiting for download
    PROCESSING, // URL is already downloading
    PROCESSED, // URL is processed and the data is downloaded
    REMOVED // Not an image - remove this file
}
