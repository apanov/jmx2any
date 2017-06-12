package org.vafer.jmx;

import java.util.Set;

import org.vafer.jmx.pipe.JmxPipe;

public final class Exporter {

    public void output(Server server, Set<String> queries, JmxPipe output) throws Exception {
        output.open();
        JmxQuery query = null;
        try {
            System.out.println("server " + server);
            query = new JmxQuery(
                    String.format("service:jmx:rmi:///jndi/rmi://%s/jmxrmi", server.getHostAndPort()),
                    queries
            );
            for (JmxQuery.JmxBean bean : query) {
                for (JmxQuery.JmxAttribute attribute : bean) {
                    output.output(server.uniqServer(), attribute);
                }
            }
        } finally {
            if (query != null) {
                query.close();
            }
            output.close();
        }
    }

}
