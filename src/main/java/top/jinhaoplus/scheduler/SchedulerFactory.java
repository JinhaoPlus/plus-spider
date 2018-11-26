package top.jinhaoplus.scheduler;

import top.jinhaoplus.config.Config;

public interface SchedulerFactory {
    Scheduler newInstance(Config config);
}
