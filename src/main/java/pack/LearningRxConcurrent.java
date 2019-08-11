package pack;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import logging.LoggingService;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class LearningRxConcurrent {
    private static Logger logger = LoggingService.getInstance().getLogger();


    static void useSubcribeOn() throws Exception {
        var sc = Schedulers.single();
        var g = Observable.just("phuvh", "vint", "quyvv")
                .subscribeOn(sc)
                .subscribe(str -> {
                    logger.info("str is {}", str);
                });


        //Observable.inon

        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> {
                    logger.info("on map {}", i);
                    return String.valueOf(i);
                })
                .subscribeOn(sc)
                .subscribe(i -> {
                    logger.info("i is {}", i);
                });

        //logger.info("g is {}", g.isDisposed());
        Thread.sleep(5000);
    }

    public static void main(String[] args) throws Exception {
        useSubcribeOn();
    }
}
