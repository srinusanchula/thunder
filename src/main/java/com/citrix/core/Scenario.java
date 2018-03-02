package com.citrix.core;

import com.citrix.data.Context;
import com.citrix.data.Input;
import com.citrix.metrics.EndpointRegistry;

import java.util.concurrent.TimeUnit;

public abstract class Scenario {
    private final Input input;
    private final Context context;
    private final Request[] requests;

    protected Scenario(Input input) {
        this.input = input;
        this.context = new Context();

        requests = init();
    }

    public abstract String getName();

    protected abstract Request[] init();

    public void warmup() {
        for (Request request : requests) {
            request.warmup(context, input);
        }
    }

    public void execute() {
        EndpointRegistry.activeUsers.incrementAndGet();
        for (Request request : requests) {
            request.fire(context, input);
        }
//        EndpointRegistry.activeUsers.decrementAndGet();
    }

    public interface Builder {
        Scenario build(Input input);
    }
}
