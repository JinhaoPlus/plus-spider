package top.jinhaoplus.pipeline;

import top.jinhaoplus.config.Config;

public interface PipelineFactory {
    Pipeline newInstance(Config config);
}
