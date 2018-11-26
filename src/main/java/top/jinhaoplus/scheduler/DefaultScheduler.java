package top.jinhaoplus.scheduler;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.Request;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DefaultScheduler implements Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScheduler.class);

    private BlockingDeque<Request> deque = new LinkedBlockingDeque<>();

    private long interval;
    private TimeUnit timeUnit;


    public DefaultScheduler(Config config) {
        interval = config.interval();
        timeUnit = TimeUnit.valueOf(config.timeUnit());
    }


    @Override
    public void batchPush(List<Request> requests) {
        if (requests != null && requests.size() > 0) {
            requests.forEach(this::push);
        }
    }

    @Override
    public void push(Request request) {
        LOGGER.debug("[↓]push request={}", request);
        deque.add(request);
    }

    @Override
    public Request poll() {
        Uninterruptibles.sleepUninterruptibly(interval, timeUnit);
        Request request = deque.poll();
        LOGGER.debug("[↑]poll request={}", request);
        return request;
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }
}
