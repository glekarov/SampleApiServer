package org.sample.server.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class Constants {
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    // Directories
    private static final String DATA_DIR_NAME = "data";
    private static final String DOWNLOAD_DIR_NAME = "download";

    // File names
    private static final String URLS_FILE_NAME = "URLS.dat";
    private static final String USERS_FILE_NAME = "Users.dat";

    private static final File URLS_FILE = new File(DATA_DIR_NAME + File.separator + URLS_FILE_NAME);
    private static final File USERS_FILE = new File(DATA_DIR_NAME + File.separator + USERS_FILE_NAME);
    private static final File DOWNLOAD_DIR_PATH = new File(DATA_DIR_NAME + File.separator + DOWNLOAD_DIR_NAME);

    public static File getUrlsFile() {
        return URLS_FILE;
    }

    public static File getUsersFile() {
        return USERS_FILE;
    }

    public static File getDownloadDirPath() {
        return DOWNLOAD_DIR_PATH;
    }

    public static void createDataFilesIfNotExist() throws IOException {
        logger.debug("Create download directory path if not exists");
        DOWNLOAD_DIR_PATH.mkdirs();

        if (!getUrlsFile().exists()) {
            logger.debug("Create URLs data file");
            FileWriter fileWriter = new FileWriter(getUrlsFile());
        }

        if (!getUsersFile().exists()) {
            logger.debug("Create Users data file");
            FileWriter fileWriter = new FileWriter(getUsersFile());
        }
    }

    public static Object readObjectFromFile(final File file) throws IOException, ClassNotFoundException {
        logger.debug("Read Object file and process its data");

        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            return objectInputStream.readObject();
        } catch (EOFException e) {
            logger.debug("URLs data file is empty... continue.");
        }

        return null;
    }

    public static void writeObjectToFile(Object object, final File file) throws IOException {
        logger.debug("Write URLs map into a file");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(object);
        }
    }

    public static File getDownloadUrlFile(URL url) {
        return new File(getDownloadDirPath() + File.separator + getFileName(url));
    }

    private static synchronized String getFileName(URL url) {
        String urlPath = url.getPath();
        return urlPath.substring(urlPath.lastIndexOf('/') + 1);
    }

}
