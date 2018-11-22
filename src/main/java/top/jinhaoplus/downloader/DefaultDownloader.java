package top.jinhaoplus.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.*;

import java.util.List;

/**
 * @author jinhaoluo
 */
public class DefaultDownloader implements Downloder {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private HttpClient httpClient;
    private HttpClientBuilder builder;

    public DefaultDownloader(Config config) throws DownloaderException {
        String connectionRequestTimeout = config.extraConfigs().getOrDefault("DefaultDownloader.connectionRequestTimeout", "10");
        String connectTimeout = config.extraConfigs().getOrDefault("DefaultDownloader.ConnectTimeout", "10");
        String maxConnTotal = config.extraConfigs().getOrDefault("DefaultDownloader.MaxConnTotal", "10");
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout))
                    .setConnectTimeout(Integer.valueOf(connectTimeout))
                    .build();
            builder = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setMaxConnTotal(Integer.valueOf(maxConnTotal));
        } catch (Exception e) {
            throw new DownloaderException("[DefaultDownloader] DefaultDownloader init error" + e.getMessage());
        }
    }

    @Override
    public Response download(Request request) throws DownloaderException {
        HttpUriRequest httpRequest = prepareClientAndRequest(request);
        if (httpRequest == null) {
            throw new DownloaderException("[DefaultDownloader] httpRequest convert error");
        }

        try {
            HttpResponse response = httpClient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity httpEntity = response.getEntity();
                String resultText = EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
                System.out.println("resultText:" + resultText);
                return new Response(statusCode, resultText, request);
            } else {
                return new Response(statusCode, ResponseStatus.UNUSUAL, request);
            }
        } catch (Exception e) {
            throw new DownloaderException("[DefaultDownloader] httpRequest execute error: " + e.getMessage());
        }
    }

    private HttpUriRequest prepareClientAndRequest(Request request) {
        modifyCookies(request);
        httpClient = builder.build();
        return convertHttpRequest(request);
    }

    private void modifyCookies(Request request) {
        List<RequestCookie> cookies = request.cookies();
        if (cookies != null && cookies.size() > 0) {
            BasicCookieStore cookieStore = new BasicCookieStore();
            for (RequestCookie cookie : cookies) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.name(), cookie.value());
                basicClientCookie.setDomain(cookie.domain());
                basicClientCookie.setPath(cookie.path());
                cookieStore.addCookie(basicClientCookie);
            }
            builder.setDefaultCookieStore(cookieStore);
        }

    }

    private HttpUriRequest convertHttpRequest(Request request) {
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
}
