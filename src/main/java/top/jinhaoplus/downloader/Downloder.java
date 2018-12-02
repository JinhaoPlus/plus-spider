package top.jinhaoplus.downloader;

import top.jinhaoplus.http.Request;

public interface Downloder {
    void download(Request request, DownloadCallback callback);

    boolean hasDownloadCapacity();

    boolean allDownloadFinished();
}
