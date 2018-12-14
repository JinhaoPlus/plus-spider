package top.jinhaoplus.core;

import com.google.common.collect.Lists;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DownloaderException;
import top.jinhaoplus.http.Request;

import java.util.List;

public class Spider {

    private Engine engine;

    public Spider() throws Exception {
        this(Config.defaultConfig());
    }

    public Spider(String startUrl) throws Exception {
        this(Config.defaultConfig(), startUrl);
    }

    public Spider(Request startRequest) throws Exception {
        this(Config.defaultConfig(), startRequest);
    }

    public Spider(Config config) throws Exception {
        this(config, Lists.newArrayList());
    }

    public Spider(Config config, String startUrl) throws Exception {
        this(config, Lists.newArrayList(new Request(startUrl)));
    }

    public Spider(Config config, Request startRequest) throws Exception {
        this(config, Lists.newArrayList(startRequest));
    }

    public Spider(Config config, List<Request> startRequests) throws Exception {
        this.engine = new Engine(config, startRequests);
    }

    public void crawl() throws DownloaderException {
        engine.start();
    }
}
