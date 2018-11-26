package top.jinhaoplus.downloader;

import top.jinhaoplus.config.Config;

public class DefaultAsyncDownloaderFactory implements DownloaderFactory {
    @Override
    public Downloder newInstance(Config config) throws DownloaderException {
        return new DefaultAsyncDownloader(config);
    }
}
