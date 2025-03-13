package util;

import java.io.IOException;
import java.util.logging.*;

public abstract class BaseLogger  {
        //I don't use static so that each subclass can configure its own logging level.
        protected final Logger logger;

        //constructor
        public BaseLogger() {
        /** The information from different classes will be logged to the different files
        logger = Logger.getLogger(this.getClass().getName());*/

            // Logger initialization - the information from different classes will be logged to the same file
            logger = Logger.getLogger("webshop");

        /**ConsoleHandler which directs the log messages to the console (standard output).
         * Without this handler, the logger might not display anything on the console or might only print messages of higher levels (like WARNING or SEVERE).
         **/
            // Avoid adding duplicate handlers
            if (logger.getHandlers().length == 0) {
                try {
                    // Console handler
                    ConsoleHandler consoleHandler = new ConsoleHandler();
                    consoleHandler.setLevel(Level.FINE);// Set the minimum level of log messages that the handler will process

                    // File handler in resources folder
                    FileHandler fileHandler = new FileHandler("resources/webshop.log", false);
                    fileHandler.setLevel(Level.FINE);
                    fileHandler.setFormatter(new SimpleFormatter());

                    // Logger settings
                    logger.setLevel(Level.FINE);//Set the level of the logger itself, if the logger has a higher level (like INFO), messages with a lower level (FINE, DEBUG) won’t be logged
                    logger.setUseParentHandlers(false);//Without this line, log messages may be duplicated or handled by multiple handlers, causing cluttered output.
                    logger.addHandler(consoleHandler);// Add the handler to the logger. Without this step, the logger would not know where to send the log messages.*/
                    logger.addHandler(fileHandler);

                    // Close handlers on shutdown
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        for (Handler handler : logger.getHandlers()) {
                            handler.close();
                        }
                    }));

                } catch (IOException e) {
                    System.err.println("Error setting up file handler: " + e.getMessage());
                }
            }
    }
}
