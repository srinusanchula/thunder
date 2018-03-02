package com.citrix.metrics;

import com.citrix.core.StressTester;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class MetricsWriter {
    private final StressTester stressTester;
    private final PrintStream printStream;
    private final int sleepDuration;
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Thread localThread;

    public MetricsWriter(
            StressTester stressTester,
            PrintStream printStream,
            int sleepDuration) {
        this.stressTester = stressTester;
        this.printStream = printStream;
        this.sleepDuration = sleepDuration;
    }

    public void start() {
        this.localThread = new Thread(() -> {
            boolean interrupted = false;

            while (!interrupted) {
                print();

                try {
                    TimeUnit.SECONDS.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        });

        this.localThread.start();
    }

    public void stop() {
        this.localThread.interrupt();
        printFinalMetrics();
    }

    private void printFinalMetrics() {
        //Graceful shutdown
        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        print();
    }

    private void print() {
        printStream.print(EndpointRegistry.toTableFormat(stressTester));
    }
}
