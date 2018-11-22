package top.jinhaoplus.downloader;

import top.jinhaoplus.core.Config;

public class DownloaderCreator {
    public static Downloder create(Config config) throws DownloaderException {
        try {
            DownloaderFactory downloaderFactory = (DownloaderFactory) Class.forName(config.downloaderFactoryClass()).newInstance();
            return downloaderFactory.newInstance(config);
        } catch (Exception e) {
            throw new DownloaderException(e);
        }
    }
}
