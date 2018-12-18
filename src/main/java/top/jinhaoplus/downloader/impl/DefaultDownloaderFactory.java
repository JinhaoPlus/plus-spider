package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.downloader.DownloaderFactory;
import top.jinhaoplus.downloader.Downloder;

public class DefaultDownloaderFactory implements DownloaderFactory {
    @Override
    public Downloder newInstance(Config config) throws DownloaderException {
        DefaultDownloaderExtraConfig configurator = new DefaultDownloaderExtraConfig();
        configurator.config(config.extraConfig());
        return new DefaultDownloader(config);
    }
}
