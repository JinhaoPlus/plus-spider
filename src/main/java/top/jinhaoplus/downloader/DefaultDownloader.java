package top.jinhaoplus.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.helper.DownloadHelper;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.HttpRequestContext;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

/**
 * @author jinhaoluo
 */
public class DefaultDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDownloader.class);

    private HttpClient httpClient;

    private volatile Boolean downloading = false;

    public DefaultDownloader(Config config) throws DownloaderException {
        int connectionRequestTimeout = (int) config.extraConfigs().getOrDefault("DefaultDownloader.connectionRequestTimeout", 10000);
        int connectTimeout = (int) config.extraConfigs().getOrDefault("DefaultDownloader.connectTimeout", 10000);
        int socketTimeout = (int) config.extraConfigs().getOrDefault("DefaultDownloader.socketTimeout", 10000);

        int maxConnTotal = (int) config.extraConfigs().getOrDefault("DefaultDownloader.maxConnTotal", 10);
        int maxPerRoute = (int) config.extraConfigs().getOrDefault("DefaultDownloader.maxPerRoute", 10);

        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            HttpClientBuilder builder = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxPerRoute);
            httpClient = builder.build();
        } catch (Exception e) {
            throw new DownloaderException("[DefaultDownloader] DefaultDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) {
        HttpRequestContext httpRequestContext = DownloadHelper.prepareHttpRequest(request);

        try {
            downloading = true;
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
            downloading = false;
        }
    }

    @Override
    public boolean hasDownloadCapacity() {
        return !downloading;
    }

    @Override
    public boolean allDownloadFinished() {
        return !downloading;
    }
}
