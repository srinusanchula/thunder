package com.citrix.util;

import java.util.concurrent.TimeUnit;

public class Delay {
    private final TimeUnit timeUnit;
    private final int delay;

    public Delay(TimeUnit timeUnit, int delay) {
        this.timeUnit = timeUnit;
        this.delay = delay;
    }

    public void delay() {
        try {
            timeUnit.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
