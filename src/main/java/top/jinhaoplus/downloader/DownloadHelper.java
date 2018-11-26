package top.jinhaoplus.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import top.jinhaoplus.http.*;

import java.util.List;

public class DownloadHelper {

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static HttpUriRequest convertHttpRequest(Request request) {
        HttpUriRequest httpRequest;
        switch (request.method()) {
            case GET:
                httpRequest = new HttpGet(request.url());
                break;
            case POST:
                httpRequest = new HttpPost(request.url());
                break;
            case PUT:
                httpRequest = new HttpPut(request.url());
                break;
            case DELETE:
                httpRequest = new HttpDelete(request.url());
                break;
            default:
                return null;
        }
        List<RequestHeader> headers = request.headers();
        if (headers != null && headers.size() > 0) {
            for (RequestHeader header : headers) {
                httpRequest.addHeader(new BasicHeader(header.name(), header.value()));
            }
        }
        return httpRequest;
    }

    public static BasicCookieStore convertCookies(Request request) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        List<RequestCookie> cookies = request.cookies();
        if (cookies != null && cookies.size() > 0) {
            for (RequestCookie cookie : cookies) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.name(), cookie.value());
                basicClientCookie.setDomain(cookie.domain());
                basicClientCookie.setPath(cookie.path());
                cookieStore.addCookie(basicClientCookie);
            }
        }
        return cookieStore;
    }

    public static Response convertHttpResponse(HttpResponse httpResponse, Request request, int statusCode) {
        HttpEntity httpEntity = httpResponse.getEntity();
        try {
            String resultText = EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
            return new Response(request).statusCode(statusCode).resultText(resultText);
        } catch (Exception e) {
            return new ErrorResponse(request);
        }
    }
}
