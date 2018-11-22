package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Config;

public interface PipelineFactory {
    Pipeline newInstance(Config config);
}
