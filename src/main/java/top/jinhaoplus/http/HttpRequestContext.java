package top.jinhaoplus.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;

public class HttpRequestContext {
    private HttpUriRequest httpRequest;
    private HttpClientContext context;

    public HttpRequestContext(HttpUriRequest httpRequest, HttpClientContext context) {
        this.httpRequest = httpRequest;
        this.context = context;
    }

    public HttpUriRequest httpRequest() {
        return httpRequest;
    }

    public HttpClientContext context() {
        return context;
    }
}
