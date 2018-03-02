package com.citrix.util;

public class Memory {
    private long init;
    private long used;
    private long committed;
    private long max;

    private static long MB = 1024 * 1024;

    public long getInit() {
        return init / MB;
    }

    public void addInit(long init) {
        this.init += init;
    }

    public long getUsed() {
        return used / MB;
    }

    public void addUsed(long used) {
        this.used += used;
    }

    public long getCommitted() {
        return committed / MB;
    }

    public void addCommitted(long committed) {
        this.committed += committed;
    }

    public long getMax() {
        return max / MB;
    }

    public void addMax(long max) {
        this.max += max;
    }

    public void reset() {
        this.init = 0;
        this.used = 0;
        this.committed = 0;
        this.max = 0;
    }
}
