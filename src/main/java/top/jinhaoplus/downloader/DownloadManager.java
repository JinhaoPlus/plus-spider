package top.jinhaoplus.downloader;

import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;
import top.jinhaoplus.http.ResponseStatus;

public class DownloadManager {

    private Downloder downloder;

    private int maxRetryTimes;

    public DownloadManager(Downloder downloder, Config config) {
        this.downloder = downloder;
        this.maxRetryTimes = Integer.valueOf(config.maxRetryTimes());
    }

    public Response executeDownload(Request request) {
        for (int i = 0; i < maxRetryTimes; i++) {
            try {
                Response response = downloder.download(request);
                if (ResponseStatus.SUCCESS == response.responseStatus()) {
                    return response;
                }
            } catch (DownloaderException e) {
                return new Response(ResponseStatus.EXCEPTION, request);
            }
        }
        return new Response(ResponseStatus.OUT_OF_RETRY, request);
    }
}
