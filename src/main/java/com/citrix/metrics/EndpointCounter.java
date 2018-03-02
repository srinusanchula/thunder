package com.citrix.metrics;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EndpointCounter {
    private final String name;
    private final AtomicInteger invocationCount = new AtomicInteger(0);
    private final AtomicInteger responseCount = new AtomicInteger(0);
    private final Map<Integer, AtomicInteger> responseCodes = Collections.synchronizedMap(new HashMap<>(5));
    private final AtomicLong rtt = new AtomicLong();
    private final List<Long> rttList = Collections.synchronizedList(new ArrayList<>());

    public EndpointCounter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getInvocationCount() {
        return this.invocationCount.get();
    }

    public Map<Integer, AtomicInteger> getResponseCodes() {
        return responseCodes;
    }

    public int getResponseCount() {
        return this.responseCount.get();
    }

    public void incrementInvocationCount() {
        this.invocationCount.incrementAndGet();

    }

    public String getRtt90thAnd99th() {
        Collections.sort(this.rttList);
        int cell90 = (int) Math.floor((double) this.rttList.size() * 0.90);
        int cell99 = (int) Math.ceil((double) this.rttList.size() * 0.99);
        return this.rttList.size() > 0 ? (this.rttList.get(cell90 > 0 ? cell90 - 1 : cell90).toString() + "-" + this.rttList.get(cell99 > 0 ? cell99 - 1 : cell99)) : "";
    }

    public void addToRTT(long timeMillis) {
        this.rtt.addAndGet(timeMillis);
        if (timeMillis != 0L)
            this.rttList.add(timeMillis);
    }

    public void incrementResponseCode(int responseCode) {
        if (!this.responseCodes.containsKey(responseCode)) {
            this.responseCodes.put(responseCode, new AtomicInteger(0));
        }

        this.responseCodes.get(responseCode).incrementAndGet();
    }

    public void incrementResponseCount() {
        this.responseCount.incrementAndGet();
    }

    public long getRttAvg() {
        return (this.rtt.intValue() != 0 && this.responseCount.intValue() != 0) ? this.rtt.intValue() / this.responseCount.intValue() : 1;
    }

    public long getThroughputPerSec(int noOfThreads) {
        long avgRtt = getRttAvg();
        return (noOfThreads * 1000) / (avgRtt != 0 ? avgRtt : 1);
    }

    public long getInvocationPerSec(long elapsedMillis) {
        int invocationCountLocal = invocationCount.get();
        if(elapsedMillis > 1000 && invocationCountLocal > 0)
            return invocationCountLocal / (elapsedMillis / 1000);
        else
            return 0;
    }

    @Override
    public String toString() {
        return "EndpointCounter{" +
                "name='" + name + '\'' +
                ", invocationCount=" + invocationCount +
                ", responseCount=" + responseCount +
                ", responseCodes=" + responseCodes +
                ", rtt=" + rtt +
                ", rttList=" + rttList +
                '}';
    }
}
