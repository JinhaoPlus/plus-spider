package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Item;

import java.util.List;

public interface Pipeline {
    void process(List<Item> items);
}
