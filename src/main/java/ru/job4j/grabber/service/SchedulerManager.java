package ru.job4j.grabber.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.stores.Store;

public class SchedulerManager {
    private static final Logger log = LogManager.getLogger(SchedulerManager.class);
    private Scheduler scheduler;

    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(int period, Class<SuperJobGrab> task, Store store) {
        try {
            var data = new JobDataMap();
            data.put("store", store);
            var job = JobBuilder.newJob(task)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(period)
                    .repeatForever();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            log.error("When init job", se);
        }
    }

    public void close() {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException se) {
                log.error("When shutdown scheduler", se);
            }
        }
    }
}
