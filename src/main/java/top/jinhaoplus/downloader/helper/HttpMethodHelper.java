package top.jinhaoplus.downloader.helper;

import org.apache.http.client.methods.*;
import top.jinhaoplus.http.Request;

public class HttpMethodHelper {

    public static HttpRequestBase convertRequest(Request request) {
        HttpRequestBase httpRequest = null;
        switch (request.method()) {
            case GET:
                httpRequest = new HttpGet(request.url());
                break;
            case HEAD:
                httpRequest = new HttpHead(request.url());
                break;
            case TRACE:
                httpRequest = new HttpTrace(request.url());
                break;
            case POST:
                httpRequest = new HttpPost(request.url());
                break;
            case PUT:
                httpRequest = new HttpPut(request.url());
                break;
            case PATCH:
                httpRequest = new HttpPatch(request.url());
                break;
            case DELETE:
                httpRequest = new HttpDelete(request.url());
                break;
            default:
                break;
        }
        return httpRequest;
    }

}
