package top.jinhaoplus.core;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import top.jinhaoplus.downloader.DownloadManager;
import top.jinhaoplus.downloader.DownloaderCreator;
import top.jinhaoplus.downloader.Downloder;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;
import top.jinhaoplus.parser.Parser;
import top.jinhaoplus.parser.ParserCreator;
import top.jinhaoplus.pipeline.Pipeline;
import top.jinhaoplus.pipeline.PipelineCreator;
import top.jinhaoplus.scheduler.Scheduler;
import top.jinhaoplus.scheduler.SchedulerCreator;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Engine {

    private Config config;

    private List<Request> startRequests = Lists.newArrayList();

    private Scheduler scheduler;

    private Downloder downloder;

    private DownloadManager downloadManager;

    private Parser parser;

    private Pipeline pipeline;

    Engine(Config config, List<Request> startRequests) throws Exception {
        this.config = config;
        this.startRequests = startRequests;
        init();
    }

    void init() throws Exception {
        scheduler = SchedulerCreator.create(config);
        downloder = DownloaderCreator.create(config);
        downloadManager = new DownloadManager(downloder, config);
        parser = ParserCreator.create(config);
        pipeline = PipelineCreator.create(config);
    }

    void start() {
        if (startRequests != null && startRequests.size() > 0) {
            scheduler.batchPush(startRequests);
        }
        startSchedule();
    }

    private void startSchedule() {

        while (true) {
            Request request = scheduler.poll();
            if (request != null) {
                Response response = downloadManager.executeDownload(request);
            } else if (scheduler.isEmpty()) {
                System.out.println("close");
                break;
            }
            this.waitOneSecond();
        }
    }

    private void waitOneSecond() {
        Uninterruptibles.sleepUninterruptibly(1L, TimeUnit.SECONDS);
    }
}
