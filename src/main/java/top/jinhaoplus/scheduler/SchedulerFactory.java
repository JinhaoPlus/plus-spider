package top.jinhaoplus.scheduler;

import top.jinhaoplus.core.Config;

public interface SchedulerFactory {
    Scheduler newInstance(Config config);
}
