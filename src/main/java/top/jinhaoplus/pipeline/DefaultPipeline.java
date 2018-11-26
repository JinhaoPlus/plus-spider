package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Item;

import java.util.List;

public class DefaultPipeline implements Pipeline {


    @Override
    public void process(List<Item> items) {
        items.forEach(System.out::println);
    }
}
