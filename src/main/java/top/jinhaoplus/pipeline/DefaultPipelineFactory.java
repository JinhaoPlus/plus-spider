package top.jinhaoplus.pipeline;

import top.jinhaoplus.config.Config;

public class DefaultPipelineFactory implements PipelineFactory {

    @Override
    public Pipeline newInstance(Config config) {
        return new DefaultPipeline();
    }
}
