package org.sample.server;

import org.sample.server.auth.UsersList;
import org.sample.server.constants.Constants;
import org.sample.server.threads.ProcessURLsTimerThread;
import org.sample.server.url.URLsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MainServerClass {
    private static final Logger logger = LoggerFactory.getLogger(MainServerClass.class);

    public static void main(String[] args) {
        try {
            Constants.createDataFilesIfNotExist();

            URLsMap.readURLsFromFile();
            UsersList.readUsersFromFile();

            Thread timerTask = new ProcessURLsTimerThread();
            timerTask.start();

            SpringApplication.run(MainServerClass.class, args);
        } catch (IOException ioException) {
            logger.error("Can not read URLs data file! ", ioException);
        } catch (ClassNotFoundException e) {
            logger.error("Data file doesn't contains URL objects!", e);
        }
    }
}
