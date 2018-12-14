package top.jinhaoplus.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.helper.DownloadHelper;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.HttpRequestContext;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jinhaoluo
 */
public class DefaultAsyncDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAsyncDownloader.class);

    private HttpAsyncClient httpAsyncClient;

    private RequestConfig requestConfig;

    private AtomicInteger downloadingCount = new AtomicInteger(0);

    private int maxDownloadingCount;

    public DefaultAsyncDownloader(Config config) throws DownloaderException {

        int maxDownloadingCount = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.maxDownloadingCount", 10);

        int connectionRequestTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.connectionRequestTimeout", 10000);
        int connectTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.connectTimeout", 10000);
        int socketTimeout = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.socketTimeout", 10000);

        int ioThreadCount = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.ioThreadCount", Runtime.getRuntime().availableProcessors());
        int maxConnTotal = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.maxConnTotal", 10);
        int maxPerRoute = (int) config.extraConfigs().getOrDefault("DefaultAsyncDownloader.maxPerRoute", 10);

        try {
            this.maxDownloadingCount = maxDownloadingCount;

            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout).build();

            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                    setIoThreadCount(ioThreadCount)
                    .setSoKeepAlive(true)
                    .build();

            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            connManager.setMaxTotal(maxConnTotal);
            connManager.setDefaultMaxPerRoute(maxPerRoute);

            httpAsyncClient = HttpAsyncClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig).build();
            ((CloseableHttpAsyncClient) httpAsyncClient).start();
        } catch (Exception e) {
            throw new DownloaderException("[DefaultAsyncDownloader] DefaultAsyncDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) throws DownloaderException {
        HttpRequestContext httpRequestContext = DownloadHelper.prepareHttpRequest(request, requestConfig);
        downloadingCount.incrementAndGet();

        try {
            LOGGER.debug("downloadingCount[+]=" + downloadingCount);
            httpAsyncClient.execute(
                    httpRequestContext.httpRequest(),
                    httpRequestContext.context(),
                    new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(HttpResponse httpResponse) {
                            try {
                                int statusCode = httpResponse.getStatusLine().getStatusCode();
                                Response response = new Response(request).statusCode(statusCode);
                                if (HttpStatus.SC_OK == statusCode) {
                                    callback.handleResponse(DownloadHelper.convertHttpResponse(httpResponse, request, statusCode));
                                } else {
                                    LOGGER.error("async download failed, statusCode={}", statusCode);
                                    callback.handleResponse(ErrorResponse.wrap(response));
                                }
                            } catch (Exception e) {
                                LOGGER.error("async download throw exception: e={}", e.getMessage());
                                callback.handleResponse(new ErrorResponse(request).error("async download throw exception: e=" + e.getMessage()));
                            } finally {
                                downloadingCount.decrementAndGet();
                                LOGGER.debug("downloadingCount[-]=" + downloadingCount);
                            }
                        }

                        @Override
                        public void failed(Exception e) {
                            LOGGER.error("async download failed: e={}", e.getMessage());
                            callback.handleResponse(new ErrorResponse(request).error("async download failed: e=" + e.getMessage()));
                            downloadingCount.decrementAndGet();
                            LOGGER.debug("downloadingCount[-]=" + downloadingCount);
                        }

                        @Override
                        public void cancelled() {
                            LOGGER.error("async download cancelled");
                            callback.handleResponse(new ErrorResponse(request).error("async download cancelled"));
                            downloadingCount.decrementAndGet();
                            LOGGER.debug("downloadingCount[-]=" + downloadingCount);
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("download throw exception: e={}", e.getMessage());
            callback.handleResponse(new ErrorResponse(request));
            downloadingCount.decrementAndGet();
            LOGGER.debug("downloadingCount[-]=" + downloadingCount);
        }
    }

    @Override
    public boolean hasDownloadCapacity() {
        return downloadingCount.get() < maxDownloadingCount;
    }

    @Override
    public boolean allDownloadFinished() {
        return downloadingCount.get() == 0;
    }
}
