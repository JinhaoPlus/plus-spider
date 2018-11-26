package top.jinhaoplus.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;

/**
 * @author jinhaoluo
 */
public class DefaultDownloader implements Downloder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDownloader.class);

    private HttpClient httpClient;
    private HttpClientBuilder builder;

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
            builder = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxPerRoute);
        } catch (Exception e) {
            throw new DownloaderException("[DefaultDownloader] DefaultDownloader init error" + e.getMessage());
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) {
        HttpUriRequest httpRequest = prepareClientAndRequest(request);

        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                callback.handleResponse(DownloadHelper.convertHttpResponse(httpResponse, request, statusCode));
            } else {
                LOGGER.error("download failed, statusCode={}", statusCode);
                callback.handleResponse(new ErrorResponse(request).statusCode(statusCode));
            }
        } catch (Exception e) {
            LOGGER.error("download throw exception: e={}", e.getMessage());
            callback.handleResponse(new ErrorResponse(request));
        } finally {
            resetCookies();
        }
    }

    private HttpUriRequest prepareClientAndRequest(Request request) {
        modifyCookies(request);
        httpClient = builder.build();
        return DownloadHelper.convertHttpRequest(request);
    }

    private void modifyCookies(Request request) {
        builder.setDefaultCookieStore(DownloadHelper.convertCookies(request));
    }

    private void resetCookies() {
        builder.setDefaultCookieStore(new BasicCookieStore());
    }
}
