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

        //khi sử dụng interval nếu muốn nó chạy trên một schedule chỉ định thì phải thêm tham số vào cuối
        Observable.interval(1000, TimeUnit.MILLISECONDS, sc)
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


    private static void useObserverOn() throws InterruptedException {

        var sc1 = Schedulers.newThread();
        var sc2 = Schedulers.single();

        var y = Observable.interval(1000, TimeUnit.MILLISECONDS, sc1)
                .map(i -> {

                    logger.info("map run i is {}", i);

                    return i;
                }).take(10)
                .observeOn(sc2)
                .reduce(0L, Long::sum)
                .subscribe(r -> {
                    logger.info("result on {}", r);
                });

        logger.info("y is {}", y);

        Thread.sleep(10000);
    }


    public static void main(String[] args) throws Exception {
        useObserverOn();


    }
}
