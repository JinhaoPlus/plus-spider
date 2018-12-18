package top.jinhaoplus.downloader;

import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.capacity.DownloadingCapacity;

public class DownloaderCreator {

    public static Downloder create(Config config) throws DownloaderException {
        try {
            DownloaderFactory downloaderFactory = (DownloaderFactory) Class.forName(config.downloaderFactoryClass()).newInstance();
            Downloder downloder = downloaderFactory.newInstance(config);

            DownloadingCapacity downloadingCapacity = (DownloadingCapacity) Class.forName(config.downloadingCapacityClass()).newInstance();
            downloadingCapacity.init(config.maxDownloadingCapacity());
            downloder.initDownloadCapacity(downloadingCapacity);

            return downloder;
        } catch (Exception e) {
            throw new DownloaderException(e);
        }
    }
}
