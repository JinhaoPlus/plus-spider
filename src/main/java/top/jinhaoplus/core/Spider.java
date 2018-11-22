package top.jinhaoplus.core;

import com.google.common.collect.Lists;
import top.jinhaoplus.http.Request;

import java.util.List;

public class Spider {

    private Engine engine;

    private Config config;

    public Spider() throws Exception {
        this(new Config());
    }

    public Spider(Config config) throws Exception {
        this(config, Lists.newArrayList());
    }

    public Spider(String startUrl) throws Exception {
        this(new Config(), startUrl);
    }

    public Spider(Config config, String startUrl) throws Exception {
        this(config, Lists.newArrayList(new Request(startUrl)));
    }

    public Spider(Config config, Request startRequest) throws Exception {
        this(config, Lists.newArrayList(startRequest));
    }

    public Spider(Config config, List<Request> startRequests) throws Exception {
        this.config = config;
        this.engine = new Engine(config, startRequests);
    }

    public void crawl() {
        engine.start();
    }
}
