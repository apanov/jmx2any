package org.vafer.jmx;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/*
* java -javaagent:/path/jmx2any.jar=/etc/jmx2any.yml yourmainclass
*/
public final class Agent {

    private final String filename;
    private final ScheduledThreadPoolExecutor executor;

    public Agent(String filename) {
        this.filename = filename;
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.executor.setThreadFactory(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("jmx2any");
                return t;
            }
        });
    }

    public void start() {
        try {
            final Exporter exporter = new Exporter();
            final Exporter.Config config = exporter.load(filename);
            executor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    try {
                        exporter.output(config);
                    } catch (Exception e) {
                        System.err.println("jmx2any: " + e.getMessage());
                    }
                    System.out.println("-");
                    }

            }, config.initialDelay, config.repeatDelay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.err.println("jmx2any: " + e.getMessage());
        }
    }

    public void stop() {
        executor.shutdown();
    }

    public static void premain(String args, Instrumentation inst) {
        System.out.println("Starting jmx2any agent");
        new Agent(args).start();
    }
}
