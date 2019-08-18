package pack;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import logging.LoggingService;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class LearningRxConcurrent {
    private static Logger logger = LoggingService.getInstance().getLogger();


    private static void testEventLoopPattern() throws Exception {


        Executor executor = Executors.newSingleThreadExecutor();
        Executor executor2 = Executors.newSingleThreadExecutor();


        var sc1 = Schedulers.from(executor);

        var sc2 = Schedulers.from(executor2);

        var m = new MyStateFullObject(sc1, "o1");

        var m2 = new MyStateFullObject(sc2, "o2");


        var oInterval10 = Observable.interval(1, TimeUnit.SECONDS).publish().autoConnect().take(10);


        var d1 = oInterval10.flatMap(i -> {
            return Observable.just(m).observeOn(m.getSchedulerToRunOn()).map(stateFullObject -> {
                stateFullObject.addCount(1, "interval " + i);
                return 1;
            });
        }).subscribe();



        var d2 = oInterval10.flatMap(i -> {
            return Observable.just(m2).observeOn(m2.getSchedulerToRunOn()).map(stateFullObject -> {
                stateFullObject.addCount(1, "interval " + i);
                return 1;
            });
        }).subscribe();


        Thread.sleep(15000);


        logger.info("Disposed d1 {}, d2 {}", d1.isDisposed(), d2.isDisposed());
    }


    private static void useParallel() throws Exception {

        //var executor = new ThreadPoolExecutor(8, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());


        //var y = new CompletableFuture<String>();


        var r = Schedulers.io();
        var o1 = Observable.just("aaa", "phuvh", "vint", "haonc", "quyvv", "sss");

        final int numGroup = 3;

        var index = new MutableInt();

        var d1 = o1.observeOn(Schedulers.single()).map(str -> {
            logger.info("map on");
            return new MutablePair<>(index.getAndIncrement() % numGroup, str);
        }).groupBy(MutablePair::getLeft).flatMap(gpr -> {
            return gpr.observeOn(r).map(p -> {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    logger.info("err while sleep");
                }

                logger.info("map group on {}", gpr.getKey());
                return p.right;
            });
        }).unsubscribeOn(Schedulers.single()).doOnDispose(() -> {
            logger.info("d1 disposed");
        }).doOnComplete(() -> {
            logger.info("complete d1");
        }).subscribe(str -> {
            logger.info("str is {}", str);
        });


        Thread.sleep(10000);

        //d1.dispose();
        logger.info("d1 disposed {}", d1.isDisposed());

    }

    private static void useThreadPool() throws Exception {


        var executor = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());


        var r = Schedulers.from(executor);

        var o1 = Observable.just("aaa", "phuvh", "vint", "haonc", "quyvv", "sss");

        //o1


        o1.flatMap(str -> {
            //logger.info("flat map {}", str);
            return Observable.just(str).
                    subscribeOn(r);
                    /*.map(s->{
                        try {
                            Thread.sleep(200);
                        }catch (Exception e){
                            logger.info("err while sleep");
                        }
                        logger.info("map on at {}", str);


                        return s.length();
                    });*/
        }).subscribe(s -> {

            String msg = String.format("thread is %s, s is %s", Thread.currentThread().getName(), s);

            System.out.println(msg);
            //logger.info("final s is {}", s);
        });


        Thread.sleep(10000);
        //o1.flatMapSingle()
        //JavaFxScheduler.platform().
        //Observable.fro

    }

    static void useMultiThreadAndReduceOnOne() throws Exception {

        var sc1 = Schedulers.newThread();

        var sc2 = Schedulers.newThread();

        var o1 = Observable.just("aaa", "phuvh", "vint", "haonc", "quyvv", "sss").subscribeOn(sc1);

        var o2 = Observable.just("congnn", "linhntm2", "duongdv", "khangvt", "tuanmn3", "thanhtt").subscribeOn(sc2);


        var o3 = o1.map(str -> {
            logger.info("o1 map");


            return Pair.of(str, str.length());

            //return str.length();
        });

        var o4 = o2.map(str -> {
            logger.info("o2 map");
            return Pair.<String, Integer>of(str, str.length());
        });


        var d1 = o3.mergeWith(o4).observeOn(Schedulers.newThread()).subscribe(v -> {
            logger.info("v is {}", v);
        });

        //Observable.merge(o1, o2).

        /*var v3 = o1.mergeWith(o2)
                .map(str -> {
                    logger.info(" map run on str is {}", str);
                    return str.length();
                })
                .reduce(0, (a, b) -> {
                    //logger.info("reduce on");
                    return a + b;
                })
                .subscribe(i -> {
                    logger.info("sum is {}", i);
                });*/


        Thread.sleep(5000);
    }

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
        testEventLoopPattern();


    }
}
