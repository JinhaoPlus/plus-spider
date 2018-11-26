package top.jinhaoplus.scheduler;

import top.jinhaoplus.config.Config;

public class DefaultSchedulerFactory implements SchedulerFactory {

    @Override
    public Scheduler newInstance(Config config) {
        return new DefaultScheduler(config);
    }
}
