package top.jinhaoplus.http;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class Response {
    private int statusCode;
    private ResponseStatus responseStatus = ResponseStatus.SUCCESS;
    private String resultText;
    private Request request;
    private List<Cookie> cookies;
    private List<Header> headers;

    public Response(ResponseStatus responseStatus, Request request) {
        this.responseStatus = responseStatus;
        this.request = request;
    }

    public Response(int statusCode, Request request) {
        this.statusCode = statusCode;
        this.request = request;
    }

    public Response(int statusCode, ResponseStatus responseStatus, Request request) {
        this.statusCode = statusCode;
        this.responseStatus = responseStatus;
        this.request = request;
    }

    public Response(int statusCode, String resultText, Request request) {
        this.statusCode = statusCode;
        this.resultText = resultText;
        this.request = request;
    }

    public int statusCode() {
        return statusCode;
    }

    public Response statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ResponseStatus responseStatus() {
        return responseStatus;
    }

    public Response responseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    public String resultText() {
        return resultText;
    }

    public Response resultText(String resultText) {
        this.resultText = resultText;
        return this;
    }

    public Request request() {
        return request;
    }

    public Response request(Request request) {
        this.request = request;
        return this;
    }

    public List<Cookie> cookies() {
        return cookies;
    }

    public Response cookies(List<Cookie> cookies) {
        this.cookies = cookies;
        return this;
    }

    public List<Header> headers() {
        return headers;
    }

    public Response headers(List<Header> headers) {
        this.headers = headers;
        return this;
    }
}
