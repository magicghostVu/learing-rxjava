package pack;

import io.reactivex.Scheduler;
import logging.LoggingService;
import org.apache.logging.log4j.Logger;

public class MyStateFullObject {

    private static Logger logger = LoggingService.getInstance().getLogger();

    private int count;


    private Scheduler schedulerToRunOn;

    private String name;


    public MyStateFullObject(Scheduler schedulerToRunOn, String name) {
        this.schedulerToRunOn = schedulerToRunOn;
        this.name = name;
    }

    public int getCount() {
        return count;
    }


    public boolean addCount(int amount, String reason) {
        count += amount;
        logger.info("count increase to {} with reason {} at {}", count, reason, name);
        return true;
    }

    public Scheduler getSchedulerToRunOn() {
        return schedulerToRunOn;
    }
}
