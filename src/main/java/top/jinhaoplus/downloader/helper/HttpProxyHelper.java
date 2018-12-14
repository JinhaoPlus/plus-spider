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
import top.jinhaoplus.http.Request;

public class HttpProxyHelper {

    public static void modifyProxy(Request request, HttpRequestBase httpRequest, RequestConfig requestConfig) throws DownloaderException {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(requestConfig);
        Proxy requestProxy = request.proxy();
        if (requestProxy != null) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(AuthScope.ANY),
                    new UsernamePasswordCredentials(requestProxy.credentialUsername(), requestProxy.credentialPassword())
            );

            if (StringUtils.isBlank(requestProxy.host()) || requestProxy.port() == null) {
                throw new DownloaderException("[HttpProxyHelper] proxy host address must need a host and a port");
            }
            HttpHost proxy = new HttpHost(requestProxy.host(), requestProxy.port());
            RequestConfig config = requestConfigBuilder.setProxy(proxy).build();
            httpRequest.setConfig(config);
        }
    }
}
