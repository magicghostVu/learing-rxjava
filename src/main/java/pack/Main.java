package pack;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import logging.LoggingService;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Main {


    private static Logger logger = LoggingService.getInstance().getLogger();

    static Disposable createFromEmmiter() {

        //nhớ rằng khi chưa subscribe thì sẽ không chạy create và emmiter chưa có emmit ra event nào cả
        // map, filter cũng như vậy -> lazy call


        var oo = Observable.<String>create(emmiter -> {
            emmiter.onNext("phuvh");

            logger.info("done emmit phuvh");

            emmiter.onNext("vint");
            emmiter.onNext("quyvv");

            emmiter.onNext("unexpected");
            //e.onError(new Exception("exception"));
            emmiter.onComplete();

            logger.info("done emmit");
        });


        var i1 = oo.map(str -> {
            logger.info("map to length {}", str);
            return str.length();
        });

        var i2 = i1.filter(l -> l > 1);


        return i2.subscribe(w -> {
            logger.info("leght is {}", w);
        }, throwable -> {
            logger.error("err while emit somethings", throwable);
        });

    }

    private static void doStuff1() {
        var t = 0;
        System.out.println("okok");
        LoggingService.getInstance().getLogger().info("Done");
        var o = Observable.interval(5L, TimeUnit.SECONDS);

        o.subscribe(epoch -> {
            LoggingService.getInstance().getLogger().info("epoch is {}", epoch);
        });
    }

    public static void main(String[] args) {
        var y = createFromEmmiter();

        logger.info("y is {}", y);

    }
}
