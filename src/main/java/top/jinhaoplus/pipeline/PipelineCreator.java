package top.jinhaoplus.pipeline;

import top.jinhaoplus.core.Config;

public class PipelineCreator {
    public static Pipeline create(Config config) throws PipelineException {
        try {
            PipelineFactory pipelineFactory = (PipelineFactory) Class.forName(config.pipelineFactoryClass()).newInstance();
            return pipelineFactory.newInstance(config);
        } catch (Exception e) {
            throw new PipelineException(e);
        }
    }
}
