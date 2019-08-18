package pack;

import io.reactivex.Observable;
import logging.LoggingService;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class BufferWindowAndThrottling {

    private static Logger logger = LoggingService.getInstance().getLogger();


    private static void useWindow() {

        var d = Observable.range(1, 50).window(2, 4).
                flatMapSingle(o -> {


                    return o.<String>reduce("", (a, b) -> {
                        return String.valueOf(a) + "|" + String.valueOf(b);
                    });
                })
                .subscribe(numberConcated -> {
                    logger.info("sum is {}", numberConcated);
                });

    }

    private static void useBuffering() throws Exception {
        var y = Observable.interval(1, TimeUnit.SECONDS).publish().autoConnect();

        y.take(10).buffer(4, 5).subscribe(u -> {
            logger.info("u is {}", u);
        });

        /*y.buffer(3, LinkedList::new).map(g -> {
            logger.info("g is {}", g);
            return 1;
        }).subscribe();*/


        Thread.sleep(10000);


    }

    public static void main(String[] args) throws Exception {

        useWindow();
    }

}
