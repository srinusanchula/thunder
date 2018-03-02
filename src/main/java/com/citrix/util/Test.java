package com.citrix.util;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Test {
    static RateLimiter rateLimiter = RateLimiter.create(1000);
    public static void main(String[] args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        TimeUnit.MILLISECONDS.sleep(500);

        stopWatch.split();
        System.out.println("Split1: " + stopWatch.getSplitTime());

        stopWatch.stop();
        System.out.println("Stop: " + stopWatch.getTime());

    }
}
