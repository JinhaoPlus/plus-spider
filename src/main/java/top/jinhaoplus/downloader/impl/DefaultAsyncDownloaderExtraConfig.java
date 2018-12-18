package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.ExtraConfig;

public class DefaultAsyncDownloaderExtraConfig extends ExtraConfig {

    public static final String CONNECTION_REQUEST_TIMEOUT = "DefaultAsyncDownloader.connectionRequestTimeout";
    public static final String CONNECT_TIMEOUT = "DefaultAsyncDownloader.connectTimeout";
    public static final String SOCKET_TIMEOUT = "DefaultAsyncDownloader.socketTimeout";
    public static final String IO_THREAD_COUNT = "DefaultAsyncDownloader.ioThreadCount";
    public static final String MAX_CONN_TOTAL = "DefaultAsyncDownloader.maxConnTotal";
    public static final String MAX_PER_ROUTE = "DefaultAsyncDownloader.maxPerRoute";

    @Override
    public void config() {
        set(CONNECTION_REQUEST_TIMEOUT, 10000);
        set(CONNECT_TIMEOUT, 10000);
        set(SOCKET_TIMEOUT, 10000);
        set(IO_THREAD_COUNT, Runtime.getRuntime().availableProcessors());
        set(MAX_CONN_TOTAL, 10);
        set(MAX_PER_ROUTE, 10);
    }
}
