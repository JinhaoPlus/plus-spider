package top.jinhaoplus.downloader.impl;

import top.jinhaoplus.config.ExtraConfig;

public class DefaultDownloaderExtraConfigrator {

    public static final String CONNECTION_REQUEST_TIMEOUT = "DefaultDownloader.connectionRequestTimeout";
    public static final String CONNECT_TIMEOUT = "DefaultDownloader.connectTimeout";
    public static final String SOCKET_TIMEOUT = "DefaultDownloader.socketTimeout";
    public static final String MAX_CONN_TOTAL = "DefaultDownloader.maxConnTotal";
    public static final String MAX_PER_ROUTE = "DefaultDownloader.maxPerRoute";

    public static void config(ExtraConfig extraConfig) {
        extraConfig.set(CONNECTION_REQUEST_TIMEOUT, 10000);
        extraConfig.set(CONNECT_TIMEOUT, 10000);
        extraConfig.set(SOCKET_TIMEOUT, 10000);
        extraConfig.set(MAX_CONN_TOTAL, 10);
        extraConfig.set(MAX_PER_ROUTE, 10);
    }
}
