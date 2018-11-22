package top.jinhaoplus.http;


import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class Request {
    private RequestMethod method = RequestMethod.GET;
    private String url;
    private Map<String, Object> meta;
    private List<RequestCookie> cookies = Lists.newArrayList();
    private List<RequestHeader> headers = Lists.newArrayList();
    private String entity;

    public Request(String url) {
        this.url = url;
    }

    public Request(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
    }

    public RequestMethod method() {
        return method;
    }

    public Request method(RequestMethod method) {
        this.method = method;
        return this;
    }

    public String url() {
        return url;
    }

    public Request url(String url) {
        this.url = url;
        return this;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public Request meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public List<RequestCookie> cookies() {
        return cookies;
    }

    public Request addCookie(RequestCookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public Request cookies(List<RequestCookie> cookies) {
        this.cookies = cookies;
        return this;
    }

    public List<RequestHeader> headers() {
        return headers;
    }

    public Request addHeader(RequestHeader header) {
        this.headers.add(header);
        return this;
    }

    public Request headers(List<RequestHeader> headers) {
        this.headers = headers;
        return this;
    }

    public String entity() {
        return entity;
    }

    public Request entity(String entity) {
        this.entity = entity;
        return this;
    }
}
