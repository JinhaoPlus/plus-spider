package top.jinhaoplus.core;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DownloadCallback;
import top.jinhaoplus.downloader.DownloadManager;
import top.jinhaoplus.downloader.DownloaderCreator;
import top.jinhaoplus.downloader.Downloder;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.parser.Parser;
import top.jinhaoplus.parser.ParserCreator;
import top.jinhaoplus.pipeline.Pipeline;
import top.jinhaoplus.pipeline.PipelineCreator;
import top.jinhaoplus.scheduler.Scheduler;
import top.jinhaoplus.scheduler.SchedulerCreator;

import java.util.List;
import java.util.concurrent.TimeUnit;

class Engine {

    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    private Config config;

    private List<Request> startRequests = Lists.newArrayList();

    private Scheduler scheduler;

    private Downloder downloder;

    private DownloadManager downloadManager;

    private Parser parser;

    private Pipeline pipeline;

    Engine(Config config, List<Request> startRequests) throws Exception {
        this.config = config;
        if (startRequests != null && startRequests.size() > 0) {
            this.startRequests = startRequests;
        }
        init();
    }

    private void init() throws Exception {
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

        LOGGER.info("\n[Engine]{} start engine ...", config.name());
        while (true) {
            if (!downloder.hasDownloadCapacity()) {
                waitOneSecond();
                continue;
            }
            Request request = scheduler.poll();
            if (request != null) {
                downloadManager.executeDownload(
                        request,
                        new DownloadCallback(parser, scheduler, pipeline)
                );
            } else if (!downloder.allDownloadFinished()) {
                waitOneSecond();
            } else if (scheduler.isEmpty()) {
                LOGGER.info("\n[Engine]{} close engine ...", config.name());
                break;
            }
        }
    }

    private void waitOneSecond() {
        Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
    }
}
