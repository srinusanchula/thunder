package com.citrix.core;

import com.citrix.config.ThunderProperties;
import com.citrix.data.Input;
import com.citrix.util.TimerLatch;
import com.google.common.util.concurrent.RateLimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static com.google.common.util.concurrent.RateLimiter.create;
import static java.util.stream.IntStream.range;

public class LoopStressTester extends StressTester {
    private ThreadPoolExecutor executor;
    private TimerLatch timerLatch;
    private List<Runnable> requestList;

    public LoopStressTester(ThunderProperties tp, List<Input> inputList, Scenario.Builder builder) throws Exception {
        super(tp, inputList, builder);

        int rate = (int) determineRate();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(rate > 0 ? rate : 1000);
        requestList = new ArrayList<>(tp.getUsers());
        timerLatch = new TimerLatch(tp.getUsers() * tp.getIterations());
    }

    @Override
    public int getActiveThreads() {
        return executor.getActiveCount();
    }

    @Override
    public int getTotalThreads() {
        return executor.getPoolSize();
    }

    @Override
    public long getElapseTimeMillis() {
        return timerLatch.getElapsedTimeMillis();
    }

    @Override
    public void stress() throws InterruptedException {
        //Prepare the test
        prepareThreads(builder, timerLatch);

        timerLatch.start();
        metricsWriter.start();

        spawnThreads();

        timerLatch.await();
        timerLatch.stop();
        executor.shutdown();

        metricsWriter.stop();
    }

    private void prepareThreads(Scenario.Builder builder, TimerLatch timerLatch) {
        range(0, tp.getUsers()).forEach(uCount -> {
            Scenario scenario = builder.build(inputList.get(uCount));
            requestList.add(() -> {
                scenario.execute();
                timerLatch.countDown();
            });
        });
    }

    private void spawnThreads() {
        new Thread(() -> {
            RateLimiter rateLimiter = buildRateLimiter();
            range(0, tp.getIterations())
                    .forEach(iCount -> range(0, requestList.size())
                            .forEach(uCount -> {
                                executor.execute(requestList.get(uCount));

                                if (rateLimiter != null)
                                    rateLimiter.acquire();
                            }));
        }).start();
    }

    private RateLimiter buildRateLimiter() {
        double rate = determineRate();
        if (rate > 0)
            return create(rate);
        else
            return null;
    }

    private double determineRate() {
        if (tp.getRate() > 0)
            return getRate();
        else if (tp.getSpread() > 0)
            return (double) (tp.getUsers() * tp.getIterations()) / tp.getSpread();
        else
            return 0;
    }
}
