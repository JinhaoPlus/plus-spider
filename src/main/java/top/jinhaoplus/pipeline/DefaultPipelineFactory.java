package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Config;

public class DefaultPipelineFactory implements PipelineFactory {

    @Override
    public Pipeline newInstance(Config config) {
        return new DefaultPipeline();
    }
}
