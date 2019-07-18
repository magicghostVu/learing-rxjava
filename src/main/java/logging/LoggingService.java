package logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by phuvh on 28/06/2019.
 */
public class LoggingService {


    private Logger logger;

    private Logger bootLogger;

    private Logger socketLogger;

    private static LoggingService ourInstance = new LoggingService();

    public static LoggingService getInstance() {
        return ourInstance;
    }

    private LoggingService() {
        logger = LogManager.getLogger("RollingFileLog");
        bootLogger = LogManager.getLogger("bootLogger");
        socketLogger=LogManager.getLogger("socketLogger");
    }

    public Logger getLogger() {
        return logger;
    }

    public Logger getBootLogger() {
        return bootLogger;
    }

    public Logger getSocketLogger() {
        return socketLogger;
    }
}
