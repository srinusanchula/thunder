package com.citrix.core;

import com.citrix.config.ThunderProperties;
import com.citrix.data.Input;
import com.citrix.util.Delay;
import com.citrix.util.TimerLatch;
import com.google.common.util.concurrent.RateLimiter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InstantStressTester extends StressTester {
    private ThreadPoolExecutor executor;
    private TimerLatch timerLatch;

    public InstantStressTester(ThunderProperties tp, List<Input> inputList, Scenario.Builder builder) throws Exception {
        super(tp, inputList, builder);

        timerLatch = new TimerLatch(tp.getUsers());
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(tp.getUsers());
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
        timerLatch.start();
        metricsWriter.start();

        spawnThreads();
        timerLatch.await();
        timerLatch.stop();
        executor.shutdown();

        metricsWriter.stop();
    }

    private void spawnThreads() {
        for (int i = 0; i < tp.getUsers(); i++) {
            final int index = i;
            executor.execute(() -> {
                repeat(builder.build(inputList.get(index)), buildRateLimiter());
                timerLatch.countDown();
            });
        }
    }

    private void repeat(Scenario sequence, RateLimiter rateLimiter) {
//        sequence.warmup();

        for (int i = 0; i < tp.getIterations(); i++) {
            if (rateLimiter != null)
                rateLimiter.acquire();

            sequence.execute();
        }
    }

    private RateLimiter buildRateLimiter() {
        if(tp.getRate() > 0)
            return RateLimiter.create((double) tp.getRate() / tp.getUsers());
        else if(tp.getSpread() > 0)
            return RateLimiter.create((double) tp.getIterations() / tp.getSpread());
        else
            return null;
    }

}
