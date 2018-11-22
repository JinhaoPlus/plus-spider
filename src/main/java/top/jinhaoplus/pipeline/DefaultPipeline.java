package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Item;

public class DefaultPipeline implements Pipeline {

    @Override
    public void process(Item item) {
        System.out.println(item);
    }
}
