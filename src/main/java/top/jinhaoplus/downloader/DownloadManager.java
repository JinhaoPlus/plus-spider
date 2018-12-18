package top.jinhaoplus.downloader;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.filter.DownloadFilter;
import top.jinhaoplus.http.EndPoint;
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
                    throw new DownloaderException("[DownloadManager] downloadFilter: " + downloaderFilterClassStr + " convertRequest error");
                }
            } catch (Exception e) {
                throw new DownloaderException("[DownloadManager] DownloadManager convertRequest error: " + e);
            }
        }
    }

    public void executeDownload(Request request, DownloadCallback callback) throws DownloaderException {
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            EndPoint endPoint = downloadFilter.processRequest(request);
            if (endPoint instanceof Response) {
                return;
            }
        }
        callback.downloadFilterChain(downloadFilterChain);
        downloder.download(request, callback);
    }
}
