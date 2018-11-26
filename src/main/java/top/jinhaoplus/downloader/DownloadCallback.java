package top.jinhaoplus.downloader;

import com.google.common.collect.Lists;
import top.jinhaoplus.core.Result;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.ErrorResponse;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;
import top.jinhaoplus.parser.Parser;
import top.jinhaoplus.pipeline.Pipeline;
import top.jinhaoplus.scheduler.Scheduler;

import java.util.List;

public class DownloadCallback {

    private Parser parser;

    private Scheduler scheduler;

    private Pipeline pipeline;

    private List<DownloadFilter> downloadFilterChain = Lists.newArrayList();

    public DownloadCallback(Parser parser, Scheduler scheduler, Pipeline pipeline) {
        this.parser = parser;
        this.scheduler = scheduler;
        this.pipeline = pipeline;
    }

    public List<DownloadFilter> downloadFilterChain() {
        return downloadFilterChain;
    }

    public DownloadCallback downloadFilterChain(List<DownloadFilter> downloadFilterChain) {
        this.downloadFilterChain = downloadFilterChain;
        return this;
    }

    public void handleResponse(Response response) {
        if (!(response instanceof ErrorResponse)) {
            Result result = parser.parse(response);
            if (result != null) {
                pipeline.process(result.items());
                scheduler.batchPush(result.requests());
            }
        }
        for (DownloadFilter downloadFilter : downloadFilterChain) {
            EndPoint endPoint = downloadFilter.processResponse(response);
            if (endPoint instanceof Request) {
                Request request = (Request) endPoint;
                scheduler.push(request);
            }
        }
    }
}
