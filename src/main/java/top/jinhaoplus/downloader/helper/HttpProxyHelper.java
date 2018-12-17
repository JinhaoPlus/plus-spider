package top.jinhaoplus.downloader.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.http.Proxy;

public class HttpProxyHelper {

    public static CredentialsProvider getProxyCredentials(Proxy proxyConfig) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (proxyConfig != null) {
            credsProvider.setCredentials(
                    new AuthScope(proxyConfig.host(), proxyConfig.port()),
                    new UsernamePasswordCredentials(proxyConfig.credentialUsername(), proxyConfig.credentialPassword())
            );
        }
        return credsProvider;
    }

    public static void modifyProxy(HttpRequestBase httpRequest, Proxy proxyConfig, RequestConfig requestConfig) throws DownloaderException {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(requestConfig);
        if (proxyConfig != null) {
            if (StringUtils.isBlank(proxyConfig.host()) || proxyConfig.port() == null) {
                throw new DownloaderException("[HttpProxyHelper] proxyConfig must contain a host and a port");
            }
            HttpHost proxyHost = new HttpHost(proxyConfig.host(), proxyConfig.port());
            RequestConfig config = requestConfigBuilder.setProxy(proxyHost).build();
            httpRequest.setConfig(config);
        }
    }
}
