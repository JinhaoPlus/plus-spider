package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.downloader.DownloaderFactory;
import top.jinhaoplus.downloader.Downloder;

public class DefaultAsyncDownloaderFactory implements DownloaderFactory {
    @Override
    public Downloder newInstance(Config config) throws DownloaderException {
        DefaultAsyncDownloaderExtraConfigrator.config(config.extraConfig());
        return new DefaultAsyncDownloader(config);
    }
}
