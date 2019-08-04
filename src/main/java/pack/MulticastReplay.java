package pack;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import logging.LoggingService;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class MulticastReplay implements Runnable {


    private static Logger logger = LoggingService.getInstance().getLogger();


    private static void useThreadLocal() throws Exception {
        var t1 = new MulticastReplay();
        var t2 = new MulticastReplay();

        new Thread(t1).start();
        new Thread(t2).start();


        Thread.sleep(1000);
        logger.info("jj in main is {}", jj.get());
    }

    private static void useReplay() throws Exception {
        var oo = Observable.interval(1, TimeUnit.SECONDS);

        var oo1 = oo.replay();

        oo1.connect();

        oo1.subscribe(l -> {
            logger.info("o1 l is {}", l);
        });


        Thread.sleep(3000);


        oo1.subscribe(l -> {
            logger.info("o2 l is {}", l);
        });


        Thread.sleep(10000);

    }

    private static void useRefCount() throws Exception {
        var b = Observable.interval(1, TimeUnit.SECONDS).share();

        b.take(3).subscribe(l -> {
            logger.info("o1 l is {}", l);
        });


        Thread.sleep(3001);
        b.take(2).subscribe(l1 -> {
            logger.info("o2 l2 is {}", l1);
        });


        logger.info("Done main");
        Thread.sleep(10000);
    }

    private static void tryRange() {
        var ii = Observable.range(1, 10)
                .publish();


        Function<Integer, Integer> ran = i -> ThreadLocalRandom.current().nextInt(100);

        var ii1 = ii.map(ran);
        var ii2 = ii.map(ran);

        ii1.map(ran).subscribe(i -> {
            logger.info("at o1, ii is {}", i);
        });

        ii2.map(ran).subscribe(i -> {
            logger.info("at o2 is is {}", i);
        });


        ii.connect();

        logger.info("Done");
    }


    private static ThreadLocal<Integer> jj = new ThreadLocal<>();


    @Override
    public void run() {
        int y = ThreadLocalRandom.current().nextInt();
        jj.set(y);
        logger.info("y is {} is {}", this, y);
    }

    public static void main(String[] args) throws Exception {
        useReplay();
    }
}
