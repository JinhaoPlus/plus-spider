package top.jinhaoplus.downloader;

import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

public class RetryDownloadFilter implements DownloadFilter {

    private static final String RETRY_TIMES_KEY = "retryTimes";

    private int maxRetryTimes;

    @Override
    public EndPoint filterBefore(Request request) {
        Integer retryTimes = (Integer) request.meta().getOrDefault(RETRY_TIMES_KEY, 0);
        System.out.println("下载第" + retryTimes + "次");
        if (retryTimes <= maxRetryTimes) {
            return request;
        }
        return request;
    }

    @Override
    public EndPoint filterAfter(Response response) {
        Integer retryTimes = (Integer) response.request().meta().getOrDefault(RETRY_TIMES_KEY, 0);
        response.request().meta().put(RETRY_TIMES_KEY, retryTimes + 1);
        return response;
    }

    @Override
    public void config(Config config) {
        maxRetryTimes = Integer.valueOf(config.maxRetryTimes());
    }
}
