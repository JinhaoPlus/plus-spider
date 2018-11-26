package top.jinhaoplus.downloader;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

import java.util.List;

public class DownloadManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private Downloder downloder;

    private List<DownloadFilter> downloadFilterChain = Lists.newArrayList();

    public DownloadManager(Downloder downloder, Config config) throws DownloaderException {
        this.downloder = downloder;
        List<String> downloaderFilterClasses = config.downloaderFilterClasses();
        for (String downloaderFilterClassStr : downloaderFilterClasses) {
            try {
                Class<?> downloaderFilterClass = Class.forName(downloaderFilterClassStr);
                if (DownloadFilter.class.isAssignableFrom(downloaderFilterClass)) {
                    DownloadFilter downloadFilter = (DownloadFilter) downloaderFilterClass.newInstance();
                    downloadFilter.config(config);
                    downloadFilterChain.add(downloadFilter);
                } else {
                    throw new DownloaderException("[DownloadManager] downloadFilter: " + downloaderFilterClassStr + " init error");
                }
            } catch (Exception e) {
                throw new DownloaderException("[DownloadManager] DownloadManager init error: " + e);
            }
        }
    }

    public EndPoint executeDownload(Request request) {
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            EndPoint endPoint = downloadFilter.processRequest(request);
            if (endPoint instanceof Response) {
                return endPoint;
            }
        }
        Response response;
        try {
            response = downloder.download(request);
        } catch (DownloaderException e) {
            LOGGER.error("[DownloadManager] download error, e={}", e.getMessage());
            response = new ErrorResponse(request).error(e.getMessage());
        }
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            EndPoint endPoint = downloadFilter.processResponse(response);
            if (endPoint instanceof Request) {
                return endPoint;
            }
        }
        return response;
    }
}
