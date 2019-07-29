package pack;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import logging.LoggingService;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Main {


    private static Logger logger = LoggingService.getInstance().getLogger();


    public static void useDefer() {

        int start = 0;
        MutableInt v = new MutableInt(10);

        var b = Observable.defer(() -> {
            return Observable.range(start, v.getValue());
        });


        var g = b.subscribe(i -> {
            logger.info("o1 i is {}", i);
        });

        v.setValue(20);
        var g2 = b.subscribe(ii -> {
            logger.info("o2 i is {}", ii);
        });


    }

    static void tryConnectableObservable() {
        var coldO = Observable.<String>just("phuvh", "quyvv", "vint", "haonc");

        var hotO = coldO.publish();

        hotO.subscribe(v -> {
            logger.info("v is {}", v);
        });


        hotO.map(String::length).subscribe(l -> {
            logger.info("l is {}", l);
        });

        var t = hotO.connect();

        logger.info("t is {}", t);

    }

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

        o.subscribe(index -> {
            LoggingService.getInstance().getLogger().info("epoch is {}", index);
        });
    }

    public static void main(String[] args) {
        useDefer();
    }
}
