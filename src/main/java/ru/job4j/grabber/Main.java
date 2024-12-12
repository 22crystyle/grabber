package ru.job4j.grabber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.grabber.model.JdbcStore;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Main {
    public static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var config = new Config();
        config.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("application.properties")).getFile());

        try {
            var store = new JdbcStore(DriverManager.getConnection(
                    config.get("spring.datasource.url"),
                    config.get("spring.datasource.name"),
                    config.get("spring.datasource.password")));

            HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
            List<Post> list = parse.list("https://career.habr.com/vacancies?page=1&q=Java%20developer&type=all");
            for (Post post : list) {
                store.save(post);
            }

            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
        } catch (SQLException sql) {
            log.error("When create a connection", sql);
        }
    }
}
