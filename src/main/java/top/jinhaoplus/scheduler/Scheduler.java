package top.jinhaoplus.scheduler;

import top.jinhaoplus.http.Request;

import java.util.List;

public interface Scheduler {

    void batchPush(List<Request> requests);

    void push(Request request);

    Request poll();

    boolean isEmpty();
}
