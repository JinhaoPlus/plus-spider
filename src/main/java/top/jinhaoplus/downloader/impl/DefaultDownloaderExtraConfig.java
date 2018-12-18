package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.ExtraConfig;

public class DefaultDownloaderExtraConfig extends ExtraConfig {

    public static final String CONNECTION_REQUEST_TIMEOUT = "DefaultDownloader.connectionRequestTimeout";
    public static final String CONNECT_TIMEOUT = "DefaultDownloader.connectTimeout";
    public static final String SOCKET_TIMEOUT = "DefaultDownloader.socketTimeout";
    public static final String MAX_CONN_TOTAL = "DefaultDownloader.maxConnTotal";
    public static final String MAX_PER_ROUTE = "DefaultDownloader.maxPerRoute";

    @Override
    public void config() {
        set(CONNECTION_REQUEST_TIMEOUT, 10000);
        set(CONNECT_TIMEOUT, 10000);
        set(SOCKET_TIMEOUT, 10000);
        set(MAX_CONN_TOTAL, 10);
        set(MAX_PER_ROUTE, 10);
    }
}
