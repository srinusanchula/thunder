package com.citrix.metrics;

import com.citrix.core.StressTester;
import com.citrix.util.Memory;
import com.citrix.util.MemoryUtil;
import com.citrix.util.TableFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EndpointRegistry {
    private static final Map<String, EndpointCounter> registry = new LinkedHashMap<>(10);
    public static final AtomicInteger activeUsers = new AtomicInteger(0);

    public static EndpointCounter registerEndpoint(String name) {
        if (!registry.containsKey(name))
            registry.put(name, new EndpointCounter(name));

        return registry.get(name);
    }

    public static TableFormat toTableFormat(StressTester stressTester) {
        long elapsed = stressTester.getElapseTimeMillis();
        StringBuilder banner = prepareBanner(stressTester, elapsed);

        int columnCount = 7;
        List<String> headers = new ArrayList<>(columnCount);
        headers.add("Name");
        headers.add("Requests (/s)");
        headers.add("Responses");
        headers.add("Status");
        headers.add("RTT (AVG)");
        headers.add("RTT (90th-99th)");
        headers.add("Throughput (/s)");

        ArrayList<List<String>> content = new ArrayList<>();

        for (Map.Entry<String, EndpointCounter> entry : registry.entrySet()) {
            EndpointCounter endpointCounter = entry.getValue();

            List<String> row = new ArrayList<>(columnCount);
            row.add(endpointCounter.getName());
            row.add(endpointCounter.getInvocationCount() + "(" + endpointCounter.getInvocationPerSec(elapsed) + ")");
            row.add(endpointCounter.getResponseCount() + "");
            row.add(endpointCounter.getResponseCodes().toString());
            row.add(endpointCounter.getRttAvg() + "");
            row.add(endpointCounter.getRtt90thAnd99th());
            row.add(endpointCounter.getThroughputPerSec(stressTester.getUsers()) + "");

            content.add(row);
        }

        return new TableFormat(banner, headers, content);
    }

    private static StringBuilder prepareBanner(StressTester stressTester, long elapsed) {
        StringBuilder sb = new StringBuilder();

        sb.append('\n');
        sb.append("Active: ").append(activeUsers.getAndSet(0));
        sb.append('\t');
        sb.append("Elapsed: ").append(TimeUnit.SECONDS.convert(elapsed, TimeUnit.MILLISECONDS));
        sb.append('\t');
        sb.append("Thread (A/T): ").append(stressTester.getActiveThreads()).append('/').append(stressTester.getTotalThreads());
        sb.append('\t');

        Memory memory = MemoryUtil.getTotalMemory();

        // Init/Used/Committed/Max
        sb.append("Mem (I/U/C/M): ");
        sb.append(memory.getInit()).append('/').append(memory.getUsed());
        sb.append('/').append(memory.getCommitted()).append('/').append(memory.getMax());
        sb.append('\n');
        sb.append("Conf[");
        sb.append("Users: ").append(stressTester.getUsers()).append("; ");
        sb.append("Iterations: ").append(stressTester.getIterations()).append("; ");
        sb.append("Rate: ").append(stressTester.getRate()).append("; ");
        sb.append("Spread: ").append(stressTester.getSpread());
        sb.append("]");

        sb.append('\n');

        return sb;
    }
}
