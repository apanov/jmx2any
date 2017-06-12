package org.vafer.jmx;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Set;

import org.vafer.jmx.output.ConsoleOutput;
import org.vafer.jmx.pipe.ConverterPipe;
import org.vafer.jmx.pipe.JmxPipe;

/*
* java -javaagent:/path/jmx2any.jar=/etc/jmx2any.yml yourmainclass
*/
public final class Agent {

    private final String filename;
    private final JmxPipe output;

    public Agent(String filename, JmxPipe output) {
        this.filename = filename;
        this.output = output;
    }

    public void start() {
        try {
            final Config config = Config.load(filename);
            for (Map.Entry<Server, Set<String>> sq: config.queries.entrySet()) {
                if (sq.getValue() == null || sq.getValue().isEmpty()) {
                    throw new IllegalArgumentException("Query can't be null or empty, " + sq.getKey());
                }
                Worker worker = new Worker(sq.getKey(), sq.getValue(), output, config.repeatDelay);
                worker.start();
            }
        } catch (Exception e) {
            System.err.println("jmx2any: " + e);
        }
    }

    public static void premain(String args, Instrumentation inst) {
        System.out.println("Starting jmx2any agent");
        new Agent(args, new ConverterPipe(new ConsoleOutput(), "")).start();
    }
}
