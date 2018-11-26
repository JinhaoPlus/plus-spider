package top.jinhaoplus.core;

import com.google.common.collect.Lists;
import top.jinhaoplus.http.Request;

import java.util.List;

public class Result {
    private List<Item> items = Lists.newArrayList();
    private List<Request> requests = Lists.newArrayList();

    public List<Item> items() {
        return items;
    }

    public Result items(List<Item> items) {
        this.items = items;
        return this;
    }

    public Result addItem(Item item) {
        this.items.add(item);
        return this;
    }

    public Result addItems(List<Item> items) {
        this.items.addAll(items);
        return this;
    }

    public List<Request> requests() {
        return requests;
    }

    public Result requests(List<Request> requests) {
        this.requests = requests;
        return this;
    }

    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }

    public Result addRequests(List<Request> requests) {
        this.requests.addAll(requests);
        return this;
    }
}
