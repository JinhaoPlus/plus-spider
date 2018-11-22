package top.jinhaoplus.http;

public class RequestHeader {

    private String name;
    private String value;

    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public RequestHeader name(String name) {
        this.name = name;
        return this;
    }

    public String value() {
        return value;
    }

    public RequestHeader value(String value) {
        this.value = value;
        return this;
    }
}
