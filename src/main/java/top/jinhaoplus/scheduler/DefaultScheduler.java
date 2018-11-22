package top.jinhaoplus.scheduler;

import com.google.common.util.concurrent.Uninterruptibles;
import top.jinhaoplus.core.Config;
import top.jinhaoplus.http.Request;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DefaultScheduler implements Scheduler {

    private BlockingDeque<Request> deque = new LinkedBlockingDeque<>();

    private long interval;
    private TimeUnit timeUnit;


    public DefaultScheduler(Config config) {
        interval = Long.valueOf(config.interval());
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
        deque.add(request);
    }

    @Override
    public Request poll() {
        Uninterruptibles.sleepUninterruptibly(interval, timeUnit);
        return deque.poll();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }
}