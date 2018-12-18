package top.jinhaoplus.downloader.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DownloadCallback;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.downloader.Downloder;
import top.jinhaoplus.downloader.capacity.DownloadingCapacity;
import top.jinhaoplus.downloader.helper.DownloadHelper;
import top.jinhaoplus.downloader.helper.HttpProxyHelper;
import top.jinhaoplus.http.*;

/**
 * @author jinhaoluo
 */
public class DefaultAsyncDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAsyncDownloader.class);

    private CloseableHttpAsyncClient httpAsyncClient;

    private Proxy proxyConfig;

    private RequestConfig requestConfig;

    private DownloadingCapacity downloadingCapacity;

    public DefaultAsyncDownloader(Config config) throws DownloaderException {

        int connectionRequestTimeout = (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.CONNECTION_REQUEST_TIMEOUT);
        int connectTimeout = (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.CONNECT_TIMEOUT);
        int socketTimeout = (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.SOCKET_TIMEOUT);

        int ioThreadCount = (int) (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.IO_THREAD_COUNT);
        int maxConnTotal = (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.MAX_CONN_TOTAL);
        int maxPerRoute = (int) config.extraConfig().get(DefaultAsyncDownloaderExtraConfigrator.MAX_PER_ROUTE);

        proxyConfig = config.proxyConfig();

        try {
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
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultCredentialsProvider(HttpProxyHelper.getProxyCredentials(proxyConfig))
                    .build();
            httpAsyncClient.start();
        } catch (Exception e) {
            throw new DownloaderException("[DefaultAsyncDownloader] DefaultAsyncDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) throws DownloaderException {
        HttpRequestContext httpRequestContext = DownloadHelper.prepareHttpRequest(request, proxyConfig, requestConfig);
        downloadingCapacity.consume();
        try {
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
                                downloadingCapacity.free();
                            }
                        }

                        @Override
                        public void failed(Exception e) {
                            LOGGER.error("async download failed: e={}", e.getMessage());
                            callback.handleResponse(new ErrorResponse(request).error("async download failed: e=" + e.getMessage()));
                            downloadingCapacity.free();
                        }

                        @Override
                        public void cancelled() {
                            LOGGER.error("async download cancelled");
                            callback.handleResponse(new ErrorResponse(request).error("async download cancelled"));
                            downloadingCapacity.free();
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("download throw exception: e={}", e.getMessage());
            callback.handleResponse(new ErrorResponse(request));
            downloadingCapacity.free();
        }
    }

    @Override
    public void initDownloadCapacity(DownloadingCapacity downloadingCapacity) {
        this.downloadingCapacity = downloadingCapacity;
    }


    @Override
    public boolean hasDownloadCapacity() {
        return downloadingCapacity.hasFreeCapacity();
    }

    @Override
    public boolean allDownloadFinished() {
        return downloadingCapacity.allCapacityFree();
    }
}
