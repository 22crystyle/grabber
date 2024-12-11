package ru.job4j.grabber.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.SQLException;

public class SuperJobGrab implements Job {
    public static final Logger log = LogManager.getLogger(SuperJobGrab.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var store = (Store) context.getJobDetail().getJobDataMap().get("store");
        try {
            for (var post : store.getAll()) {
                System.out.println(post.getTitle());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
