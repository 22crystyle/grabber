package ru.job4j.grabber.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import ru.job4j.grabber.stores.Store;

@Slf4j
public class SuperJobGrab implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        var store = (Store) context.getJobDetail().getJobDataMap().get("store");
        for (var post : store.getAll()) {
            System.out.println(post.getTitle());
        }
    }
}
