package ru.job4j.grabber.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SuperJobGrab implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        var store = (Store) context.getJobDetail().getJobDataMap().get("store");
        for (var post : store.getAll()) {
            System.out.println(post.getTitle());
        }
    }
}
