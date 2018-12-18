package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.ExtraConfig;

public class DefaultAsyncDownloaderExtraConfigrator {

    public static final String CONNECTION_REQUEST_TIMEOUT = "DefaultAsyncDownloader.connectionRequestTimeout";
    public static final String CONNECT_TIMEOUT = "DefaultAsyncDownloader.connectTimeout";
    public static final String SOCKET_TIMEOUT = "DefaultAsyncDownloader.socketTimeout";
    public static final String IO_THREAD_COUNT = "DefaultAsyncDownloader.ioThreadCount";
    public static final String MAX_CONN_TOTAL = "DefaultAsyncDownloader.maxConnTotal";
    public static final String MAX_PER_ROUTE = "DefaultAsyncDownloader.maxPerRoute";

    public static void config(ExtraConfig extraConfig) {
        extraConfig.set(CONNECTION_REQUEST_TIMEOUT, 10000);
        extraConfig.set(CONNECT_TIMEOUT, 10000);
        extraConfig.set(SOCKET_TIMEOUT, 10000);
        extraConfig.set(IO_THREAD_COUNT, Runtime.getRuntime().availableProcessors());
        extraConfig.set(MAX_CONN_TOTAL, 10);
        extraConfig.set(MAX_PER_ROUTE, 10);
    }
}
