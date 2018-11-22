package top.jinhaoplus.scheduler;

import top.jinhaoplus.core.Config;

public class SchedulerCreator {
    public static Scheduler create(Config config) throws SchedulerException {
        try {
            SchedulerFactory schedulerFactory = (SchedulerFactory) Class.forName(config.schedulerFactoryClass()).newInstance();
            return schedulerFactory.newInstance(config);
        } catch (Exception e) {
            throw new SchedulerException(e);
        }
    }
}
