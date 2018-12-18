package top.jinhaoplus.downloader;

import top.jinhaoplus.downloader.capacity.DownloadingCapacity;
import top.jinhaoplus.http.Request;

public interface Downloder {

    void initDownloadCapacity(DownloadingCapacity downloadingCapacity);

    boolean hasDownloadCapacity();

    boolean allDownloadFinished();

    void download(Request request, DownloadCallback callback) throws DownloaderException;
}
