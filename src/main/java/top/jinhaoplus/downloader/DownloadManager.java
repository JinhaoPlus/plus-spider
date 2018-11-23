package top.jinhaoplus.downloader;

import com.google.common.collect.Lists;
import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

import java.util.List;

public class DownloadManager {

    private Downloder downloder;

    private List<DownloadFilter> downloadFilterChain = Lists.newArrayList();

    public DownloadManager(Downloder downloder, Config config) throws DownloaderException {
        this.downloder = downloder;
        List<String> downloaderFilterClasses = config.downloaderFilterClasses();
        for (String downloaderFilterClassStr : downloaderFilterClasses) {
            try {
                Class<?> downloaderFilterClass = Class.forName(downloaderFilterClassStr);
                if (DownloadFilter.class.isAssignableFrom(downloaderFilterClass)) {
                    downloadFilterChain.add((DownloadFilter) downloaderFilterClass.newInstance());
                } else {
                    throw new DownloaderException("downloadFilter: " + downloaderFilterClassStr + " init error");
                }
            } catch (Exception e) {
                throw new DownloaderException("[DownloadManager] DownloadManager init error: " + e);
            }
        }
    }

    public EndPoint executeDownload(Request request) {
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            if (!downloadFilter.filterBefore(request)) {
                return new ErrorResponse(request).error("");
            }
        }
        Response response;
        try {
            response = downloder.download(request);
        } catch (DownloaderException e) {
            response = new ErrorResponse(request).error(e.getMessage());
        }
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            if (!downloadFilter.filterAfter(response)) {
                return new ErrorResponse(request).error("");
            }
        }
        return response;
    }
}
