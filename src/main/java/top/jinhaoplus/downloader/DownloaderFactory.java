package top.jinhaoplus.downloader;

import top.jinhaoplus.core.Config;

public interface DownloaderFactory {
    Downloder newInstance(Config config) throws DownloaderException;
}
