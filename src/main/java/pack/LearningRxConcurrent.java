package pack;

import logging.LoggingService;
import org.apache.logging.log4j.Logger;

public class LearningRxConcurrent {
    private static Logger logger = LoggingService.getInstance().getLogger();

    public static void main(String[] args) {
        logger.info("Main run");



        Runnable r= ()->{
          logger.info("Run");
        };

        new Thread(r).start();
        new Thread(r).start();



    }
}
