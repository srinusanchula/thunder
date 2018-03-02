package com.citrix.core;

import com.citrix.config.ThunderProperties;
import com.citrix.data.Input;
import com.citrix.metrics.MetricsWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintStream;
import java.util.List;

public abstract class StressTester {
    ThunderProperties tp;
    final List<Input> inputList;
    final Scenario.Builder builder;
    MetricsWriter metricsWriter;

    StressTester(ThunderProperties tp, List<Input> inputList, Scenario.Builder builder) throws Exception {
        this.tp = tp;
        this.inputList = inputList;
        this.builder = builder;

        if (getUsers() > inputList.size())
            throw new Exception("Insufficient input data.");
    }

    public abstract int getActiveThreads();

    public abstract int getTotalThreads();

    public int getUsers() {
        return tp.getUsers();
    }

    public int getIterations() {
        return tp.getIterations();
    }

    public abstract long getElapseTimeMillis();

    public int getRate() {
        return tp.getRate();
    }

    public long getSpread() {
        return tp.getSpread();
    }

    public abstract void stress() throws InterruptedException;

    public void writeMetricsTo(PrintStream printStream) {
        this.metricsWriter = new MetricsWriter(this, printStream, 1);
    }
}
