package top.jinhaoplus.http;

public class Proxy {
    private String host;
    private Integer port;
    private String credentialUsername;
    private String credentialPassword;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Proxy(String host, int port, String credentialUsername, String credentialPassword) {
        this.host = host;
        this.port = port;
        this.credentialUsername = credentialUsername;
        this.credentialPassword = credentialPassword;
    }

    public String host() {
        return host;
    }

    public Proxy host(String host) {
        this.host = host;
        return this;
    }

    public Integer port() {
        return port;
    }

    public Proxy port(Integer port) {
        this.port = port;
        return this;
    }

    public String credentialUsername() {
        return credentialUsername;
    }

    public Proxy credentialUsername(String credentialUsername) {
        this.credentialUsername = credentialUsername;
        return this;
    }

    public String credentialPassword() {
        return credentialPassword;
    }

    public Proxy credentialPassword(String credentialPassword) {
        this.credentialPassword = credentialPassword;
        return this;
    }
}
