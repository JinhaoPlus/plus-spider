package top.jinhaoplus.downloader.helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.util.EntityUtils;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.http.*;

public class DownloadHelper {

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static HttpRequestContext prepareHttpRequest(Request request, Proxy proxyConfig, RequestConfig requestConfig) throws DownloaderException {
        HttpRequestBase httpRequest = initHttpRequest(request);
        HttpProxyHelper.modifyProxy(httpRequest, proxyConfig, requestConfig);
        HttpHeaderHelper.convertHeaders(request, httpRequest);
        HttpClientContext clientContext = prepareClientContext(request);
        return new HttpRequestContext(httpRequest, clientContext);
    }

    private static HttpRequestBase initHttpRequest(Request request) {
        HttpRequestBase httpRequest = null;
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
                break;
        }
        return httpRequest;
    }

    private static HttpClientContext prepareClientContext(Request request) {
        BasicCookieStore cookieStore = HttpCookieHelper.convertCookies(request);
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        return context;
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
