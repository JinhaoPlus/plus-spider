package top.jinhaoplus.downloader;

import top.jinhaoplus.config.Config;

public class DefaultDownloaderFactory implements DownloaderFactory {
    @Override
    public Downloder newInstance(Config config) throws DownloaderException {
        return new DefaultDownloader(config);
    }
}
