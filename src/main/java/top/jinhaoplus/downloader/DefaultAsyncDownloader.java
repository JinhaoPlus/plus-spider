package top.jinhaoplus.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

/**
 * @author jinhaoluo
 */
public class DefaultAsyncDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAsyncDownloader.class);

    private static final String DEFAULT_CHARSET = "UTF-8";
    private HttpAsyncClient httpAsyncClient;
    private HttpAsyncClientBuilder builder;

    public DefaultAsyncDownloader(Config config) throws DownloaderException {

        int connectionRequestTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.connectionRequestTimeout", 10000);
        int connectTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.connectTimeout", 10000);
        int socketTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.socketTimeout", 10000);

        int ioThreadCount = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.ioThreadCount", Runtime.getRuntime().availableProcessors());
        int maxConnTotal = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.maxConnTotal", 10);
        int maxPerRoute = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.maxPerRoute", 10);

        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout)
                    .build();

            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                    setIoThreadCount(ioThreadCount)
                    .setSoKeepAlive(true)
                    .build();

            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            connManager.setMaxTotal(maxConnTotal);
            connManager.setDefaultMaxPerRoute(maxPerRoute);

            builder = HttpAsyncClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig);
        } catch (Exception e) {
            throw new DownloaderException("[DefaultAsyncDownloader] DefaultAsyncDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) {
        HttpUriRequest httpRequest = prepareClientAndRequest(request);

        try {
            httpAsyncClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    try {
                        int statusCode = httpResponse.getStatusLine().getStatusCode();
                        if (HttpStatus.SC_OK == statusCode) {
                            HttpEntity httpEntity = httpResponse.getEntity();
                            String resultText = EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
                            Response response = new Response(request).statusCode(statusCode).resultText(resultText);
                            callback.handleResponse(response);
                        } else {
                            LOGGER.error("download failed, statusCode={}", statusCode);
                            callback.handleResponse(new ErrorResponse(request).statusCode(statusCode));
                        }
                    } catch (Exception e) {
                        LOGGER.error("async download throw exception: e={}", e.getMessage());
                        callback.handleResponse(new ErrorResponse(request));
                    }
                }

                @Override
                public void failed(Exception e) {
                    LOGGER.error("async download failed: e={}", e.getMessage());
                    callback.handleResponse(new ErrorResponse(request));
                }

                @Override
                public void cancelled() {
                    LOGGER.error("async download cancelled");
                    callback.handleResponse(new ErrorResponse(request));
                }
            });
        } catch (Exception e) {
            LOGGER.error("download throw exception: e={}", e.getMessage());
            callback.handleResponse(new ErrorResponse(request));
        } finally {
            resetCookies();
        }

    }

    private HttpUriRequest prepareClientAndRequest(Request request) {
        modifyCookies(request);
        httpAsyncClient = builder.build();
        return DownloadHelper.convertHttpRequest(request);
    }

    private void modifyCookies(Request request) {
        builder.setDefaultCookieStore(DownloadHelper.convertCookies(request));
    }

    private void resetCookies() {
        builder.setDefaultCookieStore(new BasicCookieStore());
    }
}
