package top.jinhaoplus.downloader;

import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

public interface Downloder {
    Response download(Request request) throws DownloaderException;
}
