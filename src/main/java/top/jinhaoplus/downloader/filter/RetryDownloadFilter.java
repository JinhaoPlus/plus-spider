package top.jinhaoplus.downloader.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

public class RetryDownloadFilter implements DownloadFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryDownloadFilter.class);

    private static final String RETRY_TIMES_KEY = "retryTimes";

    private int maxRetryTimes;

    @Override
    public EndPoint processRequest(Request request) {
        return request;
    }

    @Override
    public EndPoint processResponse(Response response) {
        if (!(response instanceof ErrorResponse)) {
            return response;
        } else {
            Request request = response.request();
            Integer retryTimes = (Integer) request.meta().getOrDefault(RETRY_TIMES_KEY, 0);
            if (retryTimes < maxRetryTimes) {
                retryTimes = retryTimes + 1;
                request.meta().put(RETRY_TIMES_KEY, retryTimes);
                LOGGER.debug("need to retry, retryTimes={}", retryTimes);
                return request;
            } else {
                return response;
            }
        }
    }

    @Override
    public void config(Config config) {
        maxRetryTimes = config.maxRetryTimes();
    }
}
