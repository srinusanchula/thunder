package com.citrix.util;

import org.apache.commons.lang3.time.StopWatch;
import java.util.concurrent.CountDownLatch;

public class TimerLatch extends CountDownLatch {
    private StopWatch stopWatch;

    public TimerLatch(int count) {
        super(count);
        stopWatch = new StopWatch();
    }

    public void start() {
        stopWatch.start();
    }

    public long getStartTimeMillis() {
        return stopWatch.getStartTime();
    }

    public long getElapsedTimeMillis() {
        if(!stopWatch.isStopped()) {
            stopWatch.split();
            return stopWatch.getSplitTime();
        } else {
            return stopWatch.getTime();
        }
    }

    @Override
    public void await() throws InterruptedException {
        super.await();
    }

    public void stop() {
        stopWatch.stop();
    }

    public void suspend() {
        stopWatch.suspend();
    }

    public void resume() {
        stopWatch.resume();
    }
}
