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

    /**
     * Path to the URLs file
     * @return a File with the path to the URLs data file
     */
    public static File getUrlsFile() {
        return URLS_FILE;
    }

    /**
     * Path to the Users file
     * @return a File with the path to the Users data file
     */
    public static File getUsersFile() {
        return USERS_FILE;
    }

    /**
     * Path to the 'download' directory
     * @return a File with the path to the directory where the files will be downloaded
     */
    public static File getDownloadDirPath() {
        return DOWNLOAD_DIR_PATH;
    }

    /**
     * Use this only in the startup to create all directories and files structure.
     * If the directory and files already exist this method does nothing. It creates
     * all the necessary file structure during the very first start of the server.
     *
     * @throws IOException an error occurred during the directory or file creation.
     */
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

    /**
     * Reads already stored URLs from data file.
     *
     * @param file the name of the file to be read
     * @return an Object with the data structure stored into the file
     * @throws IOException An error has occurred during the data file reading
     * @throws ClassNotFoundException The class of the stored data is missing
     */
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

    /**
     * Write an object into the specified file.
     *
     * @param object object to be stored in file
     * @param file the File where the object will be stored
     * @throws IOException An error has occurred during the data file writing
     */
    public static void writeObjectToFile(Object object, final File file) throws IOException {
        logger.debug("Write URLs map into a file");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(object);
        }
    }

    /**
     * From the given URL gets the path where the file in the URL will be stored
     *
     * @param url an absolute URL giving the base location of the file
     * @return a File containing the file name and the path where the fill will be stored
     */
    public static File getDownloadUrlFile(URL url) {
        return new File(getDownloadDirPath() + File.separator + getFileName(url));
    }

    /**
     * Extract the file name from a given URL
     * @param url an absolute URL giving the base location of the file
     * @return the name of the file
     */
    private static synchronized String getFileName(URL url) {
        String urlPath = url.getPath();
        return urlPath.substring(urlPath.lastIndexOf('/') + 1);
    }

}
