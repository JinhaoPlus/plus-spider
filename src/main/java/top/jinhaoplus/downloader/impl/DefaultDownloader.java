package top.jinhaoplus.downloader.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
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
public class DefaultDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDownloader.class);

    private HttpClient httpClient;

    private Proxy proxyConfig;

    private RequestConfig requestConfig;

    private DownloadingCapacity downloadingCapacity;

    public DefaultDownloader(Config config) throws DownloaderException {
        int connectionRequestTimeout = (int) config.extraConfig().get(DefaultDownloaderExtraConfigurator.CONNECTION_REQUEST_TIMEOUT);
        int connectTimeout = (int) config.extraConfig().get(DefaultDownloaderExtraConfigurator.CONNECT_TIMEOUT);
        int socketTimeout = (int) config.extraConfig().get(DefaultDownloaderExtraConfigurator.SOCKET_TIMEOUT);

        int maxConnTotal = (int) config.extraConfig().get(DefaultDownloaderExtraConfigurator.MAX_CONN_TOTAL);
        int maxPerRoute = (int) config.extraConfig().get(DefaultDownloaderExtraConfigurator.MAX_PER_ROUTE);

        proxyConfig = config.proxyConfig();

        try {
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            HttpClientBuilder builder = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultCredentialsProvider(HttpProxyHelper.getProxyCredentials(proxyConfig))
                    .setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxPerRoute);
            httpClient = builder.build();
        } catch (Exception e) {
            throw new DownloaderException("[DefaultDownloader] DefaultDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) throws DownloaderException {
        HttpRequestContext httpRequestContext = DownloadHelper.prepareHttpRequest(request, proxyConfig, requestConfig);

        try {
            downloadingCapacity.consume();
            HttpResponse httpResponse = httpClient.execute(httpRequestContext.httpRequest(), httpRequestContext.context());
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Response response = new Response(request).statusCode(statusCode);
            if (HttpStatus.SC_OK == statusCode) {
                callback.handleResponse(DownloadHelper.convertHttpResponse(httpResponse, request, statusCode));
            } else {
                LOGGER.error("download failed, statusCode={}", statusCode);
                callback.handleResponse(ErrorResponse.wrap(response));
            }
        } catch (Exception e) {
            LOGGER.error("download throw exception: e={}", e.getMessage());
            callback.handleResponse(new ErrorResponse(request));
        } finally {
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
