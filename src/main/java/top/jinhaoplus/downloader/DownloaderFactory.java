package top.jinhaoplus.downloader;

import top.jinhaoplus.config.Config;

public interface DownloaderFactory {
    Downloder newInstance(Config config) throws DownloaderException;
}
