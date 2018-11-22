package top.jinhaoplus.http;

import java.util.Date;

public class RequestCookie {

    private String name;
    private String value;
    private Date expiryDate;
    private String domain;
    private String path;

    public String name() {
        return name;
    }

    public RequestCookie name(String name) {
        this.name = name;
        return this;
    }

    public String value() {
        return value;
    }

    public RequestCookie value(String value) {
        this.value = value;
        return this;
    }

    public Date expiryDate() {
        return expiryDate;
    }

    public RequestCookie expiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public String domain() {
        return domain;
    }

    public RequestCookie domain(String domain) {
        this.domain = domain;
        return this;
    }

    public String path() {
        return path;
    }

    public RequestCookie path(String path) {
        this.path = path;
        return this;
    }
}
