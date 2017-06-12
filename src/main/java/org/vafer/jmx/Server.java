package org.vafer.jmx;

public class Server {

    /**
     * service host and port
     */
    private final String hostAndPort;

    /**
     * service process id
     */
    private Integer pid;

    /**
     * file descriptor of container network namespace
     */
    private Integer nsFd;

    public Server(String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    public String getHostAndPort() {
        return hostAndPort;
    }

    public Integer getPid() {
        return pid;
    }

    public Server setPid(Integer pid) {
        this.pid = pid;
        return this;
    }

    public Integer getNsFd() {
        return nsFd;
    }

    public Server setNsFd(Integer nsFd) {
        this.nsFd = nsFd;
        return this;
    }

    /**
     * @return host and port with particular linux namespace
     */
    public String uniqServer() {
        return (pid != null ? pid + "/" : "") + (nsFd != null ? nsFd + "/" : "") + hostAndPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Server server = (Server) o;

        if (!hostAndPort.equals(server.hostAndPort))
            return false;
        return pid != null ? pid.equals(server.pid) : server.pid == null;
    }

    @Override
    public int hashCode() {
        int result = hostAndPort.hashCode();
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Server{" +
                "hostAndPort='" + hostAndPort + '\'' +
                ", pid=" + pid +
                ", nsFd=" + nsFd +
                '}';
    }
}
