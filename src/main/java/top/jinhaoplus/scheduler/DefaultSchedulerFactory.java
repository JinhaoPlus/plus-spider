package top.jinhaoplus.scheduler;

import top.jinhaoplus.core.Config;

public class DefaultSchedulerFactory implements SchedulerFactory {

    @Override
    public Scheduler newInstance(Config config) {
        return new DefaultScheduler(config);
    }
}
